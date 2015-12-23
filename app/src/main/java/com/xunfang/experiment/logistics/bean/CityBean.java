package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

/**
 * <p>Title：3G物联网开发平台</p>
 * <p>Description：城市Bean</p>
 
 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author 3G终端应用开发组 
 */
public class CityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;//序号

	//城市编号
	private int code;
	
	//城市名称
	private String name;
	
	public CityBean(){
		this(-1, -1, null);
	}
	
	public CityBean(int id,int code,String name){
		this.id = id;
		this.code= code;
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
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		CityBean city = (CityBean)o;
		return (city.code == this.code && (city.name.equals(this.name)));
	}
}
