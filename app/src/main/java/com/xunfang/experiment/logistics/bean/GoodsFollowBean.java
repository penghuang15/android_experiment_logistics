package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class GoodsFollowBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号//
	private String ordernumber;//订单号
	private int currstationid;//当前站点编号
	private String currentstation;//当前站点
	private String information;//处理信息
	private String handletime;//处理时间	
	private int operaterid;//操作员编号
	private String operatername;//操作员用户名
	private int state;//是否已签收
	
	public GoodsFollowBean(int id,String ordernumber,int currentstationid,String currentstation,String information,
			String handletime,int operaterid,String operatername,int state){
		this.id = id;
		this.ordernumber = ordernumber;
		this.currstationid = currentstationid;
		this.currentstation = currentstation;
		this.information = information;
		this.handletime = handletime;
		this.operaterid = operaterid;
		this.operatername = operatername;
		this.state = state;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getOrdernumber() {
		return ordernumber;
	}


	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}


	public int getCurrstationid() {
		return currstationid;
	}


	public void setCurrstationid(int currstationid) {
		this.currstationid = currstationid;
	}


	public String getCurrentstation() {
		return currentstation;
	}


	public void setCurrentstation(String currentstation) {
		this.currentstation = currentstation;
	}


	public String getInformation() {
		return information;
	}


	public void setInformation(String information) {
		this.information = information;
	}


	public String getHandletime() {
		return handletime;
	}


	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}

	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public int getOperaterid() {
		return operaterid;
	}


	public void setOperaterid(int operaterid) {
		this.operaterid = operaterid;
	}


	public String getOperatername() {
		return operatername;
	}


	public void setOperatername(String operatername) {
		this.operatername = operatername;
	}
	
}
