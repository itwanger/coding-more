package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Posts对象", description="文章")
public class PostsParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "posts_id")
    private Long postsId;

    @ApiModelProperty(value = "发布时间")
    private Date postDate;

    @ApiModelProperty(value = "正文")
    @NotBlank(message = "文章内容不能为空")
    private String postContent;

    @ApiModelProperty(value = "正文html")
    private String htmlContent;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String postTitle;

    @ApiModelProperty(value = "摘要")
    private String postExcerpt;

    @ApiModelProperty(value = "文章状态：PUBLISHED,DELETED,DRAFT")
    @NotBlank(message = "文章状态不能为空")
    private String postStatus;

    @ApiModelProperty(value = "排序ID")
    private Integer menuOrder;

    @ApiModelProperty(value = "栏目ID")
    private Long termTaxonomyId;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty("属性")
    private String attribute;
}
