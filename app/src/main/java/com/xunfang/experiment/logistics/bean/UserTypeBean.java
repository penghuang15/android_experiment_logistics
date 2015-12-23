package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class UserTypeBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号
	private String typename;//操作员类型名称
	
	public UserTypeBean(int id,String typename){
		this.id = id;
		this.typename = typename;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	
}
