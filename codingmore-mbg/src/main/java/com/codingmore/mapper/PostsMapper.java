package com.codingmore.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.model.Posts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codingmore.vo.PostsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
public interface PostsMapper extends BaseMapper<Posts> {

    IPage<PostsVo> findByPage(IPage<PostsVo> page, @Param(Constants.WRAPPER) Wrapper<PostsPageQueryParam> wrapper);
    IPage<PostsVo> findByPageWithTag(IPage<PostsVo> page, @Param(Constants.WRAPPER) Wrapper<PostsPageQueryParam> wrapper);
    List<PostsVo> findByPageWithTagPaged(@Param(Constants.WRAPPER) Wrapper<PostsPageQueryParam> wrapper, Long searchTagId, long pageStart, long pageSize);

}
