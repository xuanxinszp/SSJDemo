package com.xuanxinszp.ssjdemo.api;

import com.xuanxinszp.ssjdemo.common.web.CommonApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Benson on 2018/2/27.
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories("com.xuanxinszp.ssjdemo.**.jpa")
@ComponentScan(basePackages = "com.xuanxinszp.ssjdemo")
@EntityScan("com.xuanxinszp.ssjdemo.**.entity")
@EnableAsync
public class Application extends CommonApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Application.class, WebConfig.class).run(args);
    }


}
