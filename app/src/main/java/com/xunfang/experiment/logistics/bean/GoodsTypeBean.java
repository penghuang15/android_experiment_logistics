package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class GoodsTypeBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号
	private int code;//货物类型编号
	private String name;//货物类型名称
	
	public GoodsTypeBean(int id,int code,String name){
		this.id = id;
		this.code = code;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
