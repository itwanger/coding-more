package com.codingmore.service;

import com.codingmore.dto.MenuNode;
import com.codingmore.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 树形结构返回所有菜单列表
     */
    List<MenuNode> treeList();

}
