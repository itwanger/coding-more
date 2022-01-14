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
 * 用户属性
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Usermeta对象", description="用户属性")
public class Usermeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "umeta_id", type = IdType.AUTO)
    private Long umetaId;

    @ApiModelProperty(value = "对应用户ID")
    private Long userId;

    @ApiModelProperty(value = "键名")
    private String metaKey;

    @ApiModelProperty(value = "键值")
    private String metaValue;


}
