package com.xunfang.experiment.logistics;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.xunfang.experiment.logistics.db.UserDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.widget.LoginWidget;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：登录界面
 * </p>
 * <p>

 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author sas
 */
public class LoginActivity extends Activity implements OnClickListener {

	private final int DIALOG_ID = 13;
	// 定义控件
	private LoginWidget loginWidget;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// 获得登录控件的实例
		loginWidget = (LoginWidget) findViewById(R.id.login_module);
		loginWidget.getLoginnamestr().setText(
				getText(R.string.loginnamestr) + "：");
		loginWidget.getPasswordstr().setText(
				getText(R.string.passwordstr) + "：");
		loginWidget.getLoginname().setText("admin");
		loginWidget.getPassword().setText("admin");
		loginWidget.getLogin().setOnClickListener(this);
		loginWidget.getCancel().setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("islogin", "onResume");
		if (!"".equals(MyConfig.getNOW_LOGINNAME())) {
			Main.setChecked(R.id.main_tab_systemmgr);
		} else {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			String loginname = loginWidget.getLoginname().getText().toString();
			String password = loginWidget.getPassword().getText().toString();
			if (loginname != null && !"".equals(loginname) && password != null
					&& !"".equals(password)) {
				new LoginTask().execute(loginname, password);
			} else {
				Toast.makeText(LoginActivity.this,
						getText(R.string.login_empty), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.common_title_revert:
		case R.id.cancel:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 异步操作——登录
	 * 
	 * @author sas
	 * 
	 */
	class LoginTask extends AsyncTask<String, String, String> {
		/**
		 * 后台执行登录操作
		 */
		@Override
		protected String doInBackground(String... params) {
			UserDBUtil userDBUtil = new UserDBUtil(LoginActivity.this);
			int flag = userDBUtil.login(params[0], params[1]);
			return flag + "";
		}

		/**
		 * 执行doInBackground前被调用
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID);
		}

		/**
		 * 执行doInBackground后被调用
		 */
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID);
			if ("0".equals(result)) {
				MyConfig.setNOW_LOGINNAME(loginWidget.getLoginname().getText()
						.toString());
				MyConfig.setNOW_USERTYPE(0);
				MyConfig.setNOW_LOGINNAME(loginWidget.getLoginname().getText()
						.toString());
				Main.setChecked(R.id.main_tab_systemmgr);
			} else if ("1".equals(result)) {
				MyConfig.setNOW_LOGINNAME(loginWidget.getLoginname().getText()
						.toString());
				MyConfig.setNOW_USERTYPE(1);
				MyConfig.setNOW_LOGINNAME(loginWidget.getLoginname().getText()
						.toString());
				Main.setChecked(R.id.main_tab_systemmgr);
			} else if ("-1".equals(result)) {
				Toast.makeText(LoginActivity.this, getText(R.string.login_err),
						Toast.LENGTH_SHORT).show();
			} else if ("-2".equals(result)) {
				Toast.makeText(LoginActivity.this,
						getText(R.string.database_err), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 判断返回键是否被按下
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
			dialog.setMessage(getText(R.string.logining));
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog;
		default:
			return null;
		}
	}
}