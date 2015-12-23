package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class GoodsInfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type_id;//类型序号
	private int type_code;//货物类型编号
	private String type_name;//货物类型名称
	private int id;//货物编号
	private String name;//货物名称
	private int num;//货物数量
	
	public GoodsInfoBean(int type_id,int type_code,String type_name,int id,String name){
		this(type_id, type_code, type_name, id, name, 0);
	}
	public GoodsInfoBean(int type_id,int type_code,String type_name,int id,String name,int num){
		this.type_id = type_id;
		this.type_code = type_code;
		this.type_name = type_name;
		this.id = id;
		this.name = name;
		this.num = num;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int typeId) {
		type_id = typeId;
	}

	public int getType_code() {
		return type_code;
	}

	public void setType_code(int typeCode) {
		type_code = typeCode;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String typeName) {
		type_name = typeName;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
