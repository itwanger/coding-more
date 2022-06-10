package com.codingmore.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.mapper.AdminRoleRelationMapper;
import com.codingmore.model.*;
import com.codingmore.exception.Asserts;
import com.codingmore.mapper.UsersMapper;
import com.codingmore.service.IUsersCacheService;
import com.codingmore.service.IUsersService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.state.UserStatus;
import com.codingmore.state.UserType;
import com.codingmore.util.JwtTokenUtil;
import com.codingmore.dto.UpdateAdminPasswordParam;
import com.codingmore.dto.UsersPageQueryParam;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private static Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IUsersCacheService usersCacheService;
    @Autowired
    private AdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 实际查询数据库
     *
     * @param username
     * @return
     */
    @Override
    public Users getAdminByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_login", username);
        List<Users> usersList = baseMapper.selectList(queryWrapper);

        if (usersList != null && usersList.size() > 0) {
            return usersList.get(0);
        }

        // 用户名错误，提前抛出异常
        throw new UsernameNotFoundException("用户名错误");
    }

    @Override
    public boolean register(Users users) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_login", users.getUserLogin());
        int count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return false;
        }
        users.setUserRegistered(new Date());
        users.setUserType(UserType.BACKEND.getUserType());
        users.setUserStatus(UserStatus.ENABLE.getStatus());
        String encodePassword = passwordEncoder.encode(users.getUserPass());
        users.setUserPass(encodePassword);

        return save(users);
    }

    /**
     * 根据用户名从数据库查询用户，附带资源后，验证密码和账号是否禁用，
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            // 查询用户+用户资源
            UserDetails userDetails = loadUserByUsername(username);

            // 验证密码
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }

            // 返回 JWT
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    public void setAuthentication(UserDetails userDetails) {
        // 当前登录用户+用户权限
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam updatePasswordParam) {
        AdminUserDetails adminUserDetails = (AdminUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = adminUserDetails.getUsers();

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_login", users.getUserLogin());
        List<Users> usersList = baseMapper.selectList(queryWrapper);

        if (CollUtil.isEmpty(usersList)) {
            return -2;
        }
        Users user = usersList.get(0);
        if (!passwordEncoder.matches(updatePasswordParam.getOldPassword(), user.getUserPass())) {
            return -3;
        }
        user.setUserPass(passwordEncoder.encode(updatePasswordParam.getNewPassword()));
        baseMapper.updateById(user);
        usersCacheService.delAdminUserByUserId(user.getUsersId());
        return 1;
    }

    /**
     * 获取用户对应的资源
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 根据用户名查询用户
        Users admin = getAdminByUsername(username);
        if (admin != null) {
            List<Resource> resourceList = getResourceList(admin.getUsersId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public List<Resource> getResourceList(Long adminId) {
        log.info("根据用户{}找到资源", adminId);
        List<Resource> resourceList = usersCacheService.getResourceListByUserId(adminId);

        log.info("Redis 用户关联的角色{}", resourceList);
        if(CollUtil.isNotEmpty(resourceList)){
            return  resourceList;
        }

        resourceList = adminRoleRelationMapper.getResourceList(adminId);
        log.info("根据用户获取数据库中的资源大小{}, 内容{}", resourceList.size(), resourceList);
        if(CollUtil.isNotEmpty(resourceList)){
            usersCacheService.setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public List<Role> getRoleList(Long adminId) {
        return adminRoleRelationMapper.getRoleList(adminId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系

        QueryWrapper< AdminRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("users_id",adminId);
        adminRoleRelationMapper.delete(queryWrapper);

        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<AdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                AdminRoleRelation roleRelation = new AdminRoleRelation();
                roleRelation.setUsersId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationMapper.insertList(list);
        }
        usersCacheService.delResourceListByUserId(adminId);
        return count;
    }

    @Override
    public Users getCurrentLoginUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        AdminUserDetails adminUserDetails = (AdminUserDetails)auth.getPrincipal();
        return adminUserDetails.getUsers();
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentLoginUser().getUsersId();
    }

    @Override
    public IPage<Users> findByPage(UsersPageQueryParam param) {
        QueryWrapper<UsersPageQueryParam> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(param.getUserLogin())){
            queryWrapper.like("user_login",param.getUserLogin());
        }
        if(StringUtils.isNotEmpty(param.getUserNicename())){
            queryWrapper.like("user_nicename",param.getUserNicename());
        }
        if(param.getRoleId()!=null){
            queryWrapper.like("b.role_id",param.getRoleId());
        }
        Page<Users> usersPage = new Page<>(param.getPage(), param.getPageSize());
        IPage<Users> usersIPage = usersMapper.findByPage(usersPage,queryWrapper);
        return usersIPage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeUser(Long usersId) {
        QueryWrapper<AdminRoleRelation> query = new QueryWrapper<AdminRoleRelation>();
        query.eq("users_id",usersId);
        adminRoleRelationMapper.delete(query);
        return this.removeById(usersId);
    }
}
