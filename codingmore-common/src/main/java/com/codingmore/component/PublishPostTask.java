package com.codingmore.component;

import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务 Spring Task
 *
 * by 沉默王二
 */
@Component
public class PublishPostTask {
    private static Logger LOGGER = LoggerFactory.getLogger(PublishPostTask.class);

    @Scheduled(cron = "0 0/1 * ? * ?")
    public void publishPost() {
        // TODO 定时发布文章
        LOGGER.info("定时发布文章，线程{}，时间{}", Thread.currentThread().getName(),
                DateUtil.now());
    }
}
