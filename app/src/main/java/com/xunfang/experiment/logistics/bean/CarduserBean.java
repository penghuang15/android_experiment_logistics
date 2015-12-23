package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class CarduserBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardsn;//卡序号,普通卡：00000001—99999999
	private String cardid8bit;//8位原始卡号	char	8	not		原始卡号
	private int state;//状态	integer		not		1 正常2 挂失3 销户
	
	private int custid;//客户编号	integer		not	主键	自增，从1开始
	private String name;//客户姓名	varchar	120	not		
	private String userid;//工号	varchar	20		U	
	private int postcode;//职位编号	integer		not	外键，企业职位信息表中的id	
	private int deptcode;//部门编号	integer		not	外键，企业部门信息表中的id	
	private String postname;//职位
	private String deptname;//部门

	public CarduserBean(int custid,String name,String userid,int postcode,int deptcode,
			String cardsn,String postname,String deptname,String cardid8bit,int state){
		this.custid = custid;
		this.cardsn = cardsn;
		this.name = name;
		this.userid = userid;
		this.postcode = postcode;
		this.deptcode = deptcode;
		this.cardid8bit = cardid8bit;
		this.state = state;
		this.postname = postname;
		this.deptname = deptname;
	}
	
	public String getCardsn() {
		return cardsn;
	}

	public void setCardsn(String cardsn) {
		this.cardsn = cardsn;
	}

	public String getCardid8bit() {
		return cardid8bit;
	}

	public void setCardid8bit(String cardid8bit) {
		this.cardid8bit = cardid8bit;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCustid() {
		return custid;
	}

	public void setCustid(int custid) {
		this.custid = custid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getPostcode() {
		return postcode;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public int getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(int deptcode) {
		this.deptcode = deptcode;
	}

	public String getPostname() {
		return postname;
	}

	public void setPostname(String postname) {
		this.postname = postname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	
}
