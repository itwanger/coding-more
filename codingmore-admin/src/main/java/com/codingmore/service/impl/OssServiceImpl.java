package com.codingmore.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSSClient;
import com.codingmore.service.IOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements IOssService {

    @Value("${aliyun.oss.maxSize}")
    private int maxSize;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.dir.prefix}")
    private String dirPrefix;

    @Value("${aliyun.cdn}")
    private String cdn;

    @Autowired
    private OSSClient ossClient;

    private static final String[] imageExtension = {".jpg", ".jpeg", ".png", ".gif"};

    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);

    /**
     * 根据图片链接将其上传到 OSS
      * @param url
     * @return
     */
    @Override
    public String upload(String url) {
        String objectName = getFileName(url);
        try (InputStream inputStream = new URL(url).openStream()) {
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (IOException e) {
            LOGGER.error("根据外链上传图片到 OSS 出错了：", e.getMessage());
        }
        return formatOSSPath(objectName);
    }

    private String getFileName(String url) {
        String ext = "";
        for (String extItem : imageExtension) {
            if (url.indexOf(extItem) != -1) {
                ext = extItem;
                break;
            }
        }
        return dirPrefix + DateUtil.today() + "/" + IdUtil.randomUUID() + ext;
    }

    private String formatOSSPath(String objectName) {
        return "https://" + cdn  + "/" + objectName;
    }

    @Override
    public String upload(InputStream inputStream, String name) {
        String objectName = getFileName(name);
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        return formatOSSPath(objectName);
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            return upload(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getEndPoint() {
        return ossClient.getEndpoint().getHost();
    }
}