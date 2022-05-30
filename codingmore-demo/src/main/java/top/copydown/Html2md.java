package top.copydown;

public class Html2md {
    public static final String urls [] = {
            "https://juejin.cn/post/7054726274362638350",
            "https://mp.weixin.qq.com/s?__biz=MzA3MzA5MTU4NA==&mid=2247503136&idx=1&sn=82bfe2d145eb7e6883faa065aa1f8ccc&chksm=9f16d24fa8615b59e4e4f158d3b9d6ae39f84832b88ddccf3afa63f10002a9fcb0b44e62a32e#rd",
            "https://www.zhihu.com/question/266199665/answer/306058540",
            "https://www.cnblogs.com/xiaoyangjia/p/11535486.html",
    };

    public static void main(String[] args) {
        String url = urls[3];

        HtmlSourceOption option = HtmlSourceOption.builder()
                .imgdest(Constants.destination + "images/" + Constants.category)
                .mddest(Constants.destination + "docs/" + Constants.category)
                .url(url)
                .build();

        if (url.indexOf("juejin.cn") != -1) {
            // 掘金的链接
            option.setHtmlSourceType(HtmlSourceType.JUEJIN);
            option.setContentSelector("mark_content:");
            option.setTitleKey("meta[itemprop='headline']");
            option.setAuthorKey("div[itemprop='author'] meta[itemprop='name']");
        } else if (url.indexOf("mp.weixin.qq.com") != -1) {

            // 微信的链接
            option.setHtmlSourceType(HtmlSourceType.WEIXIN);
            option.setContentSelector("div.rich_media_content");
            option.setCoverImageKey("msg_cdn_url");
            option.setTitleKey("msg_title");
            option.setNicknameKey("nickname");
            option.setAuthorKey("author");
        } else if (url.indexOf("zhihu.com") != -1) {
            // 知乎的链接
            option.setHtmlSourceType(HtmlSourceType.ZHIHU);
            option.setContentSelector("div.QuestionAnswer-content .RichContent-inner");
            option.setTitleKey("h1.QuestionHeader-title");
            option.setAuthorKey(".AuthorInfo-content .AuthorInfo-head .AuthorInfo-name a");
        } else if (url.indexOf("cnblogs.com") != -1) {
            // 博客园的链接
            option.setHtmlSourceType(HtmlSourceType.BOKEYUAN);
            option.setContentSelector("div#mainContent div#post_detail .blogpost-body.cnblogs-markdown");
            option.setTitleKey("head title");
            // 文章标题里有作者名
        }

        MdUtil.convert(option);
    }
}
