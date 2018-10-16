package com.xuanxinszp.ssjdemo.manage.core.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.xuanxinszp.ssjdemo.manage.core.web.ManageShiroRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Benson on 2018/5/21.
 */
@Configuration
public class FreeMarkerConfig {

    private final Logger logger = LoggerFactory.getLogger(FreeMarkerConfig.class);

    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() {
        try {
            configuration.setSharedVariable("shiro", new ShiroTags());
        } catch (Exception e) {
            logger.error("设置shiro标签时异常：", e);
        }
    }

}
