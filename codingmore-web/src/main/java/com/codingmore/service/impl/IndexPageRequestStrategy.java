package com.codingmore.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.model.Site;
import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.service.IPostsService;
import com.codingmore.service.ISiteService;
import com.codingmore.service.ITermTaxonomyService;
import com.codingmore.state.PostStatus;
import com.codingmore.util.WebRequestParam;

import com.codingmore.vo.PostsVo;
import com.codingmore.vo.SiteVo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页请求处理策略
 */
@Service("indexPageRequestStrategy")
public class IndexPageRequestStrategy implements ILearnWebRequestStrategy {
    private static final String INDEX_PAGE = "index.html";
    /**
     * 站点栏目内容信息
     */
    private static final String INDEX_TERM_TAXONOMY_POST_VO = "indexTermTaxonomyPostVo";
    /**
     * 站点信息
     */
    private static final String SITE_CONFIG = "siteConfig";
    
    /**
     * 文章分页列表
     */
    private static final String POSTS_ITEMS = "postsItems";
    /**
     * 、文章条数（前端列表暂时没用上）
     */
    private static final String POSTS_TOTAL= "postsTotal";
    @Autowired
    private ISiteService siteService;
    @Autowired
    private ITermTaxonomyService termTaxonomyService;
    @Autowired
    private IPostsService postsService;

    @Override
    public String handleRequest(WebRequestParam webRequestParam) {

        List<Site> siteList = siteService.list();
        //处理站点配置
        if(siteList.size() > 0) {
            Site site = siteList.get(0);
            SiteVo siteVo = new SiteVo();
            BeanUtils.copyProperties(site, siteVo);
            webRequestParam.getRequest().setAttribute(SITE_CONFIG, siteVo);
        }

        PostsPageQueryParam pageQueryParam = new PostsPageQueryParam();
        pageQueryParam.setPage(webRequestParam.getPage());
        pageQueryParam.setAsc(webRequestParam.isAsc());
        pageQueryParam.setOrderBy(webRequestParam.getOrderBy());
        pageQueryParam.setPageSize(webRequestParam.getPageSize());
        pageQueryParam.setPostStatus(PostStatus.PUBLISHED.toString());
        pageQueryParam.setTermTaxonomyId(webRequestParam.getChannelId());

        List<PostsVo> pageVoList = postsService.findByPageWithTagPaged(pageQueryParam);
        webRequestParam.getRequest().setAttribute(POSTS_ITEMS, pageVoList);
        return INDEX_PAGE;
    }
}
