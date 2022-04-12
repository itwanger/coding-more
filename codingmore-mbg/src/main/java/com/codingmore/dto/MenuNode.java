package com.codingmore.dto;

import com.codingmore.model.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 后台菜单节点封装
 * Created by 石磊 on 2020/2/4.
 */
@Data
public class MenuNode extends Menu {
    @ApiModelProperty(value = "子级菜单")
    private List<MenuNode> children;
}
