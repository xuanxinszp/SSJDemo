package com.xuanxinszp.ssjdemo.common.permission;

/**
 * 按钮
 * Created by Benson on 2018/5/18.
 */
public class Button {

    private String name;    //按钮名称
    private String code;    //权限编码


    public Button(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
