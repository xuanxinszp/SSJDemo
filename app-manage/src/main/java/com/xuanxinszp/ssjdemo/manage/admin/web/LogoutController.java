package com.xuanxinszp.ssjdemo.manage.admin.web;

import com.xuanxinszp.ssjdemo.common.helper.CookieHelper;
import com.xuanxinszp.ssjdemo.common.util.WebUtil;
import com.xuanxinszp.ssjdemo.common.web.RequestContext;
import com.xuanxinszp.ssjdemo.common.web.session.SingleSession;
import com.xuanxinszp.ssjdemo.manage.core.cache.AdminSingleSession;
import com.xuanxinszp.ssjdemo.sys.annotation.SystemLogAnnotation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private AdminSingleSession adminSingleSession;

    @GetMapping
    @SystemLogAnnotation(desc = "用户登出操作")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        adminSingleSession.setToUnavaliable(adminSingleSession.getSessionId());
        adminSingleSession.cleanThreadSession();
        Cookie cookie = new Cookie(SingleSession.COOKIE_SESSION_ID, "");
        cookie.setMaxAge(0);
        CookieHelper.addCookie(response, cookie);

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        String redirectUrl = WebUtil.getDomainBasePath() +"/"+ RequestContext.getModuleProperties().loginUri;
        return "redirect:" + redirectUrl;
    }

}
