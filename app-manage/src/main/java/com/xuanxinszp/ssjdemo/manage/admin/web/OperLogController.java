package com.xuanxinszp.ssjdemo.manage.admin.web;


import com.xuanxinszp.ssjdemo.common.permission.RequestPermission;
import com.xuanxinszp.ssjdemo.manage.admin.bean.AdminPermission;
import com.xuanxinszp.ssjdemo.manage.common.base.BaseController;
import com.xuanxinszp.ssjdemo.manage.menu.bean.BoardMenu;
import com.xuanxinszp.ssjdemo.menu.MenuMapping;
import com.xuanxinszp.ssjdemo.sys.bean.OperLogReq;
import com.xuanxinszp.ssjdemo.sys.dto.OperLogDto;
import com.xuanxinszp.ssjdemo.sys.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Benson on 2018/3/13.
 */
@Controller
@RequestMapping("/sys/operlog")
public class OperLogController extends BaseController {

    @Autowired
    private OperLogService pubOperLogService;


    @RequestPermission(value = AdminPermission.SYS_OPER_LOG)
    @MenuMapping(value = "系统操作日志", menu = BoardMenu.ADMIN, weight = 1)
    @GetMapping
    public String logs() {
        return "sys/sys_operlog_list";
    }


    @RequestPermission(value = AdminPermission.SYS_OPER_LOG)
    @PostMapping("/data")
    @ResponseBody
    public Page<OperLogDto> data(@RequestParam(value = "searchText", required = false) String searchText) {
        OperLogReq sysStatusReq = new OperLogReq();
        sysStatusReq.setQueryLike(searchText);
        sysStatusReq.setPageSize(getPageRequest().getPageSize());
        sysStatusReq.setPageNumber(getPageRequest().getPageNumber());
        Page<OperLogDto> pageData = pubOperLogService.findPage(sysStatusReq);
        return pageData;
    }
    
}
