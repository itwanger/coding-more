package com.codingmore.controller;


import cn.hutool.core.collection.CollUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.UpdateAdminPasswordParam;
import com.codingmore.dto.UsersLoginParam;
import com.codingmore.dto.UsersPageQueryParam;
import com.codingmore.dto.UsersParam;
import com.codingmore.dto.UsersParamUpdate;
import com.codingmore.model.AdminUserDetails;
import com.codingmore.model.Role;
import com.codingmore.model.Users;
import com.codingmore.service.IRoleService;
import com.codingmore.service.IUsersService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2021-05-22
 */
@Controller
@Api(tags = "用户")
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据id获取用户")
    public ResultObject<Users> getById(long usersId) {
        Users users = usersService.getById(usersId);
        //不返回密码
        users.setUserPass(null);
        return ResultObject.success(users);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("更新")
    public ResultObject<String> update(@Validated UsersParamUpdate usersParam) {
        if (usersParam.getUsersId() == null) {
            return ResultObject.failed("id不能为空");
        }
        Users users = new Users();
        BeanUtils.copyProperties(usersParam, users);
        return ResultObject.success(usersService.updateById(users) ? "更新成功" : "更新失败");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("删除")
    public ResultObject<String> delete(long usersId) {
        return ResultObject.success(usersService.removeUser(usersId) ? "删除成功" : "删除失败");
    }

    @RequestMapping(value = "/queryPageable", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("分页查询")
    public ResultObject<Map<String, Object>> queryPageable(UsersPageQueryParam usersPageQuery) {
        Map<String, Object> map = new HashMap<>();
        IPage<Users> usersIPage = usersService.findByPage(usersPageQuery);
        map.put("items", usersIPage.getRecords());
        map.put("total", usersIPage.getTotal());
        return ResultObject.success(map);
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> register(@Validated UsersParam users) {
        Users userDto = new Users();
        userDto.setUserRegistered(new Date());
        BeanUtils.copyProperties(users, userDto);
        return ResultObject.success(usersService.register(userDto) ? "保存成功" : "保存失败");
    }

    @ApiOperation(value = "启用/禁用 0 启用 1 禁用")
    @RequestMapping(value = "/enableOrDisable", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> enableOrDisable(Long usersId,int status) {
        if(usersId==null){
            return ResultObject.failed("id不能为空");
        }
        if(status!=0&&status!=1){
            return ResultObject.failed("status不能为空");
        }
        Users users = usersService.getById(usersId);
        users.setUserStatus(status);
        return ResultObject.success(usersService.updateById(users) ? "保存成功" : "保存失败");
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject login(@Validated UsersLoginParam users, BindingResult result) {
        String token = usersService.login(users.getUserLogin(), users.getUserPass());

        if (token == null) {
            return ResultObject.validateFailed("用户名或密码错误或状态被禁用");
        }

        // 将 JWT 传递回客户端
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ResultObject.success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = usersService.refreshToken(token);
        if (refreshToken == null) {
            return ResultObject.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return ResultObject.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject getAdminInfo(Principal principal) {
        if (principal == null) {
            return ResultObject.unauthorized(null);
        }
        AdminUserDetails adminUserDetails = (AdminUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        Users user = adminUserDetails.getUsers();
        // ？
        user.setUserPass(null);

        Map<String, Object> data = new HashMap<>();

        data.put("userDetail", adminUserDetails.getUsers());
        data.put("username", user.getUserLogin());
        data.put("menus", roleService.getMenuList(user.getUsersId()));
        data.put("icon", user.getDisplayName());

        List<Role> roleList = usersService.getRoleList(user.getUsersId());
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(Role::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return ResultObject.success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject logout() {
        return ResultObject.success(null);
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject updatePassword(UpdateAdminPasswordParam updatePasswordParam) {
        int status = usersService.updatePassword(updatePasswordParam);
        if (status > 0) {
            return ResultObject.success(status);
        } else if (status == -1) {
            return ResultObject.failed("提交参数不合法");
        } else if (status == -2) {
            return ResultObject.failed("找不到该用户");
        } else if (status == -3) {
            return ResultObject.failed("旧密码错误");
        } else {
            return ResultObject.failed();
        }
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject<List<Role>> getRoleList(@RequestParam Long userId) {
        List<Role> roleList = usersService.getRoleList(userId);
        return ResultObject.success(roleList);
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> updateRole(@RequestParam("userId") Long userId,
                                           @RequestParam("roleIds") List<Long> roleIds) {
        int count = usersService.updateRole(userId, roleIds);
        return ResultObject.success(count > 0 ? "更新成功" : "更新失败");
    }

}

