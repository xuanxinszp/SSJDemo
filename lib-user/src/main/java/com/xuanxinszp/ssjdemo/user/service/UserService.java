package com.xuanxinszp.ssjdemo.user.service;


import com.xuanxinszp.ssjdemo.common.util.ServiceAssert;
import com.xuanxinszp.ssjdemo.common.web.StandardResponseHeader;
import com.xuanxinszp.ssjdemo.sys.entity.OperLog;
import com.xuanxinszp.ssjdemo.sys.jpa.OperLogRepository;
import com.xuanxinszp.ssjdemo.user.dto.UserEvent;
import com.xuanxinszp.ssjdemo.user.entity.User;
import com.xuanxinszp.ssjdemo.user.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
*
szp
*/
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OperLogRepository operLogRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public User findOne(Long id) {
		ServiceAssert.isTrue(Objects.nonNull(id), StandardResponseHeader.ERROR_PARAM);
		return userRepository.findOne(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public void saver() {
		OperLog operLog = new OperLog();
		operLog.setMethod("GET");
		operLog.setModule("aaa");
		operLog = operLogRepository.save(operLog);
		//保存用户完成以后需要做其他事情，这里用观察者模式来实现，使用spring事件监听机制
		applicationEventPublisher.publishEvent(new UserEvent(operLog));
	}


}
