package com.codingmore.controller;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author 石磊
 * @since 2021-05-22
 */
@Controller
@RequestMapping("/comments")
@Api(tags="评论")
public class CommentsController {
    private static Logger LOGGER = LoggerFactory.getLogger(CommentsController.class);
}

