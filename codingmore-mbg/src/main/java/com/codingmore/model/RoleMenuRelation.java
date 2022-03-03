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
 * 后台角色菜单关系表
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RoleMenuRelation对象", description="后台角色菜单关系表")
public class RoleMenuRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_menu_relation_id", type = IdType.AUTO)
    private Long roleMenuRelationId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;


}
