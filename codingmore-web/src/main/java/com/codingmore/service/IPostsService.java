package com.codingmore.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.dto.PostsParam;
import com.codingmore.model.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codingmore.vo.PostsVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
public interface IPostsService extends IService<Posts> {



    IPage<PostsVo> findByPage(PostsPageQueryParam postsPageQueryParam);

    PostsVo getPostsById(Long id);
}
