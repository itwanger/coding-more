package com.codingmore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codingmore.dto.PostAddTagParam;
import com.codingmore.model.PostTag;
import com.codingmore.mapper.PostTagMapper;
import com.codingmore.model.PostTagRelation;
import com.codingmore.service.IPostTagRelationService;
import com.codingmore.service.IPostTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
@Transactional
@Slf4j
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements IPostTagService {
    @Autowired
    private IPostTagRelationService postTagRelationService;

    @Override
    public boolean savePostTag(PostAddTagParam postAddTagParam) {
        log.info("保存文章的标签{}", postAddTagParam);

        PostTag postTag = new PostTag();
        BeanUtils.copyProperties(postAddTagParam,postTag);

        boolean result = save(postTag);

        if(postAddTagParam.getPostId()!=null){
            PostTagRelation postTagRelation = new PostTagRelation();
            postTagRelation.setPostTagId(postTag.getPostTagId());
            postTagRelation.setPostId(postAddTagParam.getPostId());
            postTagRelation.setTermOrder(postAddTagParam.getTermOrder());
            postTagRelationService.save(postTagRelation);
        }
        return result;
    }

    @Override
    public List<PostTag> getByPostId(Long postId) {
        QueryWrapper<PostTagRelation> postTagRelationQueryWrapper = new QueryWrapper<>();
        postTagRelationQueryWrapper.eq("post_id",postId);
        List <PostTagRelation> postTagRelationList = postTagRelationService.list(postTagRelationQueryWrapper);
        List<Long> postTagIdList = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());
        QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper();
        postTagQueryWrapper.in("post_tag_id",postTagIdList);
        return this.list(postTagQueryWrapper);
    }

    @Override
    public boolean removeTag(Long postTagId) {
        QueryWrapper<PostTagRelation> postTagRelationQueryWrapper = new QueryWrapper();
        postTagRelationQueryWrapper.eq("post_tag_id",postTagId);
        postTagRelationService.remove(postTagRelationQueryWrapper);
        return this.removeById(postTagId);
    }
}
