package io.github.furstenheim;

import lombok.Getter;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/27/22
 */
@Getter
public enum HtmlSourceSelector {
    // 微信
    WEIXIN("div.rich_media_content"),
    // 墨滴
    MDNICE( "div.writing-content"),
    // 掘金
    JUEJIN(""),
    // 知乎
    ZHIHU("");

    private String selector;

    HtmlSourceSelector(String selector) {
        this.selector = selector;
    }
}
