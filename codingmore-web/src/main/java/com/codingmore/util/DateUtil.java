package com.codingmore.util;

import cn.hutool.core.date.DateTime;

import java.util.Date;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 4/28/22
 */
public class DateUtil {
    public static String getShortTime(Date time) {
        String shortString = "";
        long now = DateTime.now().getTime();

        long delTime = (now - time.getTime()) / 1000;
        if (delTime > 365 * 24 * 60 * 60) {
            shortString = (int) (delTime / (365 * 24 * 60 * 60)) + "年前";
        } else if (delTime > 24 * 60 * 60) {
            shortString = (int) (delTime / (24 * 60 * 60)) + "天前";
        } else if (delTime > 60 * 60) {
            shortString = (int) (delTime / (60 * 60)) + "小时前";
        } else if (delTime > 60) {
            shortString = (int) (delTime / (60)) + "分前";
        } else if (delTime > 1) {
            shortString = delTime + "秒前";
        } else {
            shortString = "1秒前";
        }
        return shortString;
    }
}
