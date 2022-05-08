package com.codingmore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.service.IPostsService;
import com.codingmore.util.DateUtil;
import com.codingmore.vo.PostsVo;
import com.codingmore.webapi.ResultObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Controller
@Api(tags = "学习网站rest接口")
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private IPostsService postsService;

    @RequestMapping(value = "/postPageQuery", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("内容分页查询")
    public ResultObject<Map<String, Object>> queryPageable(PostsPageQueryParam postsPageQueryParam) {
        Map<String, Object> map = new HashMap<>();
        // IPage<PostsVo> postsIPage = postsService.findByPageWithTag(postsPageQueryParam);
        List<PostsVo> postsIPage = postsService.findByPageWithTagPaged(postsPageQueryParam);
        // map.put("items", postsIPage.getRecords());
        map.put("items", postsIPage);
        // map.put("total", postsIPage.getTotal());
        return ResultObject.success(map);
    }

    @RequestMapping(value = "/clickLike", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("点赞方法")
    public ResultObject<Map<String, Object>> giveThumbsUp(HttpServletRequest request, @NotNull @RequestParam Long postsId) {
        Map<String, Object> map = new HashMap<>();
        Boolean hasClicked = postsService.hasClickedLike(postsId, request);
        map.put("hasClicked", hasClicked);
        postsService.increaseLikeCount(postsId, request);
        Integer newestCount = postsService.getLikeCount(postsId);
        map.put("newestCount", newestCount);
        return ResultObject.success(map);
    }

    @RequestMapping(value = "/getLikeCount", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("获取点赞数量的方法")
    public ResultObject<Integer> getLikeCount(@NotNull @RequestParam Long postsId) {
        Integer newestCount = postsService.getLikeCount(postsId);
        return ResultObject.success(newestCount);
    }
}
