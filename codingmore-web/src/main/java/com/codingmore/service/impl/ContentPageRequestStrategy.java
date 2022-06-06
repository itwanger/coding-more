package com.codingmore.service.impl;

import java.util.List;

import com.codingmore.model.Site;
import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.service.IPostsService;
import com.codingmore.service.ISiteService;
import com.codingmore.util.WebRequestParam;
import com.codingmore.vo.PostsVo;
import com.codingmore.vo.SiteVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 内容请求处理策略
 */
@Service("contentPageRequestStrategy")
public class ContentPageRequestStrategy implements ILearnWebRequestStrategy {
    private static final String CONTENT_PAGE = "article.html";
    private static final String POSTS_VO = "postsVo";
    private static final String CLICKED_LIKE = "hasClickedLike";
     /**
     * 站点信息
     */
    private static final String SITE_CONFIG = "siteConfig";

    private static final String TAG_LIST = "tagList";
    @Autowired
    private ISiteService siteService;
    @Autowired
    private IPostsService iPostsService;

    @Override
    public String handleRequest(WebRequestParam webRequestParam) {
        iPostsService.increasePageView(webRequestParam.getPostId(),webRequestParam.getRequest());

        Boolean hasClickedLike = iPostsService.hasClickedLike(webRequestParam.getPostId(),webRequestParam.getRequest());
        webRequestParam.getRequest().setAttribute(CLICKED_LIKE, hasClickedLike);
        PostsVo postsVo = iPostsService.getPostsById(webRequestParam.getPostId());
        webRequestParam.getRequest().setAttribute(POSTS_VO, postsVo);
        if(StringUtils.isNotBlank(postsVo.getTagsName())){
            webRequestParam.getRequest().setAttribute(TAG_LIST, CollectionUtils.arrayToList(postsVo.getTagsName().split(",")));
        }
        List<Site> siteList = siteService.list();
        //处理站点配置
        if(siteList.size() > 0) {
            Site site = siteList.get(0);
            SiteVo siteVo = new SiteVo();
            BeanUtils.copyProperties(site, siteVo);
            webRequestParam.getRequest().setAttribute(SITE_CONFIG, siteVo);
        }
        return CONTENT_PAGE;
    }
}
