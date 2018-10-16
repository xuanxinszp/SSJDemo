package com.xuanxinszp.ssjdemo.menu;

import java.lang.annotation.*;

/**
 * 菜单定义
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuDefiner {

    String name();

    String icon();

    int priority();
}
