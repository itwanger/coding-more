package com.codingmore.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 站点
 * </p>
 *
 * @author 石磊
 * @since 2021-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Site对象", description="站点")
public class SiteParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long siteId;

    @ApiModelProperty("站点名称")
    @NotBlank(message = "站点名称不能为空")
    private String siteName;

    @ApiModelProperty("站点介绍")
    private String siteDesc;

    @NotBlank(message = "站点域名不能为空")
    @ApiModelProperty("站点域名")
    private String domain;

    @NotBlank(message = "模板方案不能为空")
    @ApiModelProperty("模板方案")
    private String telName;

    @ApiModelProperty("静态目录")
    private String staticDir;




}
