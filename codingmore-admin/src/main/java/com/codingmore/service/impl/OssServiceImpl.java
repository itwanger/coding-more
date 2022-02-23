import com.aliyun.oss.OSS;
import com.codingmore.service.IOssService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OssServiceImpl implements IOssService{
   
    @Value("${aliyun.oss.maxSize}")
    private int maxSize;
   
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
  
    @Value("${aliyun.oss.dir.prefix}")
    private String dirPrefix;
    
    @Autowired
    private OSS ossClient;

    @Override
    public String upload(String url) {
        // TODO Auto-generated method stub
        return null;
    }


}