package com.xuanxinszp.ssjdemo.admin.service;

import com.google.common.collect.Lists;

import com.xuanxinszp.ssjdemo.admin.assembler.AdminAssembler;
import com.xuanxinszp.ssjdemo.admin.dto.AdminDto;
import com.xuanxinszp.ssjdemo.admin.entity.Admin;
import com.xuanxinszp.ssjdemo.admin.entity.Role;
import com.xuanxinszp.ssjdemo.admin.jpa.AdminRepository;
import com.xuanxinszp.ssjdemo.admin.jpa.RoleRepository;
import com.xuanxinszp.ssjdemo.admin.dto.AdminReq;
import com.xuanxinszp.ssjdemo.common.bean.BaseStatus;
import com.xuanxinszp.ssjdemo.common.util.EncryptionUtil;
import com.xuanxinszp.ssjdemo.common.util.RepositoryUtil;
import com.xuanxinszp.ssjdemo.common.util.StringUtil;
import com.xuanxinszp.ssjdemo.common.web.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Benson on 2018/3/7.
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;



    public Page<Admin> getAll(int page) {
        return adminRepository.findAll(RepositoryUtil.getPageable(page));
    }

    public Admin get(String id) {
        return adminRepository.findOne(id);
    }

    public AdminDto findOneDto(String id) {
        Assert.isTrue(StringUtil.isNotBlank(id), "管理员ID不能为空");
        return AdminAssembler.convertToDto(adminRepository.findOne(id));
    }

    public Admin getByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin getByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin getByMobile(String mobile) {
        return adminRepository.findByMobile(mobile);
    }

    @Transactional(rollbackFor = Exception.class)
    public Admin save(AdminDto adminDto, String[] roleIds) {
        Admin admin;
        if (StringUtils.isEmpty(adminDto.getId())) {
            admin = AdminAssembler.convertToEntity(adminDto, null);
            admin.setPassword(EncryptionUtil.encryptPassword(adminDto.getPassword()));
        } else {
            admin = get(adminDto.getId());
            admin = AdminAssembler.convertToEntity(adminDto, admin);
            // 删除管理员之前所包含的所有角色
            admin.setRoles(null);
            // 如果密码不为空，且与原密码不同，则加密后存储
            if (!StringUtils.isEmpty(adminDto.getPassword()) && !admin.getPassword().equals(EncryptionUtil.encryptPassword(adminDto.getPassword()))) {
                admin.setPassword(EncryptionUtil.encryptPassword(adminDto.getPassword()));
            }
        }

        if (roleIds != null && roleIds.length > 0) {
            List<Role> roles = roleRepository.findByIds(Arrays.asList(roleIds));
            admin.setRoles(new HashSet<>(roles));
        } else {
            admin.setRoles(new HashSet<>());
        }
        return adminRepository.save(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    public Admin update(Admin admin) {
        Admin oa = adminRepository.findOne(admin.getId());
        oa.setRealname(admin.getRealname());
        oa.setMobile(admin.getMobile());
        return adminRepository.save(oa);
    }

    @Transactional
    public void delete(String id) {
        adminRepository.delete(id);
    }

    @Transactional
    public void updatePwd(String id, String originalpwd, String newpwd) {
        Assert.isTrue(StringUtil.isNotNil(id), "用户id不能为空");
        Admin oa = adminRepository.findOne(id);
        if (!oa.getPassword().equals(EncryptionUtil.encryptPassword(originalpwd))) {
            throw new AppException("原密码错误");
        }
        if (originalpwd.equals(newpwd)) {
            throw new AppException("新密码不能和原密码相同");
        }

        oa.setPassword(EncryptionUtil.encryptPassword(newpwd));
        adminRepository.save(oa);
    }

    @Transactional
    public String updateStatus(String id,Boolean enable){
        Admin admin = adminRepository.findOne(id);
        if(Objects.isNull(admin)){
            return null;
        }
        admin.setStatus(enable? BaseStatus.NORMAL: BaseStatus.DISABLED);
        return adminRepository.save(admin).getId();
    }


    public Page<AdminDto> findPage(AdminReq adminReq) {
        PageRequest pageRequest = new PageRequest(adminReq.getPageNumber(), adminReq.getPageSize(), new Sort(Sort.Direction.DESC, "createTime"));
        Specification<Admin> spec = (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            if (StringUtil.isNotNil(adminReq.getUsername())) {
                Path<String> username = root.get("username");
                predicates.add(builder.like(username, "%" + adminReq.getUsername() + "%", '/'));
            }
            if (StringUtil.isNotNil(adminReq.getMobile())) {
                Path<String> mobile = root.get("mobile");
                predicates.add(builder.like(mobile, adminReq.getMobile() + "%", '/'));
            }


            query.orderBy(builder.desc(root.get("createTime")));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<Admin> adminPage= adminRepository.findAll(spec,pageRequest);
        Page<AdminDto> adminDtoPage = new PageImpl<>(AdminAssembler.convertToDtoList(adminPage.getContent()), pageRequest,adminPage.getTotalElements());
        return adminDtoPage;
    }
    
}
