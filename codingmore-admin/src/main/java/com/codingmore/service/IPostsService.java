package com.codingmore.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.dto.PostsParam;
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
    /**
     * 保存内容
     * @param postsParam
     * @return
     */
    boolean savePosts(PostsParam postsParam);

    /**
     * 修改内容
     * @param postsParam
     * @return
     */
    boolean updatePosts(PostsParam postsParam);

    boolean removePostsById(Long id);


    IPage<PostsVo> findByPage(PostsPageQueryParam postsPageQueryParam);

    PostsVo getPostsById(Long id);

    int  insertPostTermTaxonomy(Long[] postsIds, Long[] termTaxonomyIds);
}
