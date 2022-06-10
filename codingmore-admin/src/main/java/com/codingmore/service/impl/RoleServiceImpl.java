package com.codingmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.model.Menu;
import com.codingmore.model.Resource;
import com.codingmore.model.Role;
import com.codingmore.dto.RolePageQueryParam;
import com.codingmore.mapper.RoleMapper;
import com.codingmore.model.RoleMenuRelation;
import com.codingmore.model.RoleResourceRelation;
import com.codingmore.service.IRoleMenuRelationService;
import com.codingmore.service.IRoleResourceRelationService;
import com.codingmore.service.IRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.service.IUsersCacheService;
import com.codingmore.vo.RoleVo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IRoleMenuRelationService roleMenuRelationService;
    @Autowired
    private IRoleResourceRelationService roleResourceRelationService;

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
    @Transactional
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<RoleMenuRelation> queryWrapper = new QueryWrapper<RoleMenuRelation>();
        queryWrapper.eq("role_id",roleId);
        roleMenuRelationService.remove(queryWrapper);

        List<RoleMenuRelation> relationList = new ArrayList<RoleMenuRelation>();
        //批量插入新关系
        for (Long menuId : menuIds) {
            RoleMenuRelation relation = new RoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            relationList.add(relation);
        }
        roleMenuRelationService.saveBatch(relationList);
        usersCacheService.delResourceListByRoleId(roleId);
        return menuIds.size();
    }

    @Override
    @Transactional
    public int allocResource(Long roleId, List<Long> resourceIds) {
        log.info("先删除原有关系，角色{}，资源{}", roleId, resourceIds);
       QueryWrapper<RoleResourceRelation> queryWrapper = new QueryWrapper<RoleResourceRelation>();
       queryWrapper.eq("role_id",roleId);
       roleResourceRelationService.remove(queryWrapper);

       List<RoleResourceRelation> relationList = new ArrayList<>();
       //批量插入新关系
       for (Long resourceId : resourceIds) {
           RoleResourceRelation relation = new RoleResourceRelation();
           relation.setRoleId(roleId);
           relation.setResourceId(resourceId);
           relationList.add(relation);
       }
       log.info("批量插入角色和资源的关系{}", relationList);
       roleResourceRelationService.saveBatch(relationList);

       usersCacheService.delResourceListByRoleId(roleId);
       return resourceIds.size();
    }

    @Override
    public IPage<RoleVo> findByPage(RolePageQueryParam param) {
        QueryWrapper<RolePageQueryParam> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(param.getKeyword())){
            queryWrapper.like("name",param.getKeyword());
        }
        Page<RoleVo> postsPage = new Page<>(param.getPage(), param.getPageSize());
        return roleMapper.findByPage(postsPage,queryWrapper);
    }

    @Override
    public boolean batchRemove(List<Long> roleIds) {
        usersCacheService.delResourceListByRoleIds(roleIds);
        return  this.removeByIds(roleIds);
    }
}
