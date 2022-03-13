package com.codingmore.vo;

import java.util.List;

import com.codingmore.model.Posts;
import com.codingmore.model.Site;
import com.codingmore.model.TermTaxonomy;

import lombok.Data;

/**
 * 首页栏目文章列表
 */
@Data
public class IndexTermTaxonomyPostVo {
    private TermTaxonomy termTaxonomy;
    private List<Posts> posts;
}
