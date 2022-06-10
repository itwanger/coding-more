package com.codingmore.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codingmore.assist.RedisConstants;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
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
@Slf4j
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {
    @Autowired
    private IUsersService userService;
    @Autowired
    private ITermRelationshipsService iTermRelationshipsService;
    @Autowired
    private IPostTagService postTagService;
    @Autowired
    private IPostTagRelationService postTagRelationService;
    @Autowired
    private ThreadPoolTaskExecutor ossUploadImageExecutor;
    @Autowired
    private IOssService iOssService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IScheduleService scheduleService;

    @Value("${post.schedule.minInterval}")
    private int postScheduleMinInterval;

    // 匹配图片的 markdown 语法
    // ![](hhhx.png)
    // ![xx](hhhx.png?ax)
    public static final String IMG_PATTERN = "\\!\\[(.*)\\]\\((.*)\\)";

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void savePosts(PostsParam postsParam) {
        Posts posts = new Posts();
        BeanUtils.copyProperties(postsParam, posts);

        // 处理扩展字段
        handleAttribute(postsParam, posts);

        // 定时发布要更改文章的状态
        boolean needScheduleAfter = handleScheduledBefore(postsParam.getPostDate(), posts);

        // TODO 评论数
        posts.setCommentCount(0L);

        // 当然登录用户
        posts.setPostAuthor(userService.getCurrentUserId());

        // 对图片进行转链
        handleContentImg(posts);

        // 保存文章
        save(posts);

        // 处理标签
        insertOrUpdateTag(postsParam.getTags(), posts.getPostsId());

        // 处理栏目
        insertTermRelationships(postsParam, posts);

        if (needScheduleAfter) {
            handleScheduledAfter(posts);
        }
    }

    @Override
    @Transactional
    public void updatePosts(PostsParam postsParam) {
        if (postsParam.getPostsId() == null) {
            log.error("更新文章时，文章 ID 为空");
            Asserts.fail("文章 ID 不能为空");
        }

        // 根据文章 ID 获取文章
        Posts posts = getById(postsParam.getPostsId());
        if (posts == null) {
            log.error("文章不存在，文章ID{}",postsParam.getPostsId());
            Asserts.fail("更新文章时，文章不存在");
        }
        BeanUtils.copyProperties(postsParam, posts);

        handleAttribute(postsParam, posts);

        // 定时发布要更改文章的状态
        boolean needScheduleAfter = handleScheduledBefore(postsParam.getPostDate(), posts);

        // 更新文章的图片
        handleContentImg(posts);
        updateById(posts);

        // 更新标签
        insertOrUpdateTag(postsParam.getTags(), posts.getPostsId());

        // 删除原来的栏目
        deleteTermRelationships(postsParam);
        // 插入新的栏目
        insertTermRelationships(postsParam, posts);

        // 重新调整定时发布的时间
        if (needScheduleAfter) {
            handleScheduledAfter(posts);
        }

    }

    @Override
    public boolean updatePostByScheduler(Long postId) {
        log.info("更新文章{}状态", postId);
        // 根据文章 ID 获取文章
        Posts posts = getById(postId);
        if (posts == null) {
            log.error("文章定时发布出错，文章 ID 不存在");
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
        log.debug("定时任务{}开始执行", jobName);
    }

    private boolean handleScheduledBefore(Date postDate, Posts posts) {

        // 条件是指定了发布时间，并且状态为发布
        // 如果指定了发布时间，那么以草稿的形式先保存起来，把文章的 ID传递给定时任务，然后再加入到定时任务中
        // 定时任务到了，执行，从定时任务中删除任务
        // 修改文章的状态为已发布
        // 定时发布一定是草稿状态
        if (postDate != null) {
            log.debug("定时发布，时间{}", DateUtil.formatDateTime(postDate));

            // 定时任务的时间必须大于当前时间 10 分钟
            if (DateUtil.between(DateTime.now(), postDate, DateUnit.MINUTE, false) <= postScheduleMinInterval) {
                Asserts.fail("定时发布的时间必须在 "+ postScheduleMinInterval +" 分钟后");
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

    private void insertOrUpdateTag(String tags, Long post_Id) {
        log.info("准备更新文章{}标签{}", post_Id, tags);

        log.info("准备删除旧的标签关联");
        QueryWrapper<PostTagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", post_Id);
        postTagRelationService.remove(queryWrapper);

        if (StringUtils.isBlank(tags)) {
            return;
        }

        int order = 0;
        for (String tag : tags.split(",")) {
            QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<>();
            postTagQueryWrapper.eq("post_tag_id", tag);
            List<PostTag> tagList = postTagService.list(postTagQueryWrapper);

            PostTagRelation postTagRelation = new PostTagRelation();
            postTagRelation.setPostTagId(tagList.get(0).getPostTagId());
            postTagRelation.setPostId(post_Id);
            postTagRelation.setTermOrder(order);
            postTagRelationService.save(postTagRelation);

            order++;
        }
    }

    @Override
    @Transactional
    public boolean removePostsById(Long id) {
        this.removeById(id);
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", id);

        //删除浏览量
        redisService.del(RedisConstants.getWebPageViewKey(id+":*"));

        //删除点赞
        redisService.del(RedisConstants.getWebPostLikeKey(id+":*"));

        return iTermRelationshipsService.remove(queryWrapper);
    }

    @Override
    public PostsVo getPostsById(Long id) {
        log.info("获取文章{}", id);
        Posts posts = this.getById(id);
        PostsVo postsVo = new PostsVo();
        if (posts == null) {
            return postsVo;
        }

        // 转成 VO
        BeanUtils.copyProperties(posts, postsVo);

        log.info("获取文章所属栏目{}", posts.getPostsId());
        QueryWrapper<TermRelationships> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("term_relationships_id", posts.getPostsId());
        List<TermRelationships> termRelationshipsList = iTermRelationshipsService.list(queryWrapper);
        if (termRelationshipsList.size() > 0) {
            postsVo.setTermTaxonomyId(termRelationshipsList.get(0).getTermTaxonomyId());
        }

        log.info("获取文章标签{}",posts.getPostsId());
        QueryWrapper<PostTagRelation> tagRelationWrapper = new QueryWrapper<>();
        tagRelationWrapper.eq("post_id", posts.getPostsId());
        tagRelationWrapper.orderBy(true,true,"term_order");

        List<PostTagRelation> postTagRelationList = postTagRelationService.list(tagRelationWrapper);
        log.info("文章标签个数{}", postTagRelationList.size());

        if (postTagRelationList.size() > 0) {
            // 取出标签 ID
            List<Long> tagIds = postTagRelationList.stream().map(PostTagRelation::getPostTagId).collect(Collectors.toList());
            QueryWrapper<PostTag> tagQuery = new QueryWrapper<>();
            tagQuery.in("post_tag_id", tagIds);

            log.info("根据标签 IDS{} 查询标签", tagIds);
            List<PostTag> postTags = postTagService.list(tagQuery);
            Collections.sort(postTags, new Comparator<PostTag>() {
                @Override
                public int compare(PostTag o1, PostTag o2) {
                    return tagIds.indexOf(o1.getPostTagId())-tagIds.indexOf(o2.getPostTagId());
                }
            });

            log.info("排序后的标签{}",postTags);
            postsVo.setTagsName(StringUtils.join(postTags.stream().map(PostTag::getPostTagId).collect(Collectors.toList()), ","));
        }

        log.info("获取文章作者{}", posts.getPostAuthor());
        Users users = userService.getById(posts.getPostAuthor());
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
                log.error("扩展字段处理出错：{}", e);
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

            log.info("使用分组进行替换图片名字：{}，图片路径：{}", imageName, imageUrl);

            // 确认是本站链接，不处理
            if (!iOssService.needUpload(imageUrl)) {
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
                log.error("图片地址获取出错{}", e);
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
    @Transactional
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
