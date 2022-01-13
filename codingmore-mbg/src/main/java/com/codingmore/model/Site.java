package com.codingmore.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "site_id", type = IdType.AUTO)
    private Long siteId;

    @TableField("site_name")
    @ApiModelProperty("站点名称")
    @NotBlank(message = "站点名称不能为空")
    private String siteName;

    @TableField("site_desc")
    @ApiModelProperty("站点介绍")
    private String siteDesc;

    @TableField("domain")
    @NotBlank(message = "站点域名不能为空")
    @ApiModelProperty("站点域名")
    private String domain;

    @TableField("tel_name")
    @NotBlank(message = "模板方案不能为空")
    @ApiModelProperty("模板方案")
    private String telName;

    @TableField("static_dir")
    @ApiModelProperty("静态目录")
    private String staticDir;

    @TableField("create_user_id")
    @ApiModelProperty("创建用户id")
    private Long createUserId;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("update_time")
    @ApiModelProperty("修改时间")
    private Date updateTime;


}
