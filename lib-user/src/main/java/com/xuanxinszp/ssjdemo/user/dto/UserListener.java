package com.xuanxinszp.ssjdemo.user.dto;

import com.xuanxinszp.ssjdemo.common.util.JsonUtil;
import com.xuanxinszp.ssjdemo.sys.entity.OperLog;
import com.xuanxinszp.ssjdemo.sys.jpa.OperLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

/**
 * Created by 6212 on 2018/10/15.
 * author  shengzhipeng
 */
@Component
public class UserListener implements SmartApplicationListener {

    @Autowired
    private OperLogRepository operLogRepository;
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == UserEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == OperLog.class;
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        OperLog operLog = (OperLog)event.getSource();

        System.out.println("监听到的消息："+ JsonUtil.beanToJson(operLog));
        operLog.setModule(null);
        operLogRepository.save(operLog);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
