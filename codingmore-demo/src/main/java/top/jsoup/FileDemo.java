package top.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * @author 微信搜「沉默王二」，回复关键字 Java
 */
public class FileDemo {
    public static void main(String[] args) throws IOException {
        String path = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/docs/szjy/";
        Document document = Jsoup.parse(new File(path + "tobebetterjavaer-beian.md"), "utf-8");
        Elements links = document.select("a[href]");  //原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/jsoup/jsoup-quick-start.html


    }
}
