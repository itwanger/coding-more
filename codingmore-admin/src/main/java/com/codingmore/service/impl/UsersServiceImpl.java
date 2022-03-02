package com.codingmore.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.model.AdminUserDetails;
import com.codingmore.exception.Asserts;
import com.codingmore.model.AdminResource;
import com.codingmore.model.Users;
import com.codingmore.mapper.UsersMapper;
import com.codingmore.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.state.UserStatus;
import com.codingmore.state.UserType;
import com.codingmore.util.JwtTokenUtil;
import com.codingmore.dto.UpdateAdminPasswordParam;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private static Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
//        adminCacheService.delAdmin(umsAdmin.getId());
        return 1;
    }

    /**
     * 获取用户对应的资源
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 根据用户名查询用户
        Users admin = getAdminByUsername(username);
        // TODO 查询用户资源
        List<AdminResource> resourceList = new ArrayList<>();
        // 自定义用户详情+资源
        return new AdminUserDetails(admin, resourceList);
    }

  /*  @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = adminCacheService.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return  resourceList;
        }
        resourceList = adminRoleRelationDao.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            adminCacheService.setResourceList(adminId,resourceList);
        }
        return resourceList;
    }*/

    @Override
    public Users getCurrentLoginUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        AdminUserDetails adminUserDetails = (AdminUserDetails)auth.getPrincipal();
        return adminUserDetails.getUsers();
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentLoginUser().getId();
    }
}
