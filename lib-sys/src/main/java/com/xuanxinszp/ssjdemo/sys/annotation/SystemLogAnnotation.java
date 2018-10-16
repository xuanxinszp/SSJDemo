package com.xuanxinszp.ssjdemo.sys.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 6246 lizl
 * @Description:
 * @Date Create on  2018/5/17
 * @Modified by
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLogAnnotation {


    String desc() default "";

    String value() default "";



}
