package com.codingmore.mapper;

import com.codingmore.dto.UsersPageQueryParam;
import com.codingmore.model.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 石磊
 * @since 2021-09-12
 */
public interface UsersMapper extends BaseMapper<Users> {
    IPage<Users> findByPage(IPage<Users> page, @Param(Constants.WRAPPER) Wrapper<UsersPageQueryParam> wrapper);
}
