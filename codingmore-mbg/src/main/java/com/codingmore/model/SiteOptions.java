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
 * 站点属性配置
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SiteOptions对象", description="站点属性配置")
public class SiteOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "option_id")
    @TableId(value = "option_id", type = IdType.AUTO)
    private Long optionId;

    @ApiModelProperty(value = "键名")
    private String optionName;

    @ApiModelProperty(value = "键值")
    private String optionValue;

    @ApiModelProperty(value = "站点id")
    private Long siteId;


}
