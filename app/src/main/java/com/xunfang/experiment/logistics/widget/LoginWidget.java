package com.xunfang.experiment.logistics.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunfang.experiment.logistics.R;


/**
 * <p>Title：3G手机开发终端——物流管理系统</p>
 * <p>Description：自定义的登录控件</p>
 
 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author 3G终端应用开发组 
 */
public class LoginWidget extends LinearLayout{
	
	private TextView loginnamestr;//用户名(字符串)
	private EditText loginname;//用户名
	private TextView passwordstr;//密码(字符串)
	private EditText password;//密码
	private Button login;//登录按钮
	private Button cancel;//取消按钮
  
    public LoginWidget(Context context) {  
        this(context, null);  
    }
  
    public LoginWidget(Context context,AttributeSet attrs) {  
        super(context,attrs);  
        // 导入布局  
        LayoutInflater.from(context).inflate(R.layout.login_widget, this, true);
        //获得控件实例
        loginnamestr = (TextView) findViewById(R.id.loginnamestr);
        loginname = (EditText) findViewById(R.id.loginname);
        passwordstr = (TextView) findViewById(R.id.passwordstr);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        cancel = (Button) findViewById(R.id.cancel);
    }

	public TextView getLoginnamestr() {
		return loginnamestr;
	}

	public void setLoginnamestr(TextView loginnamestr) {
		this.loginnamestr = loginnamestr;
	}

	public EditText getLoginname() {
		return loginname;
	}

	public void setLoginname(EditText loginname) {
		this.loginname = loginname;
	}

	public TextView getPasswordstr() {
		return passwordstr;
	}

	public void setPasswordstr(TextView passwordstr) {
		this.passwordstr = passwordstr;
	}

	public EditText getPassword() {
		return password;
	}

	public void setPassword(EditText password) {
		this.password = password;
	}

	public Button getLogin() {
		return login;
	}

	public void setLogin(Button login) {
		this.login = login;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}  
}
