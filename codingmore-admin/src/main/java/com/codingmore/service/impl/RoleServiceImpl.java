package com.codingmore.service.impl;

import com.codingmore.mapper.RoleMenuRelationMapper;
import com.codingmore.mapper.RoleResourceRelationMapper;
import com.codingmore.model.Menu;
import com.codingmore.model.Resource;
import com.codingmore.model.Role;
import com.codingmore.mapper.RoleMapper;
import com.codingmore.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.service.UsersCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;
    @Autowired
    private RoleResourceRelationMapper roleResourceRelationMapper;

    @Autowired
    private UsersCacheService usersCacheService;
    




    @Override
    public List<Role> queryList(String keyword, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public List<Menu> getMenuList(Long userId) {
        return null;
    }

    @Override
    public List<Menu> listMenu(Long roleId) {
        return null;
    }

    @Override
    public List<Resource> listResource(Long roleId) {
        return null;
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        return 0;
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        return 0;
    }

    @Override
    public Role getByName(String roleName) {
        return null;
    }
}
