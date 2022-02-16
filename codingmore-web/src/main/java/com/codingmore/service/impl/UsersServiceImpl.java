package com.codingmore.service.impl;

import com.codingmore.model.Users;
import com.codingmore.mapper.UsersMapper;
import com.codingmore.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private static Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    
}
