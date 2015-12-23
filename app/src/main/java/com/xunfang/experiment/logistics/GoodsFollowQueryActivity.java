package com.xunfang.experiment.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：物流跟踪查询界面
 * </p>
 * <p>
 * Company
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author sas
 * 
 */
public class GoodsFollowQueryActivity extends Activity implements OnClickListener{

	//更新界面的Handler
	//private static RefreshHandler OneCard_RefreshHandler = null;
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	private TextView ordernumberstr;//订单号(字符串)
	private EditText ordernumber;//订单号
	private TextView cardsnstr;//卡号(字符串)
	private EditText cardsn;//卡号
	private Button operate_qry;//查询按钮
	private Button operate_cancel;//取消按钮
	
	private String follow_cardsn = "";//卡号
	private String follow_ordernum = "";//订单号
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.follow_qry);
		// 自定义窗口样式
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(R.string.restart_query);
		revert.setOnClickListener(this);*/
		
		setContentView(R.layout.follow_qry);
		ordernumberstr = (TextView)findViewById(R.id.follow_qry_ordernumberstr);
		ordernumber = (EditText)findViewById(R.id.follow_qry_ordernumber);
		cardsnstr = (TextView)findViewById(R.id.follow_qry_cardsnstr);
		cardsn = (EditText)findViewById(R.id.follow_qry_cardsn);
		
		operate_qry = (Button)findViewById(R.id.follow_qry_operate_qry);
		operate_cancel = (Button)findViewById(R.id.follow_qry_operate_cancel);
		ordernumberstr.setText(getText(R.string.ordernumstr)+"：");
		cardsnstr.setText(getText(R.string.cardsnstr)+"：");
		
		operate_qry.setOnClickListener(this);
		operate_cancel.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*if("".equals(MyConfig.getNOW_LOGINNAME())){
    		Intent intent = new Intent(GoodsFollowQueryActivity.this,LoginActivity.class);
    		startActivity(intent);
    		finish();
    	}else{
    		// 设置系统当前所处界面
    		//IOTConfig.setCurrent_interface(IOTConfig.ONECARDOPERATE);
    		//OneCard_RefreshHandler = new RefreshHandler(textView_arr,null,
    				//IOTConfig.getCurrent_interface());
    	}*/
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 设置系统当前所处界面
		//IOTConfig.setCurrent_interface(IOTConfig.NONE_INTERFACE);
		//ReaderConfig.SEARCHER_THREAD_FLAG = true;
	}

	public static void ParseMessage(Bundle message) {
		Message msg = new Message();
		msg.setData(message);
		//OneCard_RefreshHandler.sendMessage(msg);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view instanceof ImageView || view instanceof Button){
			switch (view.getId()) {
			case R.id.follow_qry_operate_qry:
				follow_ordernum = ordernumber.getText().toString();
				follow_cardsn = cardsn.getText().toString();
				int flag = -3;
				if((!"".equals(follow_cardsn) && follow_cardsn.length() == 8)){
					if(!"".equals(follow_ordernum) && follow_ordernum.length() == 20){
						flag = 2;
					}else if("".equals(follow_ordernum)){
						flag = 0;
					}else{
						flag = -2;
					}
				}else if("".equals(follow_cardsn)){
					if(!"".equals(follow_ordernum) && follow_ordernum.length() == 20){
						flag = 1;
					}else if("".equals(follow_ordernum)){
						flag = -1;
					}else{
						flag = -2;
					}
				}else{
					flag = -1;
				}
				
				if(flag == 0 || flag == 1 || flag == 2){
					Intent intent = new Intent(GoodsFollowQueryActivity.this,GoodsFollowActivity.class);
					intent.putExtra("ordernumber", follow_ordernum);
					intent.putExtra("cardsn", follow_cardsn);
					startActivity(intent);
					//finish();
				}else if(flag == -1){
					Toast.makeText(GoodsFollowQueryActivity.this, getText(R.string.cardsn_err), Toast.LENGTH_SHORT).show();
				}else if(flag == -2){
					Toast.makeText(GoodsFollowQueryActivity.this, getText(R.string.ordernumber_err), Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.follow_qry_operate_cancel:
			case R.id.common_title_revert:
				/*if(MyConfig.getNOW_USERTYPE() == 0){//管理员
					startActivity(new Intent(GoodsFollowQueryActivity.this,Main_bak.class));
				}else if(MyConfig.getNOW_USERTYPE() == 1){//派件员
					startActivity(new Intent(GoodsFollowQueryActivity.this,DeliveryMain.class));
				}*/
				finish();
				break;
			default:
				break;
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
			/*if(MyConfig.getNOW_USERTYPE() == 0){//管理员
				startActivity(new Intent(GoodsFollowQueryActivity.this,Main_bak.class));
			}else if(MyConfig.getNOW_USERTYPE() == 1){//派件员
				startActivity(new Intent(GoodsFollowQueryActivity.this,DeliveryMain.class));
			}*/
			finish();
		}
		return false;
	}
	
}
