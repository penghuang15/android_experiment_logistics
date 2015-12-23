package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

public class CardinfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardsn;//卡序号,普通卡：00000001—99999999
	private String cardid8bit;//8位原始卡号	char	8	not		原始卡号
	private int cardtype;//1：13.56M IC卡  2：条码
	private int state;//状态	integer		not		1 正常2取消
	
	public CardinfoBean(String cardsn,String cardid8bit,int cardtype,int state){
		this.cardsn = cardsn;
		this.cardid8bit = cardid8bit;
		this.cardtype = cardtype;
		this.state = state;
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

	public int getCardtype() {
		return cardtype;
	}

	public void setCardtype(int cardtype) {
		this.cardtype = cardtype;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
