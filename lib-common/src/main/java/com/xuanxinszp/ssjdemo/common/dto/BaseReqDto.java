package com.xuanxinszp.ssjdemo.common.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 *  请求参数
 * @author 6213
 * @date 2018/4/13
 */
public class BaseReqDto extends Dto {

    @NotBlank(message = "参数签名不能为空")
    private String sign;

    @NotBlank(message = "随机字符串不能为空")
    private String nonceStr;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
}
