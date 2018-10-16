package com.xuanxinszp.ssjdemo.api.user.web;

import com.xuanxinszp.ssjdemo.common.dto.AppVoid;
import com.xuanxinszp.ssjdemo.common.web.HalfOpen;
import com.xuanxinszp.ssjdemo.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 6212 on 2018/10/15.
 * author  shengzhipeng
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;

    @ApiOperation(value="用户反馈信息", notes="用户反馈信息。", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/test")
    @HalfOpen
    public AppVoid test() {
        userService.saver();
        return AppVoid.getInstance();
    }
}
