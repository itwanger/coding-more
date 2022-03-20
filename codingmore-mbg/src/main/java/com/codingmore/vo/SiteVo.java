package com.codingmore.vo;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SiteVo {
    
    private Long siteId;

    @ApiModelProperty("站点名称")
    private String siteName;

    @ApiModelProperty("站点介绍")
    private String siteDesc;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("属性")
    private Map<String,Object> attribute;
    private String attributeStr;

    public String getAttributeStr() {
        if(attribute != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return  objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return attributeStr;
    }
}
