package com.codingmore.service.impl;

import com.codingmore.model.UserSite;
import com.codingmore.mapper.UserSiteMapper;
import com.codingmore.service.IUserSiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户站点关联关系表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
@Service
public class UserSiteServiceImpl extends ServiceImpl<UserSiteMapper, UserSite> implements IUserSiteService {

}
