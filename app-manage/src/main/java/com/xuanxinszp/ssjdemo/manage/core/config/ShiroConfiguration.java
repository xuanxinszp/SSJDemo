package com.xuanxinszp.ssjdemo.manage.core.config;

import com.google.common.collect.Maps;
import com.xuanxinszp.ssjdemo.manage.core.web.ManageShiroRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Shiro 配置
 * Created by Benson on 2018/5/21.
 */
@Configuration
public class ShiroConfiguration {

    // 将验证方式加入容器
    @Bean
    public ManageShiroRealm manageShiroRealm() {
        ManageShiroRealm manageShiroRealm = new ManageShiroRealm();
        return manageShiroRealm;
    }

    // 权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(manageShiroRealm());
        return securityManager;
    }

    // Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> map = Maps.newHashMap();
        //map.put("/logout","logout");// 登出
        map.put("/**","authc");// 对所有用户认证
        shiroFilterFactoryBean.setLoginUrl("/login");// 登录
        shiroFilterFactoryBean.setSuccessUrl("/");// 首页
        // 错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    // 加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
