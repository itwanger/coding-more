package com.codingmore.config;

import com.codingmore.component.DynamicSecurityService;
import com.codingmore.model.Resource;
import com.codingmore.service.IResourceService;
import com.codingmore.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * learn-admin-security模块相关配置
 * Created by zhanglei on 2019/11/9.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CodingmoreSecurityConfig extends CustomSecurityConfig {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IResourceService resourceService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> usersService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
                List<Resource> resources = resourceService.list();
                resources.forEach(item->{
                    map.put(item.getUrl(), new SecurityConfig(item.getResourceId() + ":" + item.getName()));
                });
                return map;
            }
        };
    }
}
