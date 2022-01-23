package com.codingmore.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 链接信息表
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Links对象", description="链接信息表")
public class Links implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "link_id", type = IdType.AUTO)
    private Long linkId;

    @ApiModelProperty(value = "链接URL")
    private String linkUrl;

    @ApiModelProperty(value = "链接标题")
    private String linkName;

    @ApiModelProperty(value = "链接图片")
    private String linkImage;

    @ApiModelProperty(value = "链接打开方式")
    private String linkTarget;

    @ApiModelProperty(value = "链接描述")
    private String linkDescription;

    @ApiModelProperty(value = "是否可见（Y/N）")
    private String linkVisible;

    @ApiModelProperty(value = "添加者用户ID")
    private Long linkOwner;




}
