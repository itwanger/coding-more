package com.codingmore.service.impl;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.aliyun.oss.OSSClient;
import com.codingmore.service.IOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.hutool.core.lang.UUID;

@Service
public class OssServiceImpl implements IOssService{
   
    @Value("${aliyun.oss.maxSize}")
    private int maxSize;
   
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
  
    @Value("${aliyun.oss.dir.prefix}")
    private String dirPrefix;
    
    @Autowired
    private OSSClient ossClient;

    private String [] imageExtension = {".jpg", ".jpeg", ".png", ".gif"};

    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);

    @Override
    public String upload(String url) {
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName =  getBucketName( url);
         try (InputStream inputStream = new URL(url).openStream()){            
             // 创建PutObject请求。
             ossClient.putObject(bucketName, objectName, inputStream);
         } catch (Exception oe) {
           LOGGER.error(oe.getMessage());
         }
        return getPath(objectName);
    }

    private String getBucketName(String url){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        String ext = "";
        
        for(String extItem:imageExtension){
            if(url.indexOf(extItem) != -1){
                ext = extItem;
                break;
            }
        }

        return dirPrefix+dateStr+"/"+UUID.randomUUID().toString()+ext;
    }

    private String getPath(String objectName){
        return "https://"  +bucketName+"."+ ossClient.getEndpoint().getHost() + "/" + objectName;
    }


    @Override
    public String upload(InputStream inputStream,String name) {
        String objectName = getBucketName(name);
        try {            
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (Exception oe) {
          LOGGER.error(oe.getMessage());
        }
        return getPath(objectName);
    }

    @Override
    public String getEndPoint() {
        // TODO Auto-generated method stub
        return ossClient.getEndpoint().getHost() ;
    }
    
    
    


}