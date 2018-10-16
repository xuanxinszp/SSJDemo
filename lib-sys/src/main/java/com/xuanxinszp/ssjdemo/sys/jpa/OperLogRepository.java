package com.xuanxinszp.ssjdemo.sys.jpa;



import com.xuanxinszp.ssjdemo.sys.entity.OperLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 */
public interface OperLogRepository extends JpaRepository<OperLog, String>, JpaSpecificationExecutor<OperLog> {
}
