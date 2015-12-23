package com.xunfang.experiment.logistics.bean;

import java.io.Serializable;

import android.content.Intent;

public class FunctionMenu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int icon;//图标
	private String func_name;//功能名称
	private Intent intent;//Intent

	public FunctionMenu(int icon,String func_name,Intent intent){
		this.icon = icon;
		this.func_name = func_name;
		this.intent = intent;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String funcName) {
		func_name = funcName;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	
}
