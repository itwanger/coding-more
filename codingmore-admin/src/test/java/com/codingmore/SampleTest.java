package com.codingmore;

import com.codingmore.mapper.UsersMapper;
import com.codingmore.service.IOssService;
import com.codingmore.service.IUsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UsersMapper userMapper;
    @Autowired
    private IUsersService iUsersService;
    private static Logger LOGGER = LoggerFactory.getLogger(SampleTest.class);
    @Autowired
    private IOssService iOssService;
    
    @Test
    public void testSelect() {
        iOssService.upload("https://pic1.zhimg.com/80/v2-5a6681d8b77b8f17a517f2858ea0bcd8_400x224.jpg?source=1940ef5c");
    }

}
