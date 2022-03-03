package com.codingmore.service.impl;

import com.codingmore.model.Menu;
import com.codingmore.mapper.MenuMapper;
import com.codingmore.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author 石磊
 * @since 2022-03-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
