package com.codingmore.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.UsersLoginParam;
import com.codingmore.dto.UsersParam;
import com.codingmore.model.Users;
import com.codingmore.service.IUsersService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2021-05-22
 */
@Controller
@Api(tags="用户")
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @RequestMapping(value = "/getById",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据id获取用户")
    public ResultObject<Users> getById(long usersId) {
        return ResultObject.success(usersService.getById(usersId));
    }

   /* @RequestMapping(value = "/update",method=RequestMethod.POST)
    @ResponseBody
    @ApiOperation("更新")
    public ResultObject<String> update(@Valid Users users) {
        if (users.getId() == null) {
            return ResultObject.failed("id不能为空");
        }
        return ResultObject.success(usersService.updateById(users) ? "更新成功" : "更新失败");
    }*/

    @RequestMapping(value = "/delete",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation("删除")
    public ResultObject<String> delete(long usersId) {
        return ResultObject.success(usersService.removeById(usersId) ? "删除成功" : "删除失败");
    }

    @RequestMapping(value = "/queryPageable",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation("分页查询")
    public ResultObject<Map<String,Object>> queryPageable(long pageSize, long page){
        Map<String,Object> map = new HashMap<>();
        Page<Users> usersPage = new Page<>(page,pageSize);
        IPage<Users> usersIPage = usersService.page(usersPage);
        map.put("items",usersIPage.getRecords());
        map.put("total",usersIPage.getTotal());
        return ResultObject.success(map);
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> register(@Validated UsersParam users) {
        Users userDto = new Users();
        BeanUtils.copyProperties(users,userDto);
        return ResultObject.success(usersService.register(userDto) ? "保存成功" : "保存失败");
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject login(@Validated UsersLoginParam users) {
        String token = usersService.login(users.getUserLogin(), users.getUserPass());
        if (token == null) {
            return ResultObject.validateFailed("用户名或密码错误");
        }
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
        if(principal==null){
            return ResultObject.unauthorized(null);
        }
        String username = principal.getName();
        Users users = usersService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", users.getUserLogin());
      /*  data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = usersService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }*/
        return ResultObject.success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject logout() {
        return ResultObject.success(null);
    }
    
}

