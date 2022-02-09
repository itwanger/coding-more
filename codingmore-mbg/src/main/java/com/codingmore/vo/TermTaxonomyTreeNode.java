package com.codingmore.vo;

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



    @ApiModelProperty(value = "栏目名称")
    private String name;



    

    @ApiModelProperty(value = "说明")
    private String description;

    private List<TermTaxonomyTreeNode> children;
}
