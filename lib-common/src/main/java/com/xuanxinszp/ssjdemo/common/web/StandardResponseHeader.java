package com.xuanxinszp.ssjdemo.common.web;

/**
 * 标准响应头
 */
public class StandardResponseHeader {



    public static final ResponseHeader SUCCESS = ResponseHeader.newInstance(0, "成功");

    public static final ResponseHeader ERROR = ResponseHeader.newInstance(-1, "请求服务器异常");

    public static final ResponseHeader ERROR_TOCKEN = ResponseHeader.newInstance(50000, "请重新登录");
    public static final ResponseHeader ERROR_TOCKEN_EXPIRE = ResponseHeader.newInstance(50000, "请重新登录");
    public static final ResponseHeader ERROR_PARAM = ResponseHeader.newInstance(50001, "请求参数有误");

    public static final ResponseHeader USER_NOT_EXISTS = ResponseHeader.newInstance(50002, "会员不存在");

    public static final ResponseHeader USER_STATUS_ERROR = ResponseHeader.newInstance(50003, "会员状态异常");

    public static final ResponseHeader USER_PASSWORD_ERROR = ResponseHeader.newInstance(50004, "密码错误");

    public static final ResponseHeader USER_PASSWORD_NOTEQUAL = ResponseHeader.newInstance(50005, "两次密码不一致");

    public static final ResponseHeader REFERUSER_NOT_EXISTS = ResponseHeader.newInstance(50006, "推荐人不存在");

    public static final ResponseHeader TRADE_NOT_EXISTS = ResponseHeader.newInstance(50007, "交易不存在");

    public static final ResponseHeader ACCOUNT_IS_SETING = ResponseHeader.newInstance(50008, "提现账号已设置");

    public static final ResponseHeader WITHDRAW_NOT_OPEN = ResponseHeader.newInstance(50009, "没有到操作时间，不允许操作");

    public static final ResponseHeader WITHDARW_NOT_RIGHT = ResponseHeader.newInstance(50010, "提取数额有误");

    public static final ResponseHeader FILE_IS_EMPTY = ResponseHeader.newInstance(50011, "文件不能为空");

    public static final ResponseHeader NO_INVEST_NO_WITHDRAW = ResponseHeader.newInstance(50012, "没有投资没有任何东西可以提取");

    public static final ResponseHeader INVEST_TIME_NOT_FULL = ResponseHeader.newInstance(50013, "提年红未达到年限要求");


    public static final ResponseHeader USER_IS_EXISTS = ResponseHeader.newInstance(50014, "用户名已存在");

    public static final ResponseHeader NO_POWER_REFER = ResponseHeader.newInstance(50015, "不是代理商没有权限推荐注册");


    public static final ResponseHeader NO_AUTHORITY  = ResponseHeader.newInstance(50016, "没有设置密码权限");

    public static final ResponseHeader ID_NUM_ERROR  = ResponseHeader.newInstance(50017, "身份证错误");

    public static final ResponseHeader SEC_ERROR  = ResponseHeader.newInstance(50018, "密保回答错误");

}
