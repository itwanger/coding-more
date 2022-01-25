package top.jsoup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author 微信搜「沉默王二」，回复关键字 Java
 */
public class SubDemo {
    public static void main(String[] args) throws IOException {
        String line = "![](https://upload-images.jianshu.io/upload_images/1179389-71c15111525af102.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)";
        System.out.println(line.indexOf(".png"));
        URL url = new URL(line.substring("![](".length(), line.indexOf(".png")) + ".png");
        String downloadPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/images/szjy/tobebetterjavaer-beian-";

        InputStream inputStream = url.openStream();
        OutputStream outputStream = new FileOutputStream(downloadPath + 1 + "test.png");
        byte[] buffer = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }
}
