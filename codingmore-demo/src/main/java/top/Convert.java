package top;

import cn.hutool.core.io.file.FileReader;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 把简书的图片链接转到 GitHub CDN
 *
 * @author 微信搜「沉默王二」，回复关键字 Java
 */
public class Convert {
    final static String docPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/docs/szjy/";
    final static String imgPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/images/szjy/";
    final static String key = "tobebetterjavaer-beian";
    final static String fileName = key +".md";
    final static String imgSuffix = ".png";
    final static String imgCdnPre = "https://cdn.jsdelivr.net/gh/itwanger/toBeBetterJavaer/images/szjy/" + key + "-";
    final static Pattern pattern = Pattern.compile("^!\\[](.*)$");

    static class MyRunnable implements Runnable {
        private String num;
        private String line;
        public MyRunnable(String num, String line) {
            this.num = num;
            this.line = line;
        }
        @SneakyThrows
        @Override
        public void run() {
            URL url = new URL(line.substring("![](".length(), line.indexOf(imgSuffix)) + imgSuffix);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(imgPath + key + "-" +num + imgSuffix);
            byte[] buffer = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FileReader fileReader = FileReader.create(new File(docPath +fileName),
                Charset.forName("utf-8"));

        List<String> list = fileReader.readLines();

        FileWriter writer = new FileWriter(docPath + fileName);
        int num = 1;
        for (String line : list) {
            Matcher m = pattern.matcher(line);
            if (m.matches()) {
                // 处理图片
                // 1、下载到本地
                new Thread(new MyRunnable(num + "", line)).start();

                // 2、修改 MD 文档
                writer.append("![](" + imgCdnPre +  num + imgSuffix +")\n");
                num++;
            } else {
                writer.append(line+"\n");
            }
        }
        writer.flush();
    }
}
