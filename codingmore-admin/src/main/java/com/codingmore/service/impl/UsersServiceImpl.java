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

    @Override
    public Users getAdminByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_login", username);
        List<Users> usersList = baseMapper.selectList(queryWrapper);

        if (usersList != null && usersList.size() > 0) {
            return usersList.get(0);
        } else {
            return null;
        }

    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                Asserts.fail("密码不正确");
            }
            if (!userDetails.isEnabled()) {
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam updatePasswordParam) {
        if (StringUtils.isEmpty(updatePasswordParam.getUsername())
                || StringUtils.isEmpty(updatePasswordParam.getOldPassword())
                || StringUtils.isEmpty(updatePasswordParam.getNewPassword())) {
            return -1;
        }

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userLogin", updatePasswordParam.getUsername());
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

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        Users admin = getAdminByUsername(username);
        if (admin != null) {
            List<AdminResource> resourceList = new ArrayList<>();
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
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
