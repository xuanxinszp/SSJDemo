package com.xuanxinszp.ssjdemo.user.entity;




import com.xuanxinszp.ssjdemo.common.bean.UUIDEntity;
import com.xuanxinszp.ssjdemo.user.bean.UserStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * 
 * 会员账户表
 * 
 **/
@Entity
@Table(name = "u_user_account")
public class UserAccount extends UUIDEntity {

	/**用户id**/
//	@OneToOne
//	@JoinColumn(name = "user_id")
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "userName")
	private String userName;

	/**用户状态：10-正常，-10-注销，-20-冻结**/
	@Column(name = "status")
	private UserStatus status;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
