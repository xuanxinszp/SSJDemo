package com.xuanxinszp.ssjdemo.common.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 按钮定义
 * Created by Benson on 2018/5/18.
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ButtonDefiner {

    /**
     * 按钮名称
     * @return
     */
    String name();

    /**
     * 权限编码
     * @return
     */
    String code();

    /**
     * 所属上级
     * @return
     */
    String parent();

    /**
     * 所属分组
     * @return
     */
    String group();

}
