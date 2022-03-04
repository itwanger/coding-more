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
     * 创建后台菜单
     */
    int create(Menu menu);

    /**
     * 修改后台菜单
     */
    int update(Long id, Menu menu);

    /**
     * 根据ID获取菜单详情
     */
    Menu getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     */
    List<Menu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<MenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    int updateHidden(Long id, Integer hidden);
}
