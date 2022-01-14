package com.codingmore.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PostTag对象", description="标签表")
public class PostTagParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称")
    private String description;

    @ApiModelProperty(value = "站点id")
    @NotNull(message="站点id不能为空")
    private Long siteId;

    @ApiModelProperty(value = " 对应文章ID")
    private Long objectId;

    @ApiModelProperty(value = "排序")
    private int termOrder;


}
