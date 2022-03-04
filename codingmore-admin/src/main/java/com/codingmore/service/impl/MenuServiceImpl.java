package com.codingmore.service.impl;

import com.codingmore.dto.MenuNode;
import com.codingmore.model.Menu;
import com.codingmore.mapper.MenuMapper;
import com.codingmore.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public int create(Menu menu) {
        return 0;
    }

    @Override
    public int update(Long id, Menu menu) {
        return 0;
    }

    @Override
    public Menu getItem(Long id) {
        return null;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<Menu> list(Long parentId, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public List<MenuNode> treeList() {
        return null;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        return 0;
    }
}
