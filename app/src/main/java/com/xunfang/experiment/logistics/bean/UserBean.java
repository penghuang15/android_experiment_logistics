package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//操作员编号
	private String loginname;//登录名
	private String password;//密码
	private String username;//用户名
	private int type;//操作员类型
	private int stationid;//所属站点
	private String stationname;//站点名称
	
	public UserBean(int id,String loginname,String password,String username,int type,int stationid,String stationname){
		this.id = id;
		this.loginname = loginname;
		this.password = password;
		this.username = username;
		this.type = type;
		this.stationid = stationid;
		this.stationname = stationname;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStationid() {
		return stationid;
	}

	public void setStationid(int stationid) {
		this.stationid = stationid;
	}

	public String getStationname() {
		return stationname;
	}

	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	
}
