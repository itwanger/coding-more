package com.codingmore.jedis;

import redis.clients.jedis.Jedis;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/15/22
 */
public class RedisJava {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("118.190.99.232");
        jedis.auth("learnsuccess");
        System.out.println("连接成功");
        System.out.println("ping:"+jedis.ping());
    }
}
