package com.xuanxinszp.ssjdemo.manage.core.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Benson on 2018/2/27.
 */
@Controller
public class IndexController {


    @GetMapping
    public String index() {
        return "/page/index";
    }

    @GetMapping("404")
    public String notFound() {
        return "/page/404";
    }

    @GetMapping("forbidden")
    public String forbidden() {
        return "/page/forbidden";
    }

    @GetMapping("forceExit")
    public String forceExit() {
        return "/page/forceExit";
    }

    @GetMapping("/admin/welcome")
    public String welcome() {
        return "/page/welcome";
    }

}
