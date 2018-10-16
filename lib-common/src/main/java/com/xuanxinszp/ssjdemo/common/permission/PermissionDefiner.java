package com.xuanxinszp.ssjdemo.common.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionDefiner {

    String name();  // 名称

    String group(); // 所属分组

    RoleSpecification[] role() default {RoleSpecification.MODIFIABLE}; //默认可修改

}
