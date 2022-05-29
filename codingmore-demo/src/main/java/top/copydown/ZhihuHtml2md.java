package top.copydown;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/27/22
 */
@Slf4j
public class ZhihuHtml2md {
    public static final String urls [] = {
            "https://www.zhihu.com/question/266199665/answer/306058540",
    };



    public static void main(String[] args) throws IOException {
        MdUtil.convert(HtmlSourceOption.builder()
                .imgdest(Constants.destination + "images/" + Constants.category)
                .mddest(Constants.destination + "docs/" + Constants.category)
                .htmlSourceType(HtmlSourceType.ZHIHU)
                .url(urls[0])
                .contentSelector("div.QuestionAnswer-content .RichContent-inner")
                .titleKey("h1.QuestionHeader-title")
                .authorKey(".AuthorInfo-content .AuthorInfo-head .AuthorInfo-name a").build());
    }
}
