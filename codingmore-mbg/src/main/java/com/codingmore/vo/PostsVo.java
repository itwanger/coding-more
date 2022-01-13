package com.codingmore.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PostsVo", description = "文章Vo")
public class PostsVo {
    @ApiModelProperty(value = "ID")
    private Long id;

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

    @ApiModelProperty(value = "文章类型（post/page等）")
    private String postType;

    @ApiModelProperty(value = "评论总数")
    private Long commentCount;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "发布人")
    private String userNiceName;

    @ApiModelProperty(value = "栏目ID")
    private Long termTaxonomyId;
}
