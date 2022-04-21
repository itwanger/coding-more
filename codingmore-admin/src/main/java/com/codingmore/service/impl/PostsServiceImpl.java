package com.codingmore.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.dto.PostAddTagParam;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.dto.PostsParam;
import com.codingmore.model.*;
import com.codingmore.mapper.PostsMapper;
import com.codingmore.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.state.PostStatus;
import com.codingmore.state.TermRelationType;
import com.codingmore.vo.PostsVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
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
import java.util.concurrent.ExecutionException;
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
    @Autowired
    private RedisService redisService;
    private static final String PAGE_VIEW_KEY = "pageView";
    private static final String POST_LIKE_COUNT = "likeCount";

    // 匹配图片的 markdown 语法
    // ![](hhhx.png)
    // ![xx](hhhx.png?ax)
    public static final String IMG_PATTERN = "\\!\\[(.*)\\]\\((.*)\\)";

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Logger LOGGER = LoggerFactory.getLogger(PostsServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void savePosts(PostsParam postsParam) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam, posts);

        // 处理扩展字段
        handleAttribute(postsParam, posts);

        // TODO 定时发布
        handleScheduled(posts);

        // TODO 评论数
        posts.setCommentCount(0L);

        //默认设置发布时间，方便排序
        posts.setPostModified(DateUtil.date());

        // 当然登录用户
        posts.setPostAuthor(iUsersService.getCurrentUserId());

        // 对图片进行转链
        handleContentImg(posts);

        // 保存文章
        save(posts);

        // 处理标签
        insertOrUpdateTag(postsParam, posts);

        // 处理栏目
        insertTermRelationships(postsParam, posts);
    }

    private void handleScheduled(Posts posts) {

        // 条件是指定了发布时间，并且状态为发布
        // 如果指定了发布时间，那么以草稿的形式先保存起来，然后再加入到定时任务中
        // 定时任务到了，执行，从定时任务中删除任务
        // 修改文章的状态为已发布
        if (posts.getPostDate() != null && PostStatus.PUBLISHED.equals(posts.getPostStatus())) {
            LOGGER.debug("定时发布，时间{}，文章状态", DateUtil.formatDateTime(posts.getPostDate()),
                    posts.getPostStatus());
            posts.setPostStatus(PostStatus.DRAFT.name());
            // 开启定时任务
        }
    }

    private boolean insertOrUpdateTag(PostsParam postsParam, Posts posts) {
        if (StringUtils.isBlank(postsParam.getTags())) {
            return false;
        }
        //删除旧的内容标签关联
        QueryWrapper<PostTagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", posts.getPostsId());
        iPostTagRelationService.remove(queryWrapper);
        String[] tags = postsParam.getTags().split(",");
        // TODO: 2021/11/14 先默认 循环添加
        int order = 0;
        for (String tag : tags) {
            QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<>();
            postTagQueryWrapper.eq("description", tag);
            List<PostTag> tagList = iPostTagService.list(postTagQueryWrapper);
            if (tagList.size() == 0) {
                PostAddTagParam postAddTagParam = new PostAddTagParam();
                postAddTagParam.setPostId(posts.getPostsId());
                postAddTagParam.setDescription(tag);
                postAddTagParam.setTermOrder(order);
                iPostTagService.savePostTag(postAddTagParam);

            } else {
                PostTagRelation postTagRelation = new PostTagRelation();
                postTagRelation.setPostTagId(tagList.get(0).getPostTagId());
                postTagRelation.setPostId(posts.getPostsId());
                postTagRelation.setTermOrder(order);
                iPostTagRelationService.save(postTagRelation);
            }
            order++;
        }

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePosts(PostsParam postsParam) {
        Posts posts = this.getById(postsParam.getPostsId());
        Date publishDate = posts.getPostDate();
        BeanUtils.copyProperties(postsParam, posts);

        handleAttribute(postsParam, posts);
        // 防止修改发布时间
        posts.setPostDate(publishDate);
        posts.setPostModified(new Date());
        handleContentImg(posts);
        this.updateById(posts);

        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", postsParam.getPostsId());
        // 删除旧的栏目内容管理
        iTermRelationshipsService.remove(queryWrapper);
        this.insertOrUpdateTag(postsParam, posts);
        insertTermRelationships(postsParam, posts);
        return true;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removePostsById(Long id) {
        this.removeById(id);
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", id);

        //删除浏览量
        redisService.del(PAGE_VIEW_KEY +":"+ id+":*");

        //删除点赞
        redisService.del( POST_LIKE_COUNT +":"+ id+":*");

        return iTermRelationshipsService.remove(queryWrapper);
    }

    @Override
    public PostsVo getPostsById(Long id) {
        Posts posts = this.getById(id);
        PostsVo postsVo = new PostsVo();
        if (posts == null) {
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
        tagRelationWrapper.orderBy(true,true,"term_order");
        List<PostTagRelation> postTagRelationList = iPostTagRelationService.list(tagRelationWrapper);
        if (postTagRelationList.size() > 0) {
            List<Long> tagIds = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());
            QueryWrapper<PostTag> tagQuery = new QueryWrapper<>();
            tagQuery.in("post_tag_id", tagIds);
            List<PostTag> postTags = iPostTagService.list(tagQuery);
            Collections.sort(postTags, new Comparator<PostTag>() {
                @Override
                public int compare(PostTag o1, PostTag o2) {
                    return tagIds.indexOf(o1.getPostTagId())-tagIds.indexOf(o2.getPostTagId());
                }
            });
            postsVo.setTagsName(StringUtils.join(postTags.stream().map(PostTag::getDescription).collect(Collectors.toList()), ","));
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
        if (postsPageQueryParam.getOrderBy() != null) {
            String[] cloums = postsPageQueryParam.getOrderBy().split(",");
            queryWrapper.orderBy(true, postsPageQueryParam.isAsc(), cloums);
        }
        if (StringUtils.isNotEmpty(postsPageQueryParam.getPostTitleKeyword())) {
            queryWrapper.like("post_title", "%" + postsPageQueryParam.getPostTitleKeyword() + "%");
        }
        if (StringUtils.isNotEmpty(postsPageQueryParam.getPostStatus())) {
            queryWrapper.eq("post_status", postsPageQueryParam.getPostStatus());
        }

        Page<PostsVo> postsPage = new Page<>(postsPageQueryParam.getPage(), postsPageQueryParam.getPageSize());

        return this.getBaseMapper().findByPage(postsPage, queryWrapper);
    }

    

    private boolean insertTermRelationships(PostsParam postsParam, Posts posts) {
        if (postsParam.getTermTaxonomyId() == null) {
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
    private void handleAttribute(PostsParam postsParam, Posts posts) {
        if (StringUtils.isNotBlank(postsParam.getAttribute())) {
            try {
                Map attribute = objectMapper.readValue(postsParam.getAttribute(), Map.class);
                posts.setAttribute(attribute);
            } catch (JsonProcessingException e) {
                LOGGER.error("扩展字段处理出错：{}", e.getMessage());
            }

        }
    }

    /**
     * 对外链图片进行转链
     *
     * @param posts
     */
    private void handleContentImg(Posts posts) {
        String content = posts.getPostContent();
        String htmlContent = posts.getHtmlContent();

        // 没有内容不处理
        if (StringUtils.isBlank(content)||StringUtils.isBlank(htmlContent)) {
            return;
        }

        htmlContent = StringEscapeUtils.unescapeHtml4(htmlContent);

        Pattern p = Pattern.compile(IMG_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);

        Map<String, Future<String>> map = new HashMap<>();

        while (m.find()) {
            String imageName = m.group(1);
            String imageUrl = m.group(2);

            LOGGER.info("使用分组进行替换图片名字：{}，图片路径：{}", imageName, imageUrl);

            // 确认是本站链接，不处理
            if (imageUrl.indexOf(iOssService.getEndPoint()) != -1) {
                continue;
            }

            // 通过线程池将图片上传到 OSS
            Future<String> future = ossUploadImageExecutor.submit(() -> {
                return iOssService.upload(imageUrl);
            });
            map.put(imageUrl, future);
        }
        for (String oldUrl : map.keySet()) {
            Future<String> future = map.get(oldUrl);

            try {
                String imageUrl = future.get();
                content = content.replace(oldUrl, imageUrl);

                if (StringUtils.isNotBlank(htmlContent)) {
                    htmlContent = htmlContent.replace(oldUrl, imageUrl);
                }
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("获取图片链接出错{}", e.getMessage());
            }
        }
        posts.setPostContent(content);
        posts.setHtmlContent(htmlContent);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertPostTermTaxonomy(Long[] postsIds, Long[] termTaxonomyIds) {
        if (postsIds == null || termTaxonomyIds == null) {
            return 0;
        }
        int addCount = 0;
        List<TermRelationships> list = new ArrayList<>();
        for(Long postsId:postsIds){
            for(Long termTaxonomyId:termTaxonomyIds){
                QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("term_taxonomy_id", termTaxonomyId);
                queryWrapper.eq("term_relationships_id", postsId);
                int count = iTermRelationshipsService.count(queryWrapper);
                if(count==0){
                    TermRelationships termRelationships = new TermRelationships();
                    termRelationships.setTermTaxonomyId(termTaxonomyId);
                    termRelationships.setTermRelationshipsId(postsId);
                    termRelationships.setType(TermRelationType.CONTENT.getType());
                    list.add(termRelationships);
                 
                    addCount++;
                }
            }
        }
        iTermRelationshipsService.saveBatch(list);
        return addCount;
    }
}
