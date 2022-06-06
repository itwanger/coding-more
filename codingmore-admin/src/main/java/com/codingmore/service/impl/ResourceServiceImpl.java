package com.codingmore.service.impl;

import com.codingmore.model.Resource;
import com.codingmore.mapper.ResourceMapper;
import com.codingmore.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingmore.service.IUsersCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {
    @Autowired
    private IUsersCacheService usersCacheService;
    @Override
    public boolean remove(Long resourceId) {
        usersCacheService.delResourceListByResourceId(resourceId);
        return this.removeById(resourceId);
    }
}
