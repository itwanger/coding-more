package com.codingmore.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.PostTagParam;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.dto.PostsParam;
import com.codingmore.model.*;
import com.codingmore.mapper.PostsMapper;
import com.codingmore.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.state.TermRelationType;
import com.codingmore.vo.PostsVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {
    @Autowired
    private IUsersService iUsersService;
    @Autowired
    private ITermRelationshipsService iTermRelationshipsService;
    @Autowired
    private IPostTagService iPostTagService;
    @Autowired
    private IPostTagRelationService iPostTagRelationService;
    @Autowired
    private ThreadPoolTaskExecutor ossUploadImageExecutor;
    @Autowired
    private IOssService iOssService;
    private static Logger LOGGER = LoggerFactory.getLogger(PostsServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean savePosts(PostsParam postsParam) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam, posts);
        try {
            handleAttribute(postsParam, posts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        posts.setCommentCount(0L);
        if (posts.getPostDate() == null) {
            posts.setPostDate(new Date());
        }
        posts.setPostAuthor(iUsersService.getCurrentUserId());
        handleContentImg(posts);
        this.save(posts);
        this.insertorUpdateTag(postsParam, posts);
        insertTermRelationships(postsParam, posts);
        return true;

    }

    private boolean insertorUpdateTag(PostsParam postsParam, Posts posts) {
        if (StringUtils.isBlank(postsParam.getTags())) {
            return false;
        }
        //删除旧的内容标签关联
        QueryWrapper<PostTagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", posts.getPostsId());
        iPostTagRelationService.remove(queryWrapper);
        String[] tags = postsParam.getTags().split(",");
        // TODO: 2021/11/14 先默认 循环添加
        for (String tag : tags) {
            QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<>();
            postTagQueryWrapper.eq("description", tag);
            List<PostTag> tagList = iPostTagService.list(postTagQueryWrapper);
            if (tagList.size() == 0) {
                PostTagParam postTagParam = new PostTagParam();
                postTagParam.setPostId(posts.getPostsId());
                postTagParam.setDescription(tag);
                postTagParam.setTermOrder(0);
                iPostTagService.savePostTag(postTagParam);

            } else {
                PostTagRelation postTagRelation = new PostTagRelation();
                postTagRelation.setPostTagId(tagList.get(0).getPostTagId());
                postTagRelation.setPostId(posts.getPostsId());
                postTagRelation.setTermOrder(0);
                iPostTagRelationService.save(postTagRelation);
            }
        }

        return true;
    }

    @Override
    public boolean updatePosts(PostsParam postsParam) {
        Posts posts = this.getById(postsParam.getPostsId());
        Date publishDate = posts.getPostDate();
        BeanUtils.copyProperties(postsParam, posts);
        try {
            handleAttribute(postsParam, posts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 防止修改发布时间
        posts.setPostDate(publishDate);
        posts.setPostModified(new Date());
        handleContentImg(posts);
        this.updateById(posts);

        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", postsParam.getPostsId());
        // 删除旧的栏目内容管理
        iTermRelationshipsService.remove(queryWrapper);
        this.insertorUpdateTag(postsParam, posts);
        insertTermRelationships(postsParam, posts);
        return true;

    }

    @Override
    public boolean removePostsById(Long id) {
        this.removeById(id);
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", id);

        return iTermRelationshipsService.remove(queryWrapper);
    }

    @Override
    public PostsVo getPostsById(Long id) {
        Posts posts = this.getById(id);
        PostsVo postsVo = new PostsVo();
        if(posts == null){
            return postsVo;
        }
        BeanUtils.copyProperties(posts, postsVo);
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", posts.getPostsId());
        List<TermRelationships> termRelationshipsList = iTermRelationshipsService.list(queryWrapper);
        if (termRelationshipsList.size() > 0) {
            postsVo.setTermTaxonomyId(termRelationshipsList.get(0).getTermTaxonomyId());
        }
        QueryWrapper<PostTagRelation> tagRelationWrapper = new QueryWrapper<>();
        tagRelationWrapper.eq("post_id", posts.getPostsId());
        List<PostTagRelation> postTagRelationList = iPostTagRelationService.list(tagRelationWrapper);
        if (postTagRelationList.size() > 0) {
            List<Long> tagIds = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());
            QueryWrapper<PostTag> tagQuery = new QueryWrapper<>();
            tagQuery.in("post_tag_id", tagIds);
            List<PostTag> postTags = iPostTagService.list(tagQuery);
            postsVo.setTagsName( StringUtils.join(postTags.stream().map(PostTag::getDescription).collect(Collectors.toList()), ","));
        }
        
        Users users = iUsersService.getById(posts.getPostAuthor());
        postsVo.setUserNiceName(users.getUserNicename());
        return postsVo;
    }

    @Override
    public IPage<PostsVo> findByPage(PostsPageQueryParam postsPageQueryParam) {
        QueryWrapper<PostsPageQueryParam> queryWrapper = new QueryWrapper<>();
        if (postsPageQueryParam.getTermTaxonomyId() != null) {
            queryWrapper.eq("b.term_taxonomy_id", postsPageQueryParam.getTermTaxonomyId());
        }
        if(postsPageQueryParam.getOrderBy()!=null){
            queryWrapper.orderBy(true,postsPageQueryParam.isAsc(),postsPageQueryParam.getOrderBy());
        }
        if(StringUtils.isNotEmpty(postsPageQueryParam.getPostTitleKeyword())){
            queryWrapper.like("post_title","%"+postsPageQueryParam.getPostTitleKeyword()+"%");
        }
        if(StringUtils.isNotEmpty(postsPageQueryParam.getPostStatus())){
            queryWrapper.eq("post_status",postsPageQueryParam.getPostStatus());
        }

        Page<PostsVo> postsPage = new Page<>(postsPageQueryParam.getPage(), postsPageQueryParam.getPageSize());

        return this.getBaseMapper().findByPage(postsPage, queryWrapper);
    }

    private boolean insertTermRelationships(PostsParam postsParam, Posts posts) {
        if(postsParam.getTermTaxonomyId()==null){
            return false;
        }
        TermRelationships termRelationships = new TermRelationships();
        termRelationships.setTermTaxonomyId(postsParam.getTermTaxonomyId());
        termRelationships.setTermRelationshipsId(posts.getPostsId());
        termRelationships.setTermOrder(postsParam.getMenuOrder());
        termRelationships.setType(TermRelationType.CONTENT.getType());
        return iTermRelationshipsService.save(termRelationships);
    }

    /**
     * 处理扩展字段
     */
    private void handleAttribute(PostsParam postsParam, Posts posts) throws JsonProcessingException {
        if (StringUtils.isNotBlank(postsParam.getAttribute())) {
            Map attribute = objectMapper.readValue(postsParam.getAttribute(), Map.class);
            posts.setAttribute(attribute);
        }
    }

    /**
     * 替换图片
     * @param posts
     */
    private void handleContentImg( Posts posts) {
        String content = posts.getPostContent();
        String htmlContent = posts.getHtmlContent();
        if(StringUtils.isBlank(content)){
            return;
        }
        
        // System.out.println(content);
        String pattern = "!\\[[^\\]]+\\]\\([^)]+\\)";

        // StringBuffer operatorStr=new StringBuffer(content);
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);

        Map<String, Future<String>> map = new HashMap<>();
        // List<String> image
        while (m.find()) {
            // 使用分组进行替换
            LOGGER.info("{}", m.group());
            String imageTag = m.group();
            String imageUrl = imageTag.substring(imageTag.indexOf("(") + 1, imageTag.indexOf(")"));
            if(imageUrl.indexOf(iOssService.getEndPoint())>=0){
                continue;
            }
            Future<String> future = ossUploadImageExecutor.submit(() -> {
                return iOssService.upload(imageUrl);
            });
            map.put(imageUrl, future);
        }
        try {
            for (String oldUrl : map.keySet()) {
                Future<String> future = map.get(oldUrl);
                String imageUrl = future.get(); // 获取返回结果 不阻塞
                content = content.replace(oldUrl, imageUrl);
                if(StringUtils.isBlank(htmlContent)){
                    htmlContent= htmlContent.replace(oldUrl, imageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("{}", e);
        }
        posts.setPostContent(content);
        posts.setHtmlContent(htmlContent);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
}
