package top.jsoup;

import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.FileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 微信搜「沉默王二」，回复关键字 Java
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://blog.csdn.net/qing_gee/article/details/122407829").get();
Elements images = doc.select(".article_content img[src~=(?i)\\.(png|jpe?g|gif)]");

int i = 1;
String downloadPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/images/szjy/tobebetterjavaer-beian-";

//for (Element image : images) {
//    URL url = new URL(image.attr("src"));
//    InputStream inputStream = url.openStream();
//    OutputStream outputStream = new FileOutputStream(downloadPath + i + ".png");
//    byte[] buffer = new byte[2048];
//    int length = 0;
//    while ((length = inputStream.read(buffer)) != -1) {
//        outputStream.write(buffer, 0, length);
//    }
//    i++;
//
//    if (i == 3) {
//        break;
//    }
//}




        String path = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/docs/szjy/";
        FileReader fileReader = FileReader.create(new File(path+"tobebetterjavaer-beian.md"), Charset.forName("utf-8"));




        String pre = "https://cdn.jsdelivr.net/gh/itwanger/toBeBetterJavaer/images/szjy/tobebetterjavaer-beian-";


        List<String> list = fileReader.readLines();

        FileWriter writer = new FileWriter(path+"tobebetterjavaer-beian.md");

        String pattern = "^!\\[](.*)$";
        Pattern r = Pattern.compile(pattern);

        int j = 1;
        for (String line : list) {
            Matcher m = r.matcher(line);
            if (m.matches()) {
                URL url = new URL(line.substring("![](".length(), line.indexOf(".png")) + ".png");
                InputStream inputStream = url.openStream();
                OutputStream outputStream = new FileOutputStream(downloadPath + i + ".png");
                byte[] buffer = new byte[2048];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                writer.append("![](" + pre + j++  + ".png)\n");
            } else {
                writer.append(line+"\n");
            }
        }
        writer.flush();

    }
}
