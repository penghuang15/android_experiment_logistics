package com.xunfang.experiment.logistics.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunfang.experiment.logistics.R;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：自定义的子业务显示控件</p>

 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author sas 
 */
public class Sub_sys_main_common extends LinearLayout{
	//定义控件
    private TextView  textview0;
    private TextView  textview1;
    private TextView  textview2;
    private TextView  textview3;
    private TextView  textview4;
    private TextView  textview5;
  
    public Sub_sys_main_common(Context context) {  
        this(context, null);  
    }
  
    public Sub_sys_main_common(Context context,AttributeSet attrs) {  
        super(context,attrs);  
        // 导入布局  
        LayoutInflater.from(context).inflate(R.layout.sub_sys_main_common, this, true); 
        //获得控件实例
        textview0 = (TextView) findViewById(R.id.sub_sys_main_common_cardcodestr);  
        textview1 = (TextView) findViewById(R.id.sub_sys_main_common_cardcode);
        textview2 = (TextView) findViewById(R.id.sub_sys_main_common_timetr);
        textview3 = (TextView) findViewById(R.id.sub_sys_main_common_time);
        textview4 = (TextView) findViewById(R.id.sub_sys_main_common_statestr);
        textview5 = (TextView) findViewById(R.id.sub_sys_main_common_right);
    }  
 
	public TextView getTextview0() {
		return textview0;
	}

	public void setTextview0(TextView textview0) {
		this.textview0 = textview0;
	}

	public TextView getTextview1() {
		return textview1;
	}

	public void setTextview1(TextView textview1) {
		this.textview1 = textview1;
	}

	public TextView getTextview2() {
		return textview2;
	}

	public void setTextview2(TextView textview2) {
		this.textview2 = textview2;
	}

	public TextView getTextview3() {
		return textview3;
	}

	public void setTextview3(TextView textview3) {
		this.textview3 = textview3;
	}

	public TextView getTextview4() {
		return textview4;
	}

	public void setTextview4(TextView textview4) {
		this.textview4 = textview4;
	}

	public TextView getTextview5() {
		return textview5;
	}

	public void setTextview5(TextView textview5) {
		this.textview5 = textview5;
	}  
    
}
