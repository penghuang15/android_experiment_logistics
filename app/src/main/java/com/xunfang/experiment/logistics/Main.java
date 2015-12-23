package com.xunfang.experiment.logistics;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.experiment.logistics.db.LogisticsSqliteHelper;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：主界面
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
public class Main extends TabActivity implements OnCheckedChangeListener,
		OnClickListener {
	/** Called when the activity is first created. */

	private static RadioButton login;
	private static RadioButton systemmgr;
	private static RadioButton goodsmgr;
	private static RadioButton goodsfollow;
	private static RadioGroup tabGroup;
	private Intent goodsmgrIntent;
	private Intent loginIntent;
	private Intent systemmgrIntent;
	private Intent goodsfollowIntent;

	public static TabHost tabHost;
	private ImageView revert;// 返回"按钮"
	private TextView title;// 标题

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_tab);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView) findViewById(R.id.common_title_revert);
		title = (TextView) findViewById(R.id.common_title_title);
		title.setText(R.string.app_name);
		revert.setOnClickListener(this);
		tabHost = getTabHost();
		this.loginIntent = new Intent(this, LoginActivity.class);
		this.systemmgrIntent = new Intent(this, SystemMgrMain.class);
		this.goodsmgrIntent = new Intent(this, GoodsMgrMain.class);
		this.goodsfollowIntent = new Intent(this,
				GoodsFollowQueryActivity.class);
		initRadios();
		setupIntent();
		// 用于创建数据库表和初始化数据的录入
		LogisticsSqliteHelper logisticsSqliteHelper = LogisticsSqliteHelper
				.getInstance(Main.this);
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatabase实例
			db = logisticsSqliteHelper.getWritableDatabase();
		} catch (Exception e) {
			// 获得SQLiteDatabase实例
			db = logisticsSqliteHelper.getReadableDatabase();
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * 初始化底部按钮
	 */
	private void initRadios() {
		tabGroup = (RadioGroup) findViewById(R.id.main_tab_group);
		login = (RadioButton) findViewById(R.id.main_tab_login);
		systemmgr = (RadioButton) findViewById(R.id.main_tab_systemmgr);
		goodsmgr = (RadioButton) findViewById(R.id.main_tab_goodsmgr);
		goodsfollow = (RadioButton) findViewById(R.id.main_tab_goodsfollow);
		tabGroup.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.main_tab_login:
			if ("".equals(MyConfig.getNOW_LOGINNAME())) {
				tabHost.setCurrentTabByTag("login_tab");
				Log.d("islogin", "click login_tab");
			} else {
				Main.setChecked(R.id.main_tab_systemmgr);
			}
			break;
		case R.id.main_tab_systemmgr:
			if (!"".equals(MyConfig.getNOW_LOGINNAME())) {
				if (MyConfig.getNOW_USERTYPE() == 0) {
					tabHost.setCurrentTabByTag("systemmgr_tab");
				} else if (MyConfig.getNOW_USERTYPE() == 1) {
					Toast.makeText(Main.this, R.string.notallowed,
							Toast.LENGTH_SHORT).show();
					Main.setChecked(R.id.main_tab_goodsmgr);
				}
			} else {
				Main.setChecked(R.id.main_tab_login);
			}
			break;
		case R.id.main_tab_goodsmgr:
			if (!"".equals(MyConfig.getNOW_LOGINNAME())) {
				tabHost.setCurrentTabByTag("goodsmgr_tab");
				Log.d("islogin", "click goodsmgr_tab");
			} else {
				Main.setChecked(R.id.main_tab_login);
			}
			break;
		case R.id.main_tab_goodsfollow:
			if (!"".equals(MyConfig.getNOW_LOGINNAME())) {
				tabHost.setCurrentTabByTag("goodsfollow_tab");
				Log.d("islogin", "click goodsfollow_tab");
			} else {
				Main.setChecked(R.id.main_tab_login);
			}
			break;
		}
	}
	private void setupIntent() {
		tabHost = getTabHost();
		tabHost.addTab(buildTabSpec("login_tab", R.string.main_login,
				R.drawable.tab_login, this.loginIntent));
		tabHost.addTab(buildTabSpec("systemmgr_tab", R.string.main_systemmgr,
				R.drawable.tab_systemmgr, this.systemmgrIntent));
		tabHost.addTab(buildTabSpec("goodsmgr_tab", R.string.main_goodsmgr,
				R.drawable.tab_goodsmgr, this.goodsmgrIntent));
		tabHost.addTab(buildTabSpec("goodsfollow_tab",
				R.string.main_goodsfollow, R.drawable.tab_goodsfollow,
				this.goodsfollowIntent));
	}
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return tabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_OK implements
			android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			MyConfig.setNOW_LOGINNAME("");
			finish();
			System.exit(0);
		}
	}
	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_Cancel implements
			android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof ImageView) {
			switch (v.getId()) {
			case R.id.common_title_revert:// 返回上一页
				AlertDialog.Builder revert_dialog = new AlertDialog.Builder(
						this);
				revert_dialog
						.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
				revert_dialog.setTitle("确定退出？");
				revert_dialog.setPositiveButton("确定", new OnClickLiner_OK());
				revert_dialog
						.setNegativeButton("取消", new OnClickLiner_Cancel());
				revert_dialog.show();
				break;
			default:
				break;
			}
		}
	}
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            	AlertDialog.Builder revert_dialog = new AlertDialog.Builder(this);
    			revert_dialog.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    			revert_dialog.setTitle("确定退出？");
    			revert_dialog.setPositiveButton("确定", new OnClickLiner_OK());
    			revert_dialog.setNegativeButton("取消", new OnClickLiner_Cancel());
    			revert_dialog.show();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
	/**
	 * 设置选中的RadioButton
	 * 
	 * @param id
	 */
	public static void setChecked(int id) {
		tabGroup.check(id);
	}
}