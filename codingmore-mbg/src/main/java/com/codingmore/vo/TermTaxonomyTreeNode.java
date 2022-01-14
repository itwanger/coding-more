package com.codingmore.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="栏目树形节点")
public class TermTaxonomyTreeNode implements Serializable {

    @ApiModelProperty(value = "ID")
    private Long termTaxonomyId;


    @ApiModelProperty(value = "父栏目id")
    private Long parentId;

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "栏目名称")
    private String name;

    @ApiModelProperty(value = "模板路径")
    private String tplPath;

    @ApiModelProperty(value = "内容模板模板路径")
    private String contentTplPath;

    @ApiModelProperty(value = "说明")
    private String description;

    private List<TermTaxonomyTreeNode> children;
}
