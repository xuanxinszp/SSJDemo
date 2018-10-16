package com.xuanxinszp.ssjdemo.common.dto;

/**
 * 底层可组装加载
 * Created by Benson on 2018/3/28.
 **/
public interface ILoad {

    /** 组装对象 */
    ILoad load(Object[] o);
}