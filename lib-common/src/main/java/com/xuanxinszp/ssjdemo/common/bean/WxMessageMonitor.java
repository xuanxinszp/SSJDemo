package com.xuanxinszp.ssjdemo.common.bean;

import java.io.Serializable;

/**
 * 微信消息监听类
 * Created by Benson on 2018/7/20.
 */
public interface WxMessageMonitor extends Serializable {

    /** 获取发送时间戳**/
    Long getSendTimestamp();

    /** 获取设备id **/
    String getDeviceId();

    /** 获取接收方微信号，群聊的话为组id **/
    String getToWeixinNo();

    /** 获取发送方微信号 **/
    String getFromWeixinNo();

}
