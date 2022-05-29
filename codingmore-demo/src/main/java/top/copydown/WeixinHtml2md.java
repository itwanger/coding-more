package top.copydown;

import io.github.furstenheim.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/27/22
 */
@Slf4j
public class WeixinHtml2md {
    public static final String urls [] = {
            "https://mp.weixin.qq.com/s?__biz=MzA3MzA5MTU4NA==&mid=2247503136&idx=1&sn=82bfe2d145eb7e6883faa065aa1f8ccc&chksm=9f16d24fa8615b59e4e4f158d3b9d6ae39f84832b88ddccf3afa63f10002a9fcb0b44e62a32e#rd",
            "https://www.mdnice.com/writing/6a250d838af14f72b609387725358d36",
    };



    public static void main(String[] args) throws IOException {
        MdUtil.convert(HtmlSourceOption.builder()
                .imgdest(Constants.destination + "images/" + Constants.category)
                .mddest(Constants.destination + "docs/" + Constants.category)
                .htmlSourceType(HtmlSourceType.WEIXIN)
                .url(urls[0])
                .contentSelector("div.rich_media_content")
                .coverImageKey("msg_cdn_url")
                .titleKey("msg_title")
                .nicknameKey("nickname")
                .authorKey("author").build());
    }
}
