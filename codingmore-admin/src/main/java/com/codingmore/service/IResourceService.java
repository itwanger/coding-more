package com.codingmore.service;

import com.codingmore.model.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
public interface IResourceService extends IService<Resource> {
    boolean remove(Long resourceId);
}
