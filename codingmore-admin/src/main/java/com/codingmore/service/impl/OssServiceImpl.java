package com.codingmore.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.aliyun.oss.OSSClient;
import com.codingmore.exception.Asserts;
import com.codingmore.service.IOssService;
import com.codingmore.util.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class OssServiceImpl implements IOssService {

    @Value("${aliyun.oss.maxSize}")
    private int maxSize;

    @Value("${aliyun.oss.bucketName}")
    private String ossBucketName;

    @Value("${aliyun.oss.dir.prefix}")
    private String ossDirPrefix;

    // Spring EL方式
    @Value("${aliyun.cdn:#{null}}")
    private String aliyunCdnDomain;

    @Autowired
    private OSSClient ossClient;

    /**
     * 根据图片链接将其上传到 OSS
      * @param url
     * @return
     */
    @Override
    public String upload(String url) {
        // codingmore/images/ + 2022年06月09日 + UUID + .jpg
        String imgName = ossDirPrefix + FileNameUtil.getImgName(url);
        try (InputStream inputStream = new URL(url).openStream()) {
            ossClient.putObject(ossBucketName, imgName, inputStream);
        } catch (IOException e) {

            log.error("根据外链上传图片到 OSS 出错了：", e);
            Asserts.fail("上传图片到 OSS 出错了");
        }
        return getImgUrl(imgName);
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            String objectName = ossDirPrefix + FileNameUtil.getImgName(file.getOriginalFilename());
            // 创建PutObject请求。
            ossClient.putObject(ossBucketName, objectName, file.getInputStream());
            return getImgUrl(objectName);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public boolean needUpload(String imageUrl) {
        if (imageUrl.indexOf(ossClient.getEndpoint().getHost()) != -1) {
            return false;
        }
        if (aliyunCdnDomain != null && imageUrl.indexOf(aliyunCdnDomain) != -1) {
            return false;
        }
        return true;
    }

    private String getImgUrl(String imgName) {
        if (aliyunCdnDomain == null) {
            // https://itwanger-oss.oss-cn-beijing.aliyuncs.com/studymore/images/2022-05-13/f7d5ef0e-61e7-41d2-9281-3035a5c81d3c.png
            return "https://" + ossClient.getEndpoint().getHost()  + "/" + imgName;
        }
        return "https://" + aliyunCdnDomain  + "/" + imgName;
    }


}