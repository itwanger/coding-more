package com.codingmore.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.component.PublishPostJob;
import com.codingmore.dto.PostAddTagParam;
import com.codingmore.dto.PostsPageQueryParam;
import com.codingmore.dto.PostsParam;
import com.codingmore.exception.Asserts;
import com.codingmore.model.*;
import com.codingmore.mapper.PostsMapper;
import com.codingmore.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.state.PostStatus;
import com.codingmore.state.TermRelationType;
import com.codingmore.vo.PostsVo;
import com.codingmore.webapi.ResultObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private IScheduleService scheduleService;

    @Value("${post.schedule.minInterval}")
    private int postScheduleMinInterval;


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

        // 定时发布要更改文章的状态
        boolean needShcduleAfter = handleScheduledBefore(posts);

        // TODO 评论数
        posts.setCommentCount(0L);

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

        if (needShcduleAfter) {
            handleScheduledAfter(posts);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePosts(PostsParam postsParam) {
        if (postsParam.getPostsId() == null) {
            LOGGER.error("更新文章时，文章 ID 为空");
            Asserts.fail("文章 ID 不能为空");
        }

        // 根据文章 ID 获取文章
        Posts posts = getById(postsParam.getPostsId());
        if (posts == null) {
            Asserts.fail("更新文章时，文章不存在");
            LOGGER.error("文章不存在，文章ID{}",postsParam.getPostsId());
        }
        BeanUtils.copyProperties(postsParam, posts);

        handleAttribute(postsParam, posts);

        // 定时发布要更改文章的状态
        // boolean needShcduleAfter = handleScheduledBefore(posts);

        // 更新文章的图片
        handleContentImg(posts);
        updateById(posts);

        // 更新标签
        insertOrUpdateTag(postsParam, posts);

        // 删除原来的栏目
        deleteTermRelationships(postsParam);
        // 插入新的栏目
        insertTermRelationships(postsParam, posts);

        // 重新调整定时发布的时间
        /*if (needShcduleAfter) {
            handleScheduledAfter(posts);
        }*/

    }

    @Override
    public boolean updatePostByScheduler(Long postId) {
        LOGGER.info("更新文章{}状态", postId);
        // 根据文章 ID 获取文章
        Posts posts = getById(postId);
        if (posts == null) {
            LOGGER.error("文章定时发布出错，文章 ID 不存在");
            return false;
        }

        // 文章设置的发布时间
        posts.setPostModified(DateTime.now());
        // 更新发布状态
        posts.setPostStatus(PostStatus.PUBLISHED.name());

        return updateById(posts);

    }

    private void deleteTermRelationships(PostsParam postsParam) {
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", postsParam.getPostsId());
        // 删除旧的栏目内容管理
        iTermRelationshipsService.remove(queryWrapper);
    }

    private void handleScheduledAfter(Posts posts) {
        // 文章已经保存为草稿了，并且拿到了文章 ID
        // 调用定时任务
        String jobName = scheduleService.scheduleFixTimeJob(PublishPostJob.class, posts.getPostDate(), posts.getPostsId().toString());
        LOGGER.debug("定时任务{}开始执行", jobName);
    }

    private boolean handleScheduledBefore(Posts posts) {

        // 条件是指定了发布时间，并且状态为发布
        // 如果指定了发布时间，那么以草稿的形式先保存起来，把文章的 ID传递给定时任务，然后再加入到定时任务中
        // 定时任务到了，执行，从定时任务中删除任务
        // 修改文章的状态为已发布
        // 定时发布一定是草稿状态
        if (posts.getPostDate() != null) {
            LOGGER.debug("定时发布，时间{}", DateUtil.formatDateTime(posts.getPostDate()));

            // 定时任务的时间必须大于当前时间 10 分钟
            if (DateUtil.between(DateTime.now(), posts.getPostDate(), DateUnit.MINUTE, false) <= postScheduleMinInterval) {
                Asserts.fail("定时发布的时间必须在 10 分钟后");
            }

            posts.setPostStatus(PostStatus.DRAFT.name());
            // 开启定时任务
            return true;
        } else {
            // 默认设置发布时间，方便排序
            posts.setPostModified(DateTime.now());
        }
        return false;
    }

    private void insertOrUpdateTag(PostsParam postsParam, Posts posts) {
        // 标签可为空
        if (StringUtils.isBlank(postsParam.getTags())) {
            return;
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
                LOGGER.error("扩展字段处理出错：{}", e);
                Asserts.fail("扩展字段处理出错");
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
        if (StringUtils.isBlank(content) || StringUtils.isBlank(htmlContent)) {
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

            String imageUrl = null;
            try {
                imageUrl = future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("图片地址获取出错{}", e);
                Asserts.fail("图片转链失败");
            }
            content = content.replace(oldUrl, imageUrl);

            if (StringUtils.isNotBlank(htmlContent)) {
                htmlContent = htmlContent.replace(oldUrl, imageUrl);
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

    @Override
    public String uploadMd(MultipartFile file) {
        try {
            return IoUtil.read(file.getInputStream(), "UTF-8");
        } catch (IORuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setOnTop(Long postsId, Integer flag) {
        Posts article = getById(postsId);
        article.setMenuOrder(flag);
        updateById(article);
    }
}
