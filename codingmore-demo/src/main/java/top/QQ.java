package top;

import cn.hutool.core.text.UnicodeUtil;

import java.net.Socket;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/7/22
 */
public class QQ {
    public static void main(String [] args) {
        System.out.println("模仿 QQ");

        Socket socket = new Socket();
        System.out.println("写不出来，放弃");

        String s = UnicodeUtil.toString("\\u002F\\u002F");
        System.out.println(s);
    }
}
