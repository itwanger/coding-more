package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台资源表
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Resource对象", description="后台资源表")
public class ResourceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long resourceId;

    @ApiModelProperty(value = "资源分类ID")
    @NotNull(message="资源分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "资源名称")
    @NotBlank(message = "资源名称不能为空")
    private String name;

    @ApiModelProperty(value = "资源URL")
    @NotBlank(message = "资源URL不能为空")
    private String url;

    @ApiModelProperty(value = "描述")
    private String description;


}
