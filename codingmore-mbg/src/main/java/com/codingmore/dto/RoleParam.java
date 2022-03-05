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
 * 后台用户角色表
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Role对象", description="后台用户角色表")
public class RoleParam implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long roleId;

    @ApiModelProperty(value = "名称")
    @NotBlank(message="名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    @NotNull(message="启用状态不能为空")
    private Integer status;


}
