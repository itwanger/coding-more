package com.codingmore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/16/22
 */
@SpringBootTest
class CodingmoreRedisApplicationTests {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CacheDemoService cacheDemoService;

    @Autowired
    private RedisCacheManager cacheManager;

    @Test
    public void testRedis() {
        cacheDemoService.getFromDB(3);

        System.out.println("----------验证缓存是否生效----------");
        Cache cache = cacheManager.getCache("demoCache");
        System.out.println(cache.get(1, String.class));

    }

}

