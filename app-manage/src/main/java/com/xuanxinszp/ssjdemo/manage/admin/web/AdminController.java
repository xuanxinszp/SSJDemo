package com.xuanxinszp.ssjdemo.manage.admin.web;

import com.xuanxinszp.ssjdemo.admin.dto.AdminDto;
import com.xuanxinszp.ssjdemo.admin.dto.AdminReq;
import com.xuanxinszp.ssjdemo.admin.entity.Admin;
import com.xuanxinszp.ssjdemo.admin.service.AdminService;
import com.xuanxinszp.ssjdemo.admin.service.RoleService;
import com.xuanxinszp.ssjdemo.common.permission.RequestPermission;
import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.common.util.WebUtil;
import com.xuanxinszp.ssjdemo.common.web.AppException;
import com.xuanxinszp.ssjdemo.common.web.JsonResponse;
import com.xuanxinszp.ssjdemo.manage.admin.bean.AdminPermission;
import com.xuanxinszp.ssjdemo.manage.admin.dto.UpdatePwdReq;
import com.xuanxinszp.ssjdemo.manage.common.base.BaseController;
import com.xuanxinszp.ssjdemo.manage.core.cache.AdminSingleSession;
import com.xuanxinszp.ssjdemo.manage.menu.bean.BoardMenu;
import com.xuanxinszp.ssjdemo.menu.MenuMapping;
import com.xuanxinszp.ssjdemo.sys.annotation.SystemLogAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by Benson on 2018/3/7.
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminSingleSession adminSingleSession;


    @RequestPermission(value = AdminPermission.ADMIN)
    @MenuMapping(value = "管理员管理", menu = BoardMenu.ADMIN, weight = 10)
    @GetMapping
    public String admins() {
        return "admin/list";
    }

    /**
     * 前往新增/编辑页面
     * @param id
     * @param model
     * @return
     */
    @RequestPermission(value = AdminPermission.ADMIN)
    @GetMapping("/edit/{id}")
    //@SystemLogAnnotation(desc = "前往编辑页面")
    public String edit(@PathVariable String id, Model model) {
        if (!"new".equals(id)) {
            AdminDto admin = adminService.findOneDto(id);
            if (null==admin) throw new AppException("无效的ID值，找不到目标对象");
            model.addAttribute("admin", admin);
            model.addAttribute("myRoles", admin.getRoles());
        }else{
            model.addAttribute("myRoles",  new ArrayList<>());
        }
        model.addAttribute("roles", roleService.getAvailable());
        //model.addAttribute("status", BaseStatus.getPropertyList(BaseStatus.class));
        return "admin/edit";
    }

    @RequestPermission(value = AdminPermission.ADMIN)
    @PostMapping("/save")
    @ResponseBody
    @SystemLogAnnotation(desc = "保存管理员信息")
    public JsonResponse save(AdminDto adminDto, String[] roleIds) {
        if (StringUtil.isNil(adminDto.getId())) {
            Admin target = adminService.getByUsername(adminDto.getUsername());
            if (target != null) {
                logger.info("该用户名经存在");
                return WebUtil.errorJsonResponse("该用户名经存在");
            }
            target = adminService.getByMobile(adminDto.getMobile());
            if (target != null) {
                logger.info("该手机号已经存在");
                return WebUtil.errorJsonResponse("该手机号已经存在");
            }
        }

        adminService.save(adminDto, roleIds);
        return WebUtil.successJsonResponse("保存成功");
    }

    @RequestPermission(value = AdminPermission.ADMIN)
    @PostMapping("/delete/{id}")
    @ResponseBody
    @SystemLogAnnotation(desc = "删除用户信息")
    public JsonResponse delete(@PathVariable String id) {
        if (StringUtil.isNotNil(id)) {
            adminService.delete(id);
            //TODO 清除被删除用的的redis信息
        }
        return WebUtil.successJsonResponse("管理员删除成功");
    }

    @RequestPermission(value = AdminPermission.ADMIN)
    @PostMapping("/data")
    @ResponseBody
    public Page<AdminDto> data(AdminReq adminReq) {
        adminReq.setPageSize(getPageRequest().getPageSize());
        adminReq.setPageNumber(getPageRequest().getPageNumber());
        Page<AdminDto> pageData = adminService.findPage(adminReq);
        return pageData;
    }





    /**
     * 密码修改页面
     * @return
     */
    @GetMapping("/update/pwd")
    public String updatePwdPage() {
        return "admin/updatePwd";
    }

    /**
     * 修改用户密码
     * @param updatePwdReq
     */
    @PostMapping("/update/pwd")
    @ResponseBody
    @SystemLogAnnotation(desc = "修改用户密码")
    public JsonResponse updatePwd(UpdatePwdReq updatePwdReq) {
        if (null==updatePwdReq) {
            return WebUtil.errorJsonResponse("参数不能为空");
        }
        if (StringUtil.isNil(updatePwdReq.getOriginalPwd()) || StringUtil.isNil(updatePwdReq.getNewPwd()) || StringUtil.isNil(updatePwdReq.getConfirmPwd())) {
            return WebUtil.errorJsonResponse("原密码、新密码和确认密码不能为空");
        }
        if (!updatePwdReq.getNewPwd().equals(updatePwdReq.getConfirmPwd())) {
            return WebUtil.errorJsonResponse("新密码和确认密码不匹配");
        }

        Admin admin = adminSingleSession.getUser();
        if (null == admin) {
            return WebUtil.errorJsonResponse("用户信息不能为空");
        }

        adminService.updatePwd(admin.getId(),updatePwdReq.getOriginalPwd(),updatePwdReq.getNewPwd());

        return WebUtil.successJsonResponse("修改成功!");
    }


    /**
     * 激活管理员/更改状态
     * @param id
     * @param enable
     * @return
     */
    @RequestPermission(value = AdminPermission.ADMIN)
    @GetMapping("/activate/{id}/{enable}")
    @ResponseBody
    @SystemLogAnnotation(desc = "激活管理员/更改状态")
    public String activate(@PathVariable String id,@PathVariable Boolean enable){
        String returnId = adminService.updateStatus(id, enable);
        if(returnId == null){
            return "不存在该管理员数据";
        }
        return "操作成功";
    }

}
