package com.codingmore.service.impl;

import com.codingmore.model.AdminRoleRelation;
import com.codingmore.mapper.AdminRoleRelationMapper;
import com.codingmore.service.IAdminRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户和角色关系表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-05
 */
@Service
public class AdminRoleRelationServiceImpl extends ServiceImpl<AdminRoleRelationMapper, AdminRoleRelation> implements IAdminRoleRelationService {

}
