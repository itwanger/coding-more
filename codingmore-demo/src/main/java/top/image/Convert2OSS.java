package top.image;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/5/22
 */
@Slf4j
public class Convert2OSS {
    final static String directory = "springboot/";
    final static String key = "springtask";

    private final static String img_url_pre_afters [] = {
            "http://cdn.tobebetterjavaer.com/tobebetterjavaer/images/",
            "https://cdn.tobebetterjavaer.com/tobebetterjavaer/images/",
            "https://itwanger-oss.oss-cn-beijing.aliyuncs.com/tobebetterjavaer/images/" };
    private final static String docPath = System.getProperty("user.home")+"/Documents/GitHub/toBeBetterJavaer/docs/";
    final static String imgPath = System.getProperty("user.home")+"/Documents/GitHub/toBeBetterJavaer/images/";
    private final static String fileName = directory + key + ".md";
    private static final String[] imageExtension = {".jpg", ".jpeg", ".png", ".gif"};

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private static final String accessKeyId = "LTAI5t924wYTNT9dQoAWGDKa";
    private static final String accessKeySecret = "RYNmqo4yZdTXbPcuQVE4jK9ax0HRLM";
    // 填写Bucket名称，例如examplebucket。
    private static final String bucketName = "itwanger-oss";

    // 匹配图片的 markdown 语法
    // ![](hhhx.png)
    // ![xx](hhhx.png?ax)
    private static final String IMG_PATTERN = "\\!\\[(.*)\\]\\((.*)\\)";

    // 网络上的图片
    // 下载到本地一份（备份）
    // 上传到 OSS 一份（CDN）

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // MD 文件
        File file = new File(docPath + fileName);
        FileReader fileReader = FileReader.create(file, StandardCharsets.UTF_8);
        // 读取全部内容
        String content = fileReader.readString();

        // 正则表达式，找到对应的图片
        Pattern p = Pattern.compile(IMG_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(content);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        while (m.find()) {
            String imageName = m.group(1);
            String imageUrl = m.group(2);
            log.info("使用分组进行替换图片名字：{}，图片路径：{}", imageName, imageUrl);

            // 需要处理的图片链接
            if (needConvert(imageUrl)) {
                InputStream inputStream = new URL(imageUrl).openStream();
                // directory + key + "-" + UUID.fastUUID() + ext
                String objectName = getFileName(imageUrl);
                // oss
                ossClient.putObject(bucketName, "tobebetterjavaer/images/"+objectName, inputStream);
                // bak
                saveLocation(imageUrl, imgPath+objectName);

                content = content.replace(imageUrl, img_url_pre_afters[0] + objectName);
            }
        }

        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
    }

    private static void saveLocation(String imageUrl, String destinationImgPath) throws IOException {
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        OutputStream outputStream = new FileOutputStream(destinationImgPath);
        byte[] buffer = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    private static boolean needConvert(String imageUrl) {
        boolean flag = true;
        for (String extItem : img_url_pre_afters) {
            if (imageUrl.indexOf(extItem) != -1) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static String getFileName(String url) {
        String ext = "";
        for (String extItem : imageExtension) {
            if (url.indexOf(extItem) != -1) {
                ext = extItem;
                break;
            }
        }
        return directory + key + "-" + UUID.fastUUID() + ext;
    }
}
