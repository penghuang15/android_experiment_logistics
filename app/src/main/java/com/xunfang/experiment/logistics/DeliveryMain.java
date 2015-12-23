package com.xunfang.experiment.logistics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.widget.Main_item;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：派件员主界面</p>
 * <p>Company</p>
 * <p>Copyright: Copyright (c) 2012 </p>
 * @version 1.0.0.0
 * @author sas 
 */
public class DeliveryMain extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	// 定义主界面上的组件
	private Main_item deliveryMgr;// 派件
	private Main_item signMgr;// 签收
	private Main_item goodsfollowMgr;// 物品跟踪
	private Main_item exit;// 退出
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_delivery);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(R.string.app_name);
		
		// 初始化主界面上的组件
		deliveryMgr = (Main_item) findViewById(R.id.deliveryMgr);
		signMgr = (Main_item) findViewById(R.id.signMgr);
		goodsfollowMgr = (Main_item) findViewById(R.id.goodsfollowMgr);
		exit = (Main_item) findViewById(R.id.exit);
		deliveryMgr.getTextview().setText(getText(R.string.delivery));
		signMgr.getTextview().setText(getText(R.string.sign));
		goodsfollowMgr.getTextview().setText(getText(R.string.goodsfollowMgr));
		exit.getTextview().setText(getText(R.string.exit));
		// 设置监听器
		deliveryMgr.setOnClickListener(this);
		signMgr.setOnClickListener(this);
		goodsfollowMgr.setOnClickListener(this);
		exit.setOnClickListener(this);
		revert.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if("".equals(MyConfig.getNOW_LOGINNAME())){
    		Intent intent = new Intent(DeliveryMain.this,LoginActivity.class);
    		startActivity(intent);
    		finish();
    	}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.deliveryMgr://派件
			Intent deliveryMgrIntent = new Intent(DeliveryMain.this,DeliveryActivity.class);
			startActivity(deliveryMgrIntent);
			finish();
			break;
		case R.id.signMgr://签收
			Intent signMgrIntent = new Intent(DeliveryMain.this,SignActivity.class);
			startActivity(signMgrIntent);
			finish();
			break;
		case R.id.goodsfollowMgr://物品跟踪
			Intent goodsfollowMgrIntent = new Intent(DeliveryMain.this,GoodsFollowQueryActivity.class);
			startActivity(goodsfollowMgrIntent);	
			finish();
			break;
		case R.id.exit://退出
			AlertDialog.Builder exit_dialog = new AlertDialog.Builder(this);
			exit_dialog.setIcon(android.R.drawable.ic_menu_help);
			exit_dialog.setTitle(getText(R.string.confirm_exit));
			exit_dialog.setPositiveButton(getText(R.string.ensure), new OnClickLiner_OK());
			exit_dialog.setNegativeButton(getText(R.string.cancel), new OnClickLiner_Cancel());
			exit_dialog.show();
			break;
		case R.id.common_title_revert://返回按钮
			AlertDialog.Builder revert_dialog = new AlertDialog.Builder(this);
			revert_dialog.setIcon(android.R.drawable.ic_menu_help);
			revert_dialog.setTitle(getText(R.string.confirm_exit));
			revert_dialog.setPositiveButton(getText(R.string.ensure), new OnClickLiner_OK());
			revert_dialog.setNegativeButton(getText(R.string.cancel), new OnClickLiner_Cancel());
			revert_dialog.show();
			break;
		default:
			break;
		}
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
			// TODO Auto-generated method stub
			MyConfig.setNOW_LOGINNAME("");
			MyConfig.setNOW_LOGINID(-1);
			MyConfig.setNOW_STATION_ID(-1);
			MyConfig.setNOW_USERTYPE(-1);
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
	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 判断返回键是否被按下
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(0);
			ImageView iv = new ImageView(this);
			iv.setImageResource(android.R.drawable.ic_menu_help);
			TextView text = new TextView(this);
			text.setTextSize(20);
			text.setText(getText(R.string.confirm_exit));
			layout.addView(iv);
			layout.addView(text);
			dialog.setCustomTitle(layout);
			dialog.setPositiveButton(getText(R.string.ensure), new OnClickLiner_OK());
			dialog.setNegativeButton(getText(R.string.cancel), new OnClickLiner_Cancel());
			dialog.show();
		}
		return false;
	}
}