package com.codingmore.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.model.Site;
import com.codingmore.model.TermTaxonomy;
import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.service.IPostsService;
import com.codingmore.service.ISiteService;
import com.codingmore.service.ITermTaxonomyService;
import com.codingmore.state.PostStatus;
import com.codingmore.util.WebRequestParam;
import com.codingmore.vo.IndexTermTaxonomyPostVo;

import com.codingmore.vo.PostsVo;
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
     * 、文章条数
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
        postsService.increasePageView(1L,webRequestParam.getRequest());
        List<Site> siteList = siteService.list();
        //处理站点配置
        if(siteList.size() > 0) {
            webRequestParam.getRequest().setAttribute(SITE_CONFIG, siteList.get(0));
        }
      /*  List<IndexTermTaxonomyPostVo>  indexTermTaxonomyPostVos = new ArrayList<>();
        List<TermTaxonomy> termTaxonomyList = termTaxonomyService.list();
        termTaxonomyList.forEach(termTaxonomy -> {
            IndexTermTaxonomyPostVo  indexTermTaxonomyPostVo = new IndexTermTaxonomyPostVo();
            indexTermTaxonomyPostVo.setTermTaxonomy(termTaxonomy);
           
            indexTermTaxonomyPostVo.setPosts(postsService.listByTermTaxonomyId(termTaxonomy.getTermTaxonomyId()));
        });*/
        PostsPageQueryParam pageQueryParam = new PostsPageQueryParam();
        pageQueryParam.setPage(webRequestParam.getPage());
        pageQueryParam.setAsc(false);
        pageQueryParam.setOrderBy("post_date");
        pageQueryParam.setPageSize(webRequestParam.getPageSize());
        pageQueryParam.setPostStatus(PostStatus.PUBLISHED.toString());
        pageQueryParam.setTermTaxonomyId(webRequestParam.getChannelId());

        IPage<PostsVo> pageVo = postsService.findByPage(pageQueryParam);
        //设置浏览量
        pageVo.getRecords().forEach(postsVo -> {
            postsVo.setPaveView(postsService.getPageView(postsVo.getPostsId()));
        });
        webRequestParam.getRequest().setAttribute(SITE_CONFIG, siteService.list().get(0));
        webRequestParam.getRequest().setAttribute(POSTS_ITEMS,pageVo.getRecords());
        webRequestParam.getRequest().setAttribute(POSTS_TOTAL,pageVo.getTotal());
        return INDEX_PAGE;
    }
}
