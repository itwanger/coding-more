package com.codingmore.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章属性表
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Postmeta对象", description="文章属性表")
public class Postmeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "meta_id", type = IdType.AUTO)
    private Long metaId;

    @ApiModelProperty(value = "对应文章ID")
    private Long postId;

    @ApiModelProperty(value = "键名")
    private String metaKey;

    @ApiModelProperty(value = "键值")
    private String metaValue;


}
