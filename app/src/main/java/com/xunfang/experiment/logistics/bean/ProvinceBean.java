package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：省份/直辖市Bean</p>
 
 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author sas
 */
public class ProvinceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;//序号
	
	//省份/直辖市编号
	private int code;
	
	//省份/直辖市名称
	private String name;
	
	public ProvinceBean(){
		this(-1,-1, null);
	}
	public ProvinceBean(int id,int code,String name){
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
		ProvinceBean province = (ProvinceBean)o;
		return (province.code == this.code && (province.name.equals(this.name)));
	}
}
