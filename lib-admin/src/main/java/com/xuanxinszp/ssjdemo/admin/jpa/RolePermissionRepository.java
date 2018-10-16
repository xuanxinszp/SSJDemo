package com.xuanxinszp.ssjdemo.admin.jpa;

import com.xuanxinszp.ssjdemo.admin.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Benson on 2018/3/7.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, String>, JpaSpecificationExecutor<RolePermission> {

    @Modifying
    @Query(value = "delete from RolePermission where role.id = ?")
    void deleteByRoleId(String roleId);
}
