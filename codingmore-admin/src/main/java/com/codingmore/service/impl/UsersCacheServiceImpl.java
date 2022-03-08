package com.codingmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.mapper.AdminRoleRelationMapper;
import com.codingmore.model.AdminRoleRelation;
import com.codingmore.model.Resource;
import com.codingmore.model.Users;
import com.codingmore.service.IUsersService;
import com.codingmore.service.RedisService;
import com.codingmore.service.UsersCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersCacheServiceImpl implements UsersCacheService {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;


    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long usersId) {
        Users admin = usersService.getById(usersId);
        if (admin != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUserLogin();
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long usersId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + usersId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        QueryWrapper<AdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<AdminRoleRelation> relations = adminRoleRelationMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(relations)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relations.stream().map(relation -> keyPrefix + relation.getUsersId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        QueryWrapper<AdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        List<AdminRoleRelation> relations = adminRoleRelationMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(relations)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relations.stream().map(relation -> keyPrefix + relation.getUsersId()).collect(Collectors.toList());
            redisService.del(keys);
        }

    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminRoleRelationMapper.getAdminIdList(resourceId);
        if (CollUtil.isNotEmpty(adminIdList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public Users getUsers(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (Users) redisService.get(key);
    }

    @Override
    public void setUsers(Users users) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + users.getUserLogin();
        redisService.set(key, users, REDIS_EXPIRE);
    }

    @Override
    public List<Resource> getResourceList(Long usersId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + usersId;
        if (redisService.get(key) != null) {
            return (List<Resource>) redisService.get(key);
        }
        return null;

    }

    @Override
    public void setResourceList(Long adminId, List<Resource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
