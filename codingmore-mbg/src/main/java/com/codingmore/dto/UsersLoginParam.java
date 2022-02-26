package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@ApiModel(value="用户登录", description="用户表")
public class UsersLoginParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录名")
    @NotBlank(message="登录名不能为空")
    private String userLogin;

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    private String userPass;

}
