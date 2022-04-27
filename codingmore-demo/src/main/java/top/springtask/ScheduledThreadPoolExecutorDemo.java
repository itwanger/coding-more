package top.springtask;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.debug("当前时间{}线程名称{}", DateTime.now(),
                        Thread.currentThread().getName());
            }
        };

        log.debug("当前时间{}线程名称{}", DateTime.now(),
                Thread.currentThread().getName());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        executorService.scheduleAtFixedRate(task, 1000L,1000L, TimeUnit.MILLISECONDS);
        Thread.sleep(1000+1000*4);
        executorService.shutdown();
    }
}
