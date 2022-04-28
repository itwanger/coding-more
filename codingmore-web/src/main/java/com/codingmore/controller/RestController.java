package com.codingmore.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
        IPage<PostsVo> postsIPage = postsService.findByPageWithTag(postsPageQueryParam);
        map.put("items", postsIPage.getRecords());
        map.put("total", postsIPage.getTotal());
        return ResultObject.success(map);
    }
}
