package com.xuanxinszp.ssjdemo.manage.admin.bean;


import com.xuanxinszp.ssjdemo.common.permission.ButtonDefiner;
import com.xuanxinszp.ssjdemo.common.permission.PermissionDefiner;
import com.xuanxinszp.ssjdemo.common.permission.PermissionGroupDefiner;
import com.xuanxinszp.ssjdemo.common.permission.PermissionType;

/**
 * Created by Benson on 2018/3/7.
 */
@PermissionType
public class AdminPermission {

    @PermissionGroupDefiner(name = "系统管理")
    public static final String SYSTEM_GROUP = "admin";

    @PermissionDefiner(name = "管理员管理", group = SYSTEM_GROUP)
    public static final String ADMIN = "admin";

    @PermissionDefiner(name = "角色管理", group = SYSTEM_GROUP)
    public static final String ROLE = "role";


    @PermissionDefiner(name = "系统操作日志", group = SYSTEM_GROUP)
    public static final String SYS_OPER_LOG = "sys_oper_log";

    @PermissionGroupDefiner(name = "会员管理")
    public static final String USER_GROUP = "user_group";
    @PermissionDefiner(name = "会员列表", group = USER_GROUP)
    public static final String USER_LIST = "user_list";
    @ButtonDefiner(name = "解冻/冻结", code = "frozen", parent = USER_LIST, group = USER_GROUP)
    public static final String FROZEN = "frozen";
    @ButtonDefiner(name = "重置密码", code = "resetPassword", parent = USER_LIST, group = USER_GROUP)
    public static final String RESET_PASSWORD = "resetPassword";
    @PermissionDefiner(name = "日收益", group = USER_GROUP)
    public static final String STAT_DATE_LIST = "stat_date_list";
    @PermissionDefiner(name = "月收益", group = USER_GROUP)
    public static final String STAT_MONTH_LIST = "stat_month_list";


    @PermissionGroupDefiner(name = "交易管理")
    public static final String TRADE_GROUP = "trade_group";
    @PermissionDefiner(name = "投资列表", group = TRADE_GROUP)
    public static final String INVEST_LIST = "invest_list";
    @PermissionDefiner(name = "提现列表", group = TRADE_GROUP)
    public static final String WITHDRAW_LIST = "withdraw_list";

    @PermissionGroupDefiner(name = "运营管理")
    public static final String ARTICLE_GROUP = "article_group";
    @PermissionDefiner(name = "分红配置", group = ARTICLE_GROUP)
    public static final String BONUS_CONFIG = "bonus_config";
    @PermissionDefiner(name = "文章列表", group = ARTICLE_GROUP)
    public static final String ARTICLE = "article";

    @PermissionDefiner(name = "跑马灯", group = ARTICLE_GROUP)
    public static final String ROLL_MSG = "rollmsg";

    @PermissionDefiner(name = "平台收款账户", group = ARTICLE_GROUP)
    public static final String PLATFORM_ACCOUNT = "platform_account";

    @PermissionDefiner(name = "反馈列表", group = ARTICLE_GROUP)
    public static final String FEED_BACK = "feed_back";

    @PermissionGroupDefiner(name = "统计管理")
    public static final String STAT_GROUP = "stat_group";
    @PermissionDefiner(name = "币池总统计", group = STAT_GROUP)
    public static final String STAT_TOTAL = "stat_total";

    @PermissionDefiner(name = "每日统计", group = STAT_GROUP)
    public static final String STAT_DAY = "stat_day";

    @PermissionDefiner(name = "每月统计", group = STAT_GROUP)
    public static final String STAT_MONTH = "stat_month";


}
