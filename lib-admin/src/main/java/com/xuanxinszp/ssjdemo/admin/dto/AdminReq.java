package com.xuanxinszp.ssjdemo.admin.dto;

import com.xuanxinszp.ssjdemo.common.dto.PageDto;

/**
 * Created by Benson on 2018/3/9.
 */
public class AdminReq extends PageDto {

    private String username;

    private String mobile;

    private String partnerId;

    private String partnerName; // 合作机构名称


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
