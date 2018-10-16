package com.xuanxinszp.ssjdemo.user.bean;

import com.xuanxinszp.ssjdemo.common.bean.JpaProperty;

/**
 * Created by 6212 on 2018/8/27.
 * author  shengzhipeng
 */
public class UserStatus extends JpaProperty {

    public static final UserStatus NORMAL =  newInstance(UserStatus.class, 10, "正常");
    public static final UserStatus STOP =  newInstance(UserStatus.class, -20, "冻结");
    public static final UserStatus DEL =  newInstance(UserStatus.class, -10, "注销");
}
