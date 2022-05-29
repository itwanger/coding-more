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
public class JuejinHtml2md {
    public static final String urls [] = {
            "https://juejin.cn/post/7054726274362638350",
    };



    public static void main(String[] args) throws IOException {
        MdUtil.convert(HtmlSourceOption.builder()
                .imgdest(Constants.destination + "images/" + Constants.category)
                .mddest(Constants.destination + "docs/" + Constants.category)
                .htmlSourceType(HtmlSourceType.JUEJIN)
                .url(urls[0])
                .contentSelector("mark_content:")
                .titleKey("meta[itemprop='headline']")
                .authorKey("div[itemprop='author'] meta[itemprop='name']").build());
    }
}
