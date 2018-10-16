package com.xuanxinszp.ssjdemo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 敏感词属性注解
 * 用于标识对象属性
 * Created by Benson on 2018/7/19.
 */
@Target(value = {ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveLexiconProperty {

}
