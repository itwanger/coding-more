package com.codingmore.controller;

import cn.hutool.core.util.ObjectUtil;
import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.util.WebRequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Api(tags = "学习网站前端入口")
public class LearnWebFacadeController {
    private static Logger LOGGER = LoggerFactory.getLogger(LearnWebFacadeController.class);
    @Resource(name = "channelPageRequestStrategy")
    private ILearnWebRequestStrategy channelPageRequestStrategy;
    @Resource(name = "contentPageRequestStrategy")
    private ILearnWebRequestStrategy contentPageRequestStrategy;

    @Resource(name = "indexPageRequestStrategy")
    private ILearnWebRequestStrategy indexPageRequestStrategy;

    @RequestMapping(value = {"/{siteId:[0-9]+}.html"}, method = RequestMethod.GET)
    @ApiOperation("首页页入口")
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable Long siteId) {
        WebRequestParam webRequestParam = new WebRequestParam.Builder().request(request).response(response).model(model).siteId(siteId).build();
        return indexPageRequestStrategy.handleRequest(webRequestParam);
    }


    @ApiOperation("内容动态页入口")
    @RequestMapping(value = {"/{siteId:[0-9]+}/{channelId:[0-9]+}/{postId:[0-9]+}.html"}, method = RequestMethod.GET)
    public String content(@PathVariable Long siteId, @PathVariable Long channelId,@PathVariable Long postId, HttpServletRequest request,
                          HttpServletResponse response, ModelMap model) {
        WebRequestParam webRequestParam = new WebRequestParam.Builder().request(request).response(response).siteId(siteId).channelId(channelId).postId(postId).model(model).build();
        return contentPageRequestStrategy.handleRequest(webRequestParam);
    }


    @ApiOperation("内容动态分页入口")
    @RequestMapping(value = {"/{siteId:[0-9]+}/{channelId:[0-9]+}/postpage_{page:[0-9]+}.html"}, method = RequestMethod.GET)
    public String contentPage(@PathVariable Long siteId, @PathVariable Long channelId, @PathVariable Integer page,
                              HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        WebRequestParam webRequestParam = new WebRequestParam.Builder().request(request).response(response).siteId(siteId).channelId(channelId).page(page).model(model).page(page).build();
        return contentPageRequestStrategy.handleRequest(webRequestParam);
    }

    /**
     * 栏目动态页入口(外网)
     */
    @ApiOperation("栏目动态页入口")
    @RequestMapping(value = {"/{siteId:[0-9]+}/{channelId:[0-9]+}.html"}, method = RequestMethod.GET)
    public String channel(@PathVariable Long siteId, @PathVariable Long channelId, HttpServletRequest request, HttpServletResponse response,
                          ModelMap model){
        ObjectUtil.clone(request);
        WebRequestParam webRequestParam = new WebRequestParam.Builder().request(request).response(response).siteId(siteId).channelId(channelId).model(model).build();
        return channelPageRequestStrategy.handleRequest(webRequestParam);
    }


    /**
     * 栏目动态分页入口
     */
    @ApiOperation("栏目动态分页入口")
    @RequestMapping(value = {"/{siteId:[0-9]+}/{channelId:[0-9]+}_{page:[0-9]+}.html"}, method = RequestMethod.GET)
    public String channelPage(@PathVariable Long siteId, @PathVariable Long channelId,  @PathVariable Integer page, HttpServletRequest request,
                              HttpServletResponse response, ModelMap model) /*throws GlobalException*/ {
        WebRequestParam webRequestParam = new WebRequestParam.Builder().request(request).response(response).siteId(siteId).channelId(channelId).model(model).page(page).build();
        return channelPageRequestStrategy.handleRequest(webRequestParam);
    }


}
