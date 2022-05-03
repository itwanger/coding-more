package com.codingmore.component;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务 Spring Task
 *
 * by 沉默王二
 */
@Component
@Slf4j
@EnableAsync
public class PublishPostTask {
    private static Logger LOGGER = LoggerFactory.getLogger(PublishPostTask.class);

    private List<Integer> index = Arrays.asList(6, 6, 2, 3);
    int i = 0;

//    @Scheduled(cron = "0 0/1 * ? * ?")
    public void publishPost() {
        // TODO 定时发布文章
        LOGGER.info("定时发布文章，线程{}，时间{}", Thread.currentThread().getName(),
                DateUtil.now());
    }

    /**
     * fixedRate：固定速率执行。每5秒执行一次。
     */
//    @Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRate() {
        log.info("Current Thread : {}", Thread.currentThread().getName());
        log.info("Fixed Rate Task : The time is now {}", DateUtil.now());
    }

    /**
     * fixedDelay：固定延迟执行。距离上一次调用成功后2秒才执。
     */
//    @Scheduled(fixedDelay = 2000)
    public void reportCurrentTimeWithFixedDelay() {
        try {
            TimeUnit.SECONDS.sleep(3);
            log.info("Fixed Delay Task : The time is now {}",DateUtil.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialDelay:初始延迟。任务的第一次执行将延迟5秒，然后将以5秒的固定间隔执行。
     */
//    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void reportCurrentTimeWithInitialDelay() {
        log.info("Fixed Rate Task with Initial Delay : The time is now {}", DateUtil.now());
    }

    // @Scheduled(fixedRate = 5000)
    public void reportCurrentTimeWithFixedRateTest() {
        if (i == 0) {
            log.info("Start time is {}", DateUtil.now());
        }
        if (i < 4) {
            try {
                TimeUnit.SECONDS.sleep(index.get(i));
                log.info("Fixed Rate Task : The time is now {}", DateUtil.now());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
