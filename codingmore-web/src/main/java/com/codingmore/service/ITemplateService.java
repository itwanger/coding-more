package com.codingmore.service;

import java.util.List;

/**
 * 模板处理业务逻辑
 */
public interface ITemplateService {
    /**
     * 栏目目录名称
     */
     String CHANNEL_DIR = "channel";

    /**
     * 内容目录名称
     */
    String CONTENT_DIR = "content";
    /**
     * 首页目录名称
     */
     String INDEX_DIR = "index";
    /**
     * 公共页面目录
     */
     String COMMON_DIR = "common";
    /**
     * 专题页面目录
     */
    String SPECIAL_DIR = "special";

    /**
     * 根据模板方案获取栏目模板列表
     * @param templateName
     * @return
     */
    List<String> getChannelTemplateList(String templateName);

    /**
     * 获取所有模板
     * @return
     */
    List<String> getTemplateList();

    /**
     * 获取所有内部模板
     * @return
     */
    List<String> getContentTemplateList(String templateName);

}
