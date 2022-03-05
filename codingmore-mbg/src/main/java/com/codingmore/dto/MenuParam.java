package com.codingmore.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 后台菜单表
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="后台菜单表")
public class MenuParam implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long menuId;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    @ApiModelProperty(value = "菜单级数")
    private Integer level;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    @NotBlank(message = "前端名称不能为空")
    private String name;

    @ApiModelProperty(value = "前端图标")
    @NotBlank(message = "前端图标不能为空")
    private String icon;

    @ApiModelProperty(value = "前端隐藏")
    @NotNull(message = "前端隐藏不能为空")
    private Integer hidden;


}
