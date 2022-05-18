package top.hutool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.FileReader;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 微信搜「沉默王二」，回复关键字 Java
 */
public class Demo {
    public static void main(String[] args) {
//        String path = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/docs/szjy/";
//        FileReader fileReader = FileReader.create(new File(path+"tobebetterjavaer-beian.md"), Charset.forName("utf-8"));
//
//        String pattern = "^!\\[](.*)$";
//
//        fileReader.readLines(new LineHandler() {
//            @Override
//            public void handle(String line) {
//                System.out.println(line);
//                Pattern r = Pattern.compile(pattern);
//                Matcher m = r.matcher(line);
//                System.out.println(m.matches());
//            }
//        });

        Date now = DateTime.now();
        Date publishTime = DateUtil.parse("2022/4/25 18:55:00");
        System.out.println(DateUtil.between(now,publishTime, DateUnit.MINUTE, false));


        List<File> files = FileUtil.loopFiles("/Users/itwanger/Documents/GitHub/toBeBetterJavaer/images/");
//        for (File file: files) {
//            System.out.println(file.getAbsolutePath());
//            System.out.println(file.getName());
//            System.out.println(file.getPath());
//        }

//        FileReader mdReader = FileReader.create(, Charset.forName("utf-8"));
//        String content = mdReader.readString();

        System.out.println(System.getProperty("user.home"));
    }
}
