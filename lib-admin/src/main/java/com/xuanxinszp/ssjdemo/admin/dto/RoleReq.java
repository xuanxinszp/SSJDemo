package com.xuanxinszp.ssjdemo.admin.dto;

import com.xuanxinszp.ssjdemo.common.dto.PageDto;

/**
 * Created by Benson on 2018/3/9.
 */
public class RoleReq extends PageDto {

    private String queryLike;

    public String getQueryLike() {
        return queryLike;
    }

    public void setQueryLike(String queryLike) {
        this.queryLike = queryLike;
    }
}
