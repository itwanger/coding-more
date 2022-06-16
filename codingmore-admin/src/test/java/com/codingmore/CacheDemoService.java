package com.codingmore;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/16/22
 */
public interface CacheDemoService {
    Object getFromDB(Integer id);
}
