package com.codingmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.dto.ResourceCategoryParam;
import com.codingmore.model.Resource;
import com.codingmore.model.ResourceCategory;
import com.codingmore.service.IResourceCategoryService;
import com.codingmore.service.IResourceService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Controller
@RequestMapping("/resourceCategory")
@Api(tags = "资源分类")
public class ResourceCategoryController {
    @Autowired
    private IResourceCategoryService iResourceCategoryService;
    @Autowired
    private IResourceService iResourceService;

    @ApiOperation("查询所有后台资源分类")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public ResultObject<List<ResourceCategory>> listAll() {
        List<ResourceCategory> resourceList = iResourceCategoryService.list();
        return ResultObject.success(resourceList);
    }

    @ApiOperation("添加后台资源分类")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> insert(@Valid ResourceCategoryParam resourceCategoryParam) {
        ResourceCategory resourceCategory = new ResourceCategory();
        BeanUtils.copyProperties(resourceCategoryParam, resourceCategory);
        if(resourceCategory.getCreateTime() == null){
            resourceCategory.setCreateTime(new Date());
        }
        return ResultObject.success(iResourceCategoryService.save(resourceCategory) ? "保存成功" : "保存失败");
    }

    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> update(ResourceCategoryParam resourceCategoryParam) {
        if (resourceCategoryParam.getResourceCategoryId() == null) {
            return ResultObject.failed("id不能为空");
        }
        ResourceCategory resourceCategory = iResourceCategoryService
                .getById(resourceCategoryParam.getResourceCategoryId());
        if (resourceCategory == null) {
            return ResultObject.failed("没有找到该资源分类");
        }
        BeanUtils.copyProperties(resourceCategoryParam, resourceCategory);
        return ResultObject.success(iResourceCategoryService.updateById(resourceCategory) ? "修改成功" : "修改失败");
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultObject<String> delete(@RequestParam Long id) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id);
        int count = iResourceService.count(queryWrapper);
        if(count>0){
            return ResultObject.failed("该分类下有资源，不能删除");
        }
        return ResultObject.success(iResourceCategoryService.removeById(id) ? "删除成功" : "删除失败");
    }
}
