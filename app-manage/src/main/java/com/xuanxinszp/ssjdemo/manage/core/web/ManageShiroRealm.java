package com.xuanxinszp.ssjdemo.manage.core.web;

import com.xuanxinszp.ssjdemo.admin.entity.Admin;
import com.xuanxinszp.ssjdemo.admin.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现AuthorizingRealm接口用户认证
 * Created by Benson on 2018/5/21.
 */
public class ManageShiroRealm extends AuthorizingRealm {

    private final Logger logger = LoggerFactory.getLogger(ManageShiroRealm.class);

    @Autowired
    private AdminService adminService;

    /**
     * 添加角色权限和对应权限
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info(">>> Enter doGetAuthorizationInfo method.");
        // 获取登录用户名
        String username = (String) principals.getPrimaryPrincipal();
        // 查询用户
        Admin admin = adminService.getByUsername(username);
        // 添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        simpleAuthorizationInfo.setRoles(admin.getStringRoles());
        // 添加权限
        simpleAuthorizationInfo.setStringPermissions(admin.permissions());

        return simpleAuthorizationInfo;
    }


    /**
     * 用户认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info(">>> Enter doGetAuthenticationInfo method.");
        // 加这一步的目的是在Post请求的时候会先进认证，然后再到请求
        if (null == authenticationToken.getPrincipal()) {
            logger.info(">>> Current Principal is null.");
            return null;
        }
        // 获取用户信息
        String username = authenticationToken.getPrincipal().toString();
        Admin admin = adminService.getByUsername(username);
        if (null == admin) {
            // 这里返回后会报出对应异常
            return null;
        } else {
            // 这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, admin.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
