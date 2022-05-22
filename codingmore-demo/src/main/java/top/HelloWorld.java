package top;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/9/22
 */
public class HelloWorld {
    public static void main(String[] args) {
        System.getProperties().list(System.out);
        System.out.println(System.getProperty("user.home"));

        HashMap<String,String> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("沉默王二");
        list.forEach( e -> {
            map.put(e, e.toLowerCase());
        });
        System.out.println(map);
    }
}
