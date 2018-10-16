package com.xuanxinszp.ssjdemo.admin.assembler;

import com.google.common.collect.Lists;
import com.xuanxinszp.ssjdemo.admin.dto.AdminDto;
import com.xuanxinszp.ssjdemo.admin.entity.Admin;
import com.xuanxinszp.ssjdemo.admin.entity.Role;
import com.xuanxinszp.ssjdemo.common.bean.BaseStatus;
import com.xuanxinszp.ssjdemo.common.util.CollectionUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benson on 2018/3/9.
 */
public class AdminAssembler {

    public static Admin convertToEntity(AdminDto adminDto, Admin admin){
        if(admin==null){
            admin = new Admin();
        }
        admin.setId(adminDto.getId());
        admin.setUsername(adminDto.getUsername());
        admin.setMobile(adminDto.getMobile());
        admin.setEmail(adminDto.getEmail());
        admin.setRealname(adminDto.getRealname());
        admin.setStatus(null==adminDto.getStatus()? BaseStatus.DISABLED:BaseStatus.getProperty(BaseStatus.class, adminDto.getStatus()));
        return admin;
    }


    public static AdminDto convertToDto(Admin admin){
        if (null==admin) return null;

        String partnerId = null;
        String partnerName = null;
        AdminDto adminDto = new AdminDto(admin.getId(),admin.getUsername(),admin.getEmail(),admin.getPassword(),admin.getMobile(),admin.getRealname(),admin.getStatus().getCode(),admin.getCreateTime());
        // 角色
        if (CollectionUtil.isNotNil(admin.getRoles())) {
            List<Role> roles = Lists.newArrayList();
            roles.addAll(admin.getRoles());
            adminDto.setRoles(RoleAssembler.convertToDtoList(roles));
        } else {
            adminDto.setRoles(Lists.newArrayList());
        }

        return adminDto;
    }


    public static List<AdminDto> convertToDtoList(List<Admin> adminList){
        List<AdminDto> adminDtoList= new ArrayList<>();
        adminList.forEach(admin -> adminDtoList.add(convertToDto(admin)) );
        return adminDtoList;
    }

}
