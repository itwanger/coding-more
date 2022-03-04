package com.codingmore.mapper;

import com.codingmore.model.AdminRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codingmore.model.Resource;
import com.codingmore.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
public interface AdminRoleRelationMapper extends BaseMapper<AdminRoleRelation> {
    /**
     * 批量插入用户角色关系
     */
    int insertList(@Param("list") List<AdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用于所有角色
     */
    List<Role> getRoleList(@Param("usersId") Long usersId);



    /**
     * 获取用户所有可访问资源
     */
    List<Resource> getResourceList(@Param("usersId") Long usersId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
