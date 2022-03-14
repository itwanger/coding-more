package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PostTag对象", description="标签表")
public class PostTagParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "post_tag_id")
    private Long postTagId;

    @ApiModelProperty(value = "标签名称")
    @NotBlank(message = "标签名称不能为空")
    private String description;




}
