package com.codingmore.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.model.Site;
import com.codingmore.model.TermTaxonomy;
import com.codingmore.service.ILearnWebRequestStrategy;
import com.codingmore.service.IPostsService;
import com.codingmore.service.ISiteService;
import com.codingmore.service.ITermTaxonomyService;
import com.codingmore.util.WebRequestParam;
import com.codingmore.vo.IndexTermTaxonomyPostVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页请求处理策略
 */
@Service("indexPageRequestStrategy")
public class IndexPageRequestStrategy implements ILearnWebRequestStrategy {
    private static final String INDEX_PAGE = "index.html";
    //站点栏目内容信息
    private static final String INDEX_TERM_TAXONOMY_POST_VO = "indexTermTaxonomyPostVo";
    //站点信息
    private static final String SITE_CONFIG = "siteConfig";
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
            webRequestParam.getRequest().setAttribute(SITE_CONFIG, siteList.get(0));
        }
        List<IndexTermTaxonomyPostVo>  indexTermTaxonomyPostVos = new ArrayList<>();
        List<TermTaxonomy> termTaxonomyList = termTaxonomyService.list();
        termTaxonomyList.forEach(termTaxonomy -> {
            IndexTermTaxonomyPostVo  indexTermTaxonomyPostVo = new IndexTermTaxonomyPostVo();
            indexTermTaxonomyPostVo.setTermTaxonomy(termTaxonomy);
           
            indexTermTaxonomyPostVo.setPosts(postsService.listByTermTaxonomyId(termTaxonomy.getTermTaxonomyId()));
        });
        webRequestParam.getRequest().setAttribute(INDEX_TERM_TAXONOMY_POST_VO, indexTermTaxonomyPostVos);
        return INDEX_PAGE;
    }
}
