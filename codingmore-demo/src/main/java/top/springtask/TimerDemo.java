package top.springtask;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class TimerDemo {
    public static void main(String[] args) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.debug("当前时间{}线程名称{}", DateTime.now(),
                        Thread.currentThread().getName());
            }
        };
        log.debug("当前时间{}线程名称{}", DateTime.now(),
                Thread.currentThread().getName());
        Timer timer = new Timer("TimerDemo");
        timer.schedule(task,1000L);
    }
}
