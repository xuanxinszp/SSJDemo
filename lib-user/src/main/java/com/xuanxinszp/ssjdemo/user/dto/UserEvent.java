package com.xuanxinszp.ssjdemo.user.dto;

import com.xuanxinszp.ssjdemo.sys.entity.OperLog;
import com.xuanxinszp.ssjdemo.user.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by 6212 on 2018/10/16.
 * author  shengzhipeng
 */
public class UserEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5865656070585817565L;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserEvent(OperLog source) {
        super(source);
    }

}
