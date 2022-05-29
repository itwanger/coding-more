package top.copydown;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/27/22
 */
@Data
@Builder
public class HtmlSourceResult {
    // 封面图路径
    private String coverImageUrl;
    // 标题
    private String mdTitle;
    // nickName
    private String nickName;
    // 作者名
    private String author;
    // 原文链接
    private String sourceLink;
    // MD 内容
    private String mdInput;
    // MD
    private String md;

    @Tolerate
    public HtmlSourceResult() {

    }
}
