package com.xuanxinszp.ssjdemo.manage.token;

import java.lang.annotation.*;

/**
 * Created by 6212 on 2018/9/28.
 * author  shengzhipeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {
    boolean save() default false;
    boolean remove() default false;
}
