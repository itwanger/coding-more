package com.codingmore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.PostTagParam;
import com.codingmore.model.PostTag;
import com.codingmore.service.IPostTagRelationService;
import com.codingmore.service.IPostTagService;
import com.codingmore.service.IRedisService;
import com.codingmore.webapi.ResultObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Controller
@Api(tags = "标签")
@RequestMapping("/postTag")
public class PostTagController {
    @Autowired
    private IPostTagService postTagService;
    @Autowired
    private IPostTagRelationService postTagRelationService;

    @Autowired
    private IRedisService redisService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("添加标签")
    public ResultObject<String> insert(@Valid PostTagParam postATagParam) {
        QueryWrapper<PostTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("description", postATagParam.getDescription());
        int count = postTagService.count(queryWrapper);
        if (count > 0) {
            return ResultObject.failed("标签已存在");
        }
        PostTag postTag = new PostTag();
        BeanUtils.copyProperties(postATagParam, postTag);
        return ResultObject.success(postTagService.save(postTag) ? "添加成功" : "添加失败");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("修改标签")
    public ResultObject<String> update(@Valid PostTagParam postAddTagParam) {
        if (postAddTagParam.getPostTagId() == null) {
            return ResultObject.failed("标签id不能为空");
        }
        PostTag postTag = postTagService.getById(postAddTagParam.getPostTagId());
        if (postTag == null) {
            return ResultObject.failed("标签不存在");
        }
        QueryWrapper<PostTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("description", postAddTagParam.getDescription());
        int count = postTagService.count(queryWrapper);
        if (count > 0) {
            return ResultObject.failed("标签名称已存在");
        }
        BeanUtils.copyProperties(postAddTagParam, postTag);
        return ResultObject.success(postTagService.updateById(postTag) ? "修改成功" : "修改失败");
    }

    @RequestMapping(value = "/getByPostId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据文章内容获取标签")
    public ResultObject<List<PostTag>> getByPostId(long objectId) {
        return ResultObject.success(postTagService.getByPostId(objectId));
    }

    @RequestMapping(value = "/getByName", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("模糊匹配")
    public ResultObject<List<PostTag>> getByName(String keyWord) {
        QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<>();
        postTagQueryWrapper.like("description", keyWord + "%");
        return ResultObject.success(postTagService.list(postTagQueryWrapper));
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("删除")
    public ResultObject<String> delete(Long postTagId) {
        postTagService.removeTag(postTagId);
        return ResultObject.success("删除成功");
    }

    @RequestMapping(value = "/queryPageable", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("分页查询")
    public ResultObject<Map<String, Object>> queryPageable(long pageSize, long page, String tagName) {
        Map<String, Object> map = new HashMap<>();
        Page<PostTag> postTagPage = new Page<>(page, pageSize);
        QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(tagName)) {
            postTagQueryWrapper.like("description", "%" + tagName + "%");
        }

        IPage<PostTag> postTagIPage = postTagService.page(postTagPage, postTagQueryWrapper);
        map.put("items", postTagIPage.getRecords());
        map.put("total", postTagIPage.getTotal());
        return ResultObject.success(map);
    }
}

