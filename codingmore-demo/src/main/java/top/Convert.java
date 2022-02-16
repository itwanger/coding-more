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
    final static String directory = "cityselect/";
    final static String key = "hangzhou";

    final static String docPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/docs/" + directory;
    final static String imgPath = "/Users/maweiqing/Documents/GitHub/TechSisterLearnJava/images/" + directory;

    final static String fileName = key +".md";
    final static String pngSuffix = ".png";
    final static String gifSuffix = ".gif";
    final static String jpgSuffix = ".jpg";
    final static String jpegSuffix = ".jpeg";
    final static String imgCdnPre = "https://cdn.jsdelivr.net/gh/itwanger/toBeBetterJavaer/images/" + directory + key + "-";
    final static Pattern pattern = Pattern.compile("^!\\[](.*)$");

    static class MyRunnable implements Runnable {
        private String originImgUrl;
        private String destinationImgPath;
        public MyRunnable(String originImgUrl, String destinationImgPath) {
            this.originImgUrl = originImgUrl;
            this.destinationImgPath = destinationImgPath;
        }
        @SneakyThrows
        @Override
        public void run() {
            URL url = new URL(originImgUrl);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(destinationImgPath);
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

                // png 还是 gif 还是 jpg
                String imgSuffixTemp = pngSuffix;
                int index = line.indexOf(pngSuffix);

                if (index == -1) {
                    index = line.indexOf(gifSuffix);
                    imgSuffixTemp = gifSuffix;
                    if  (index == -1) {
                        index = line.indexOf(jpgSuffix);
                        imgSuffixTemp = jpgSuffix;

                        if (index == -1) {
                            index = line.indexOf(jpegSuffix);
                            imgSuffixTemp = jpegSuffix;
                        }
                    }
                }

                String originImgUrl = line.substring("![](".length(), index) + imgSuffixTemp;
                String destinationImgPath = imgPath + key + "-" + num + imgSuffixTemp;

                // 1、下载到本地
                new Thread(new MyRunnable(originImgUrl, destinationImgPath)).start();

                // 2、修改 MD 文档
                writer.append("![](" + imgCdnPre +  num + imgSuffixTemp +")\n");
                num++;
            } else {
                writer.append(line+"\n");
            }
        }
        writer.flush();
    }
}
