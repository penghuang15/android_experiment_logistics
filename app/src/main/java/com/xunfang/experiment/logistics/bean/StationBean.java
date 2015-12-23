package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class StationBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//站点编号
	private String name;//站点名称
	private int city_code;//所属城市编号
	private String city_name;//所属城市名称
	private int province_code;//所属省份编号
	private String province_name;//所属省份名称
	
	public StationBean(int id,String name,int city_code,String city_name,int province_code,String province_name){
		this.id = id;
		this.name = name;
		this.city_code = city_code;
		this.city_name = city_name;
		this.province_code = province_code;
		this.province_name = province_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCity_code() {
		return city_code;
	}

	public void setCity_code(int cityCode) {
		city_code = cityCode;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String cityName) {
		city_name = cityName;
	}

	public int getProvince_code() {
		return province_code;
	}

	public void setProvince_code(int provinceCode) {
		province_code = provinceCode;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String provinceName) {
		province_name = provinceName;
	}
	
}
