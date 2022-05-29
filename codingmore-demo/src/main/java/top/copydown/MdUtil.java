package top.copydown;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.github.furstenheim.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/28/22
 */
@Slf4j
public class MdUtil {
    public static void convert(HtmlSourceOption htmlSourceOption) throws IOException {
        OptionsBuilder optionsBuilder = OptionsBuilder.anOptions();
        Options options = optionsBuilder
                .withEmDelimiter("*")
                .withCodeBlockStyle(CodeBlockStyle.FENCED)
                .build();

        CopyDown copyDown = new CopyDown(options);

        // 根据 URL 获取 jsoup 文档对象
        Document doc = Jsoup.connect(htmlSourceOption.getUrl()).get();

        HtmlSourceResult result = null;
        String markdown = null;
        switch (htmlSourceOption.getHtmlSourceType()) {

            case WEIXIN:
                // 微信链接时获取封面图、标题、昵称（订阅号名字）
                result = MdUtil.findWeixin(doc,htmlSourceOption);
                // HTML 转 MD
                markdown = copyDown.convert(result.getMdInput());
                log.info("markdown\n{}", markdown);
                result.setMd(markdown);

                break;
            case JUEJIN:
                result = MdUtil.findJuejin(doc,htmlSourceOption);
                List<String> splits = StrSplitter.split(result.getMdInput(), "\\n", 0, false, false);

                StrBuilder builder = StrBuilder.create();
                for (String str : splits) {
                    builder.append(str);
                    builder.append("\n");
                }

                result.setMd(UnicodeUtil.toString(builder.toString()));
                break;
            case ZHIHU:
                result = MdUtil.findZhihu(doc,htmlSourceOption);
                // HTML 转 MD
                markdown = copyDown.convert(result.getMdInput());
                log.info("zhihu markdown\n{}", markdown);
                result.setMd(markdown);
                break;
            default:
                break;
        }

        // 将标题转换为拼音
        String pinyin = Pinyin4jUtil.getFirstSpellPinYin(result.getMdTitle(), true);
        log.info("pinyin{}", pinyin);

        // 下载封面图
        if (StringUtils.isNotBlank(result.getCoverImageUrl())) {
            long size = HttpUtil.downloadFile(result.getCoverImageUrl(),
                    FileUtil.file(htmlSourceOption.getImgdest() + pinyin + ".jpg"));
            log.info("cover image size{}", size);
        }

        // 准备吸入到 MD 文档
        FileWriter writer = new FileWriter(htmlSourceOption.getMddest()+ pinyin + ".md");

        // 标题写入到文件中
        writer.append("---\n");

        writer.append("title: " + result.getMdTitle() + "\n");
        writer.append("shortTitle: " + result.getMdTitle() + "\n");
        if (StringUtils.isNotBlank(result.getSourceLink())) {
            writer.append("description: 转载链接：" + result.getSourceLink() + "\n");
        }
        writer.append("author: " + result.getAuthor() + "\n");
        writer.append("category:\n");
        writer.append("  - 优质文章\n");
        writer.append("---\n\n");
        writer.append(result.getMd());
        log.info("all done");
    }

    private static HtmlSourceResult findZhihu(Document doc, HtmlSourceOption option) {
        HtmlSourceResult result = new HtmlSourceResult();
        // 标题
        Elements title = doc.select(option.getTitleKey());
        result.setMdTitle(title.text());

        // 作者名
        Elements authorName = doc.select(option.getAuthorKey());
        result.setAuthor(authorName.text());

        // 转载链接
        result.setSourceLink(option.getUrl());

        // 获取文章内容
        Elements content = doc.select(option.getContentSelector());
        String input = content.html();
        result.setMdInput(input);

        return result;
    }

    /**
     * 找到微信文章的作者、封面图、标题、订阅号名字
     * @param doc
     * @param option
     * @return
     */
    public static HtmlSourceResult findWeixin(Document doc, HtmlSourceOption option) {
        // 先找作者名，如果找到，不用找订阅号名了
        String author = findWeixinAuthor(doc, option.getAuthorKey());
        HtmlSourceResult result = findWeixinImgAndTitleAndNickname(doc,option,author);
        // 获取文章内容
        Elements content = doc.select(option.getContentSelector());
        String input = content.html();
        result.setMdInput(input);

        return result;
    }

    /**
     * 查找掘金文章标题、作者、封面图
     *
     * @param doc
     * @param option
     * @return
     */
    public static HtmlSourceResult findJuejin(Document doc, HtmlSourceOption option) {
        HtmlSourceResult result = new HtmlSourceResult();
        // 标题
        Elements title = doc.select(option.getTitleKey());
        result.setMdTitle(title.attr("content"));

        // 作者名
        Elements authorName = doc.select(option.getAuthorKey());
        result.setAuthor(authorName.attr("content"));

        // 转载链接
        result.setSourceLink(option.getUrl());

        // 文章内容
        // 掘金的不是以 HTML 格式显示的，所以需要额外的处理
        // mark_content:"
        // ,is_english:d,is_original:g,user_index:13.31714372615673,original_type:d,original_author:e,content:e,ctime:"1650429118",mtime:"1650858329",rtime:"1650435284",draft_id:"7088517368665604127",view_count:36440,collect_count:346,digg_count:340,comment_count:239,hot_index:2401,is_hot:d,rank_index:.3438144,status:g,verify_status:g,audit_status:k,mark_content:"---\ntheme: awesome-green\n---\n
        // ",display_count:d}
        // div.global-component-box
        for (Element scripts : doc.getElementsByTag("script")) {
            for (DataNode dataNode : scripts.dataNodes()) {
                if (dataNode.getWholeData().contains(option.getContentSelector())) {
                    log.info("juejin contains");
                    // 内容
                    Pattern mdPattern = Pattern.compile(option.getContentSelector()+"\"(.*)\"\\,display_count\\:d\\}\\,author_user_info\\:");
                    Matcher matcher = mdPattern.matcher(dataNode.getWholeData());
                    if (matcher.find()) {
                        String md = matcher.group(1);
                        log.info("find md text success{}", md);
                        result.setMdInput(md);
                        return result;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 查找微信文档作者
     *
     * @param doc
     * @param authorKey
     * @return
     */
    private static String findWeixinAuthor(Document doc, String authorKey) {
        for (Element metaTag : doc.getElementsByTag("meta")) {
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");
            if (authorKey.equals(name)) {
                return content;
            }
        }

        return null;
    }

    /**
     * 查找微信网页的封面图、标题、订阅号名
     *
     * @param doc
     * @param option
     * @return
     */
    private static HtmlSourceResult findWeixinImgAndTitleAndNickname(Document doc, HtmlSourceOption option, String author) {
        // get <script>
        for (Element scripts : doc.getElementsByTag("script")) {
            // get data from <script>
            for (DataNode dataNode : scripts.dataNodes()) {
                // find data which contains
                if (dataNode.getWholeData().contains(option.getCoverImageKey())) {
                    log.info("contains");
                    HtmlSourceResult result = new HtmlSourceResult();

                    if (StringUtils.isNotBlank(author)) {
                        result.setAuthor(author);
                    } else {
                        // 昵称
                        Pattern nikeNamePattern = Pattern.compile("var\\s+"+option.getNicknameKey()+"\\s+=\\s+\"(.*)\";");
                        Matcher nikeNameMatcher = nikeNamePattern.matcher(dataNode.getWholeData());
                        if (nikeNameMatcher.find()) {
                            String nickName = nikeNameMatcher.group(1);
                            log.info("find nickName success{}", nickName);
                            result.setNickName(nickName);
                        }
                    }

                    // 文件名
                    Pattern titlePattern = Pattern.compile("var\\s+"+option.getTitleKey()+"\\s+=\\s+'(.*)'\\.html\\(false\\);");
                    Matcher titleMatcher = titlePattern.matcher(dataNode.getWholeData());
                    if (titleMatcher.find()) {
                        String title = titleMatcher.group(1);
                        log.info("find title success{}", title);
                        result.setMdTitle(title);
                    }

                    // 封面图
                    Pattern pattern = Pattern.compile("var\\s+"+option.getCoverImageKey()+"\\s+=\\s+\"(.*)\";");
                    Matcher matcher = pattern.matcher(dataNode.getWholeData());
                    if (matcher.find()) {
                        String msg_cdn_url = matcher.group(1);
                        log.info("find msg_cdn_url success{}", msg_cdn_url);
                        result.setCoverImageUrl(msg_cdn_url);
                    }
                    return result;
                }
            }
        }
        return null;
    }


}
