package com.codingmore.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSSClient;
import com.codingmore.exception.Asserts;
import com.codingmore.service.IOssService;
import com.codingmore.util.FileNameUtil;
import com.codingmore.util.OSSUtil;
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


    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);

    /**
     * 根据图片链接将其上传到 OSS
      * @param url
     * @return
     */
    @Override
    public String upload(String url) {
        String objectName = FileNameUtil.getFileName(dirPrefix, url);
        try (InputStream inputStream = new URL(url).openStream()) {
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (IOException e) {
            LOGGER.error("根据外链上传图片到 OSS 出错了：", e);
            Asserts.fail("上传图片到 OSS 出错了");
        }
        return OSSUtil.formatOSSPath(objectName, cdn);
    }

    @Override
    public String upload(InputStream inputStream, String name) {
        String objectName = FileNameUtil.getFileName(dirPrefix, name);
        // 创建PutObject请求。
        ossClient.putObject(bucketName, objectName, inputStream);
        return OSSUtil.formatOSSPath(objectName, cdn);
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
    public boolean needUpload(String imageUrl) {
        if (imageUrl.indexOf(ossClient.getEndpoint().getHost()) != -1) {
            return false;
        }
        if (imageUrl.indexOf(cdn) != -1) {
            return false;
        }
        return true;
    }
}