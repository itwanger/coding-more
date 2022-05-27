package io.github.furstenheim;

import lombok.Builder;
import lombok.Data;

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
}
