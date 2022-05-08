package com.codingmore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="文章分页对象", description="文章")

public class PostsPageQueryParam {
    long pageSize;
    long page;

    @ApiModelProperty(value = "排序字段,按数据库字段:menu_order,post_date,post_modified,page_view;多个以逗号间隔")
    private String orderBy;
    @ApiModelProperty(value = "是否升序，boolean类型")
    private boolean isAsc;
    @ApiModelProperty(value = "栏目id非必填")
    private Long termTaxonomyId;
    @ApiModelProperty(value = "标题关键字")
    private String postTitleKeyword;
    @ApiModelProperty(value = "要查询的标签id")
    private Long searchTagId;

    @ApiModelProperty(value = "文章状态PUBLISHED,DELETED,DRAFT")
    private String postStatus;


}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        