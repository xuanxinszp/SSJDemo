package com.xuanxinszp.ssjdemo.common.cache;

/**
 * 缓存名称常量定义
 * Created by Benson on 2018/3/28.
 */
public interface CacheConstant {

    // 登录用户tokenCache
    String TOKEN_SESSION_CACHE = "token_session_cache";

    // 登录用户token
    String USER_TOKEN_CACHE = "user_token";

    // 密码错误cache
    String PASSWORD_ERROR_CACHE = "password_error_cache";

    // 发送短信cache
    String SMS_CODE_CACHE = "sms_code_cache";

    // 回答密保成功的用户
    String ANSWER_CACHE = "answer_cache";

}
