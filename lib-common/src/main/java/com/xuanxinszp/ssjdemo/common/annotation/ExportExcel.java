package com.xuanxinszp.ssjdemo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出export注解，
 * Created by 6212 on 2017/8/13.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportExcel {

    /**
     * 导出数据表头
     * @return
     */
    String headName() default "";

}
