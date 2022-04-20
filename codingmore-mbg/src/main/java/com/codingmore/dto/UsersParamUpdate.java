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
@ApiModel(value="Users对象", description="用户表")
public class UsersParamUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long usersId;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message="昵称不能为空")
    private String userNicename;

    @ApiModelProperty(value = "Email")
    private String userEmail;



    @ApiModelProperty(value = "网址")
    private String userUrl;

    @ApiModelProperty(value = "图像")
    private String displayName;

}
