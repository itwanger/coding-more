package com.codingmore.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.model.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codingmore.vo.PostsVo;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
public interface IPostsService extends IService<Posts> {

    IPage<PostsVo> findByPageWithTag(PostsPageQueryParam postsPageQueryParam);

    List<PostsVo> findByPageWithTagPaged(PostsPageQueryParam postsPageQueryParam);

    List<Posts> listByTermTaxonomyId(Long termTaxonomyId);

    PostsVo getPostsById(Long id);

    void increasePageView(Long id, HttpServletRequest  request);

    void increaseLikeCount(Long id, HttpServletRequest  request);

    int getPageView(Long id);

    int getLikeCount(Long id);

    Boolean hasClickedLike(Long id, HttpServletRequest request);

}
