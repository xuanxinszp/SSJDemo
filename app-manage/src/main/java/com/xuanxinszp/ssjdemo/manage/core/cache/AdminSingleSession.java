package com.xuanxinszp.ssjdemo.manage.core.cache;

import com.xuanxinszp.ssjdemo.admin.entity.Admin;
import com.xuanxinszp.ssjdemo.common.web.session.SingleSession;
import org.springframework.stereotype.Component;

@Component
public class AdminSingleSession extends SingleSession<Admin> {

    @Override
    public Class<Admin> userClass() {
        return Admin.class;
    }

}
