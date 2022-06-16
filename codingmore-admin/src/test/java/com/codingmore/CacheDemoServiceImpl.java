package com.codingmore;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/16/22
 */
@Service
public class CacheDemoServiceImpl implements CacheDemoService {


    @Caching(cacheable = {
            @Cacheable(cacheNames = "demoCache", key = "#id + 0"),
    })
    @Override
    public Object getFromDB(Integer id) {
        System.out.println("模拟去db查询~~~" + id);
        return "hello cache...";
    }
}
