package com.codingmore.vo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import com.codingmore.model.PostTag;
import com.codingmore.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PostsVo", description = "文章Vo")
@TableName(autoResultMap = true)
public class PostsVo extends BaseVO {
    @ApiModelProperty(value = "postsId")
    private Long postsId;

    @ApiModelProperty(value = "对应作者ID")
    private Long postAuthor;

    @ApiModelProperty(value = "发布时间")
    private Date postDate;

    @ApiModelProperty(value = "正文")
    private String postContent;

    @ApiModelProperty(value = "标题")
    private String postTitle;

    @ApiModelProperty(value = "摘录")
    private String postExcerpt;

    @ApiModelProperty(value = "文章状态")
    private String postStatus;

    @ApiModelProperty(value = "评论状态")
    private String commentStatus;

    @ApiModelProperty(value = "修改时间")
    private Date postModified;

    @ApiModelProperty(value = "排序ID")
    private Integer menuOrder;


    @ApiModelProperty(value = "评论总数")
    private Long commentCount;

    @ApiModelProperty(value = "发布人")
    private String userNiceName;

    @ApiModelProperty(value = "栏目ID")
    private Long termTaxonomyId;

    @ApiModelProperty(value = "标签")
    private String tagsName;

    @ApiModelProperty(value = "标签带 ID 带名字")
    private List<PostTag> tags;

    @ApiModelProperty(value = "正文html")
    private String htmlContent;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("属性")
    private Map<String,Object> attribute;

    @ApiModelProperty("浏览量")
    private Long pageView;

    @ApiModelProperty("点赞")
    private Long likeCount;

    @ApiModelProperty("格式化修改时间")
    private String postModifiedShortTime;

    public String getPostModifiedShortTime() {
        return DateUtil.getShortTime(getPostModified());
    }
}
