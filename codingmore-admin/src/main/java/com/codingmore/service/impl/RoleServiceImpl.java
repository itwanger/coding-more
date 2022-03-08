package com.codingmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.mapper.RoleMenuRelationMapper;
import com.codingmore.mapper.RoleResourceRelationMapper;
import com.codingmore.model.Menu;
import com.codingmore.model.Resource;
import com.codingmore.model.Role;
import com.codingmore.mapper.RoleMapper;
import com.codingmore.model.RoleMenuRelation;
import com.codingmore.model.RoleResourceRelation;
import com.codingmore.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.service.IUsersCacheService;
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
    private IUsersCacheService usersCacheService;
    





    @Override
    public List<Menu> getMenuList(Long userId) {
        return roleMapper.getMenuList(userId);
    }

    @Override
    public List<Menu> listMenu(Long roleId) {
        return roleMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Resource> listResource(Long roleId) {
        return roleMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<RoleMenuRelation> queryWrapper = new QueryWrapper<RoleMenuRelation>();
        queryWrapper.eq("role_id",roleId);
        roleMenuRelationMapper.delete(queryWrapper);

        //批量插入新关系
        for (Long menuId : menuIds) {
            RoleMenuRelation relation = new RoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            roleMenuRelationMapper.insert(relation);
        }

        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
           //先删除原有关系
           QueryWrapper<RoleResourceRelation> queryWrapper = new QueryWrapper<RoleResourceRelation>();
           queryWrapper.eq("role_id",roleId);
           roleResourceRelationMapper.delete(queryWrapper);
           //批量插入新关系
           for (Long resourceId : resourceIds) {
               RoleResourceRelation relation = new RoleResourceRelation();
               relation.setRoleId(roleId);
               relation.setResourceId(resourceId);
               roleResourceRelationMapper.insert(relation);
           }
           usersCacheService.delResourceListByRole(roleId);
           return resourceIds.size();
    }

   
}
