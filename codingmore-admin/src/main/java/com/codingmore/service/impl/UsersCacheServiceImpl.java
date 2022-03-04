package com.codingmore.service.impl;

import com.codingmore.mapper.AdminRoleRelationMapper;
import com.codingmore.model.Resource;
import com.codingmore.model.Users;
import com.codingmore.service.IUsersService;
import com.codingmore.service.RedisService;
import com.codingmore.service.UsersCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void delAdmin(Long adminId) {

    }

    @Override
    public void delResourceList(Long adminId) {

    }

    @Override
    public void delResourceListByRole(Long roleId) {

    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {

    }

    @Override
    public void delResourceListByResource(Long resourceId) {

    }

    @Override
    public Users getUsers(String username) {
        return null;
    }

    @Override
    public void setUsers(Users users) {

    }

    @Override
    public List<Resource> getResourceList(Long usersId) {
        return null;
    }

    @Override
    public void setResourceList(Long adminId, List<Resource> resourceList) {

    }
}
