package com.xuanxinszp.ssjdemo.common.enums;

/**
 * OSS 目录枚举
 * Created by Benson on 2018/6/1.
 */
public enum OssDirType {

    ARTICLE("article", "文章图片目录"),
    FILE("file", "前端上传文件目录");


    private String dir;     // 目录
    private String desc;    // 描述

    OssDirType(String dir, String desc) {
        this.dir = dir;
        this.desc = desc;
    }

    public String getDir() {
        return dir;
    }

    public String getDesc() {
        return desc;
    }
}
