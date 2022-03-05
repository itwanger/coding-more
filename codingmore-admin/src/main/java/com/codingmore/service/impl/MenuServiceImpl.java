package com.codingmore.service.impl;

import com.codingmore.dto.MenuNode;
import com.codingmore.model.Menu;
import com.codingmore.mapper.MenuMapper;
import com.codingmore.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<MenuNode> treeList() {
        List<Menu> menuList = this.list();
        List<MenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return result;
    }

    /**
     * 将Menu转化为MenuNode并设置children属性
     */
    private MenuNode covertMenuNode(Menu menu, List<Menu> menuList) {
        MenuNode node = new MenuNode();
        BeanUtils.copyProperties(menu, node);
        List<MenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getMenuId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

}
