package com.xuanxinszp.ssjdemo.manage.menu.bean;


import com.xuanxinszp.ssjdemo.menu.MenuDefiner;
import com.xuanxinszp.ssjdemo.menu.MenuType;

/**
 * 导航菜单（一级菜单）
 * Created by Benson on 2018/3/7.
 */
@MenuType
public class BoardMenu {

    @MenuDefiner(name = "系统管理", icon = "fa fa fa-cog", priority = 1)
    public static final String ADMIN = "admin";

    @MenuDefiner(name = "会员管理", icon = "fa fa fa-cog", priority = 2)
    public static final String USER = "user";

    @MenuDefiner(name = "交易管理", icon = "fa fa fa-cog", priority = 3)
    public static final String TRADE = "trade";

    @MenuDefiner(name = "运营管理", icon = "fa fa fa-cog", priority = 4)
    public static final String ARTICLE = "article";

    @MenuDefiner(name = "数据统计", icon = "fa fa fa-cog", priority = 5)
    public static final String STAT = "stat";

}
