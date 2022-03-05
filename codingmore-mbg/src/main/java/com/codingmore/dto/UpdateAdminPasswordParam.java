package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 修改用户名密码参数
 * Created by zhanglei on 2019/10/9.
 */
@Data
@ApiModel(value="用户登录", description="用户表")
public class UpdateAdminPasswordParam {
    
    @NotEmpty(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;
    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}
