package com.xuanxinszp.ssjdemo.api.user.dto;


import com.xuanxinszp.ssjdemo.common.dto.Dto;
import com.xuanxinszp.ssjdemo.user.entity.User;


/**
 * Token会话信息
 * Created by Benson on 2018/3/1.
 */
public class TokenSessionDto extends Dto {

    // 令牌信息
    private String token;

    // 用户信息
    private User user;



    public TokenSessionDto() {
    }

    public TokenSessionDto(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
