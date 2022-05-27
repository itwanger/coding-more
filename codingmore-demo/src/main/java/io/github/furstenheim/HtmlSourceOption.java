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
public class HtmlSourceOption {
    // 地址
    private String url;
    // 内容选择器
    private String contentSelector;
    // 封面图 key
    private String coverImageKey;
    // 标题 key
    private String titleKey;
    // 保存 md 路径
    private String mddest;
    // 保存 image 路径
    private String imgdest;
    // 作者名
    private String authorKey;
    // 昵称
    private String nicknameKey;
}
