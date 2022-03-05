package com.codingmore.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.codingmore.service.IResourceCategoryService;
import com.codingmore.webapi.ResultObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Controller
@RequestMapping("/resource")
@Api(tags = "后台资源分类管理")
public class ResourceController {

  
}

