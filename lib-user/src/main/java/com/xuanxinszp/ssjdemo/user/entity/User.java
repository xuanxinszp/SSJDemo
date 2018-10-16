package com.xuanxinszp.ssjdemo.user.entity;




import com.xuanxinszp.ssjdemo.common.bean.AutoEntity;
import com.xuanxinszp.ssjdemo.user.bean.UserStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 
 * 会员表
 * 
 **/
@Entity
@Table(name = "u_user")
public class User extends AutoEntity {

	/**
	 * 账号,作为第一登录凭证
	 **/
	@Column(name = "user_name")
	private String userName;

	/**
	 * 手机号
	 **/
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 登录密码
	 **/
	@Column(name = "password")
	private String password;

	/**
	 * 用户状态：10-正常，-10-注销，-20-冻结
	 **/
	@Column(name = "status")
	private UserStatus status;

	/**
	 * 姓名
	 **/
	@Column(name = "true_name")
	private String trueName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
}
