package com.codingmore.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.MenuNode;
import com.codingmore.dto.MenuParam;
import com.codingmore.model.Menu;
import com.codingmore.model.RoleMenuRelation;
import com.codingmore.service.IMenuService;
import com.codingmore.service.IRoleMenuRelationService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Controller
@Api(tags = "菜单")
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleMenuRelationService roleMenuRelationService;

    @ApiOperation("添加后台菜单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> create( @Valid MenuParam menuParam) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuParam,menu);
        menu.setCreateTime(new Date());
        if(menu.getParentId() == null){
            menu.setParentId(0L);
        }
        if(menuParam.getHidden()!=0 && menuParam.getHidden()!=1){
            return ResultObject.failed("是否隐藏参数错误");
        }
        if(menuParam.getSort() == null){
            menu.setSort(0);
        }

        updateLevel(menu);
        return ResultObject.success(menuService.save(menu) ? "保存成功" : "保存失败");
    }

    @ApiOperation("修改后台菜单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> update( @Valid MenuParam menuParam) {
        if(menuParam.getMenuId() == null){
            return ResultObject.failed("id不能为空");
        }
        if(menuParam.getParentId() == null){
            menuParam.setParentId(0L);
        }
        if(0!= menuParam.getHidden()&& menuParam.getHidden()!=1){
            return ResultObject.failed("是否隐藏参数错误");
        }
        if(menuParam.getSort() == null){
            menuParam.setSort(0);
        }

        Menu menu = menuService.getById(menuParam.getMenuId());
        BeanUtils.copyProperties(menuParam,menu);
        updateLevel(menu);
        return ResultObject.success(menuService.updateById(menu) ? "更新成功" : "更新失败");
    }
       

    private void updateLevel(Menu menu) {
        if (menu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            menu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            Menu parentMenu =menuService.getById(menu.getParentId());
            if (parentMenu != null) {
                menu.setLevel(parentMenu.getLevel() + 1);
            } else {
                menu.setLevel(0);
            }
        }
    }


    @ApiOperation("根据ID获取菜单详情")
    @RequestMapping( method = RequestMethod.GET)
    @ResponseBody
    public ResultObject<Menu> getItem( Long id) {
        return ResultObject.success(menuService.getById(id));
    }

    @ApiOperation("根据ID删除后台菜单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> delete(Long id) {
        QueryWrapper<RoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id",id);
        int count = roleMenuRelationService.count(queryWrapper);
        if(count > 0){
            return ResultObject.failed("该菜单已被使用，不能删除");
        }
        return ResultObject.success(menuService.removeById(id) ? "删除成功" : "删除失败");
    }

    @ApiOperation("分页查询后台菜单")
    @RequestMapping(value = "/queryPageable", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject<Map<String,Object>> queryPageable(Long parentId,long pageSize, long page) {
        Map<String,Object> map = new HashMap<>();
        Page<Menu> postTagPage = new Page<>(page,pageSize);
        QueryWrapper<Menu> postTagQueryWrapper = new QueryWrapper();
        if(parentId!=null){
            postTagQueryWrapper.eq("parent_id",parentId);
        }

        postTagQueryWrapper.orderBy(true, false, "sort");
        IPage<Menu> postTagIPage = menuService.page(postTagPage,postTagQueryWrapper);
        map.put("items",postTagIPage.getRecords());
        map.put("total",postTagIPage.getTotal());
        return ResultObject.success(map);
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject<List<MenuNode>> treeList() {
        List<MenuNode> list = menuService.treeList();
        return ResultObject.success(list);
    }

    @ApiOperation("修改菜单显示状态")
    @RequestMapping(value = "/updateHidden", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> updateHidden(@RequestParam Long  id, @RequestParam Integer hidden) {
        Menu menu = menuService.getById(id);
        if(menu == null){
            return ResultObject.failed("菜单不存在");
        }
        if(hidden!=0 && hidden!=1){
            return ResultObject.failed("隐藏参数错误");
        }
        menu.setHidden(hidden);
        return ResultObject.success(menuService.updateById(menu) ? "修改菜单显示状态成功" : "修改菜单显示状态失败");

    }

}

