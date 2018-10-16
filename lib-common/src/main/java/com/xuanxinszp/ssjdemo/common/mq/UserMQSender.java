package com.xuanxinszp.ssjdemo.common.mq;

import org.springframework.stereotype.Component;


@Component
public class UserMQSender extends MQSender {

    public static void sendToUser(String user){
        send(MQConstant.USER,user);
    }

    public static void sendToUser(String user,long timeout){
        send(MQConstant.USER, user, timeout);
    }


}
