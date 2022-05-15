package com.codingmore.util;

import org.springframework.beans.factory.annotation.Value;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/15/22
 */
public class OSSUtil {
    /**
     * 如果是已经上传过的图片，不需要再上传到 OSS 了
     *
     * @param imagePath
     * @param prePaths
     * @return
     */
    public static boolean needUpload(String imagePath, String ... prePaths) {
        for (String item : prePaths) {
            if (imagePath.indexOf(item) != -1) {
                return false;
            }
        }
        return true;
    }

    public static String formatOSSPath(String objectName,String cdn) {
        return "https://" + cdn  + "/" + objectName;
    }
}
