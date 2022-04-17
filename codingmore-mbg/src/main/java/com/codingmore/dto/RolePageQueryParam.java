package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Role对象", description="角色")
public class RolePageQueryParam {
    long pageSize;
    long page;
    @ApiModelProperty(value = "关键字")
    String keyword;
}
