package com.xunfang.experiment.logistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xunfang.experiment.logistics.service.SearchCardThread;
import com.xunfang.experiment.logistics.so.Linuxc;
import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.util.ReaderConfig;
import com.xunfang.experiment.logistics.util.RefreshHandler;
import com.xunfang.experiment.logistics.widget.Sub_sys_main_common;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：派件扫描界面</p>
 * <p>Company </p>
 * <p>Copyright: Copyright (c) 2012 </p>
 * @version 1.0.0.0
 * @author sas
 * 
 */
public class DeliveryActivity extends Activity implements OnClickListener{
	//定义控件
	private Sub_sys_main_common deliverySubSysMainItem;
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	//更新界面的Handler
	private static RefreshHandler Delivery_RefreshHandler = null;
	//界面中的TextView数组
	private TextView[] textView_arr;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口布局
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //设置界面内容
        setContentView(R.layout.delivery);
        //自定义窗口样式
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(getText(R.string.delivery));
		revert.setOnClickListener(this);
        //获得子系统公用主界面控件的实例
		deliverySubSysMainItem = (Sub_sys_main_common)findViewById(R.id.sub_sys_delivery);
        //设置界面显示内容
		deliverySubSysMainItem.getTextview0().setText((String)getText(R.string.cardcodestr)+"：");
		deliverySubSysMainItem.getTextview1().setText("");
		deliverySubSysMainItem.getTextview2().setText((String)getText(R.string.timestr)+"：");
		deliverySubSysMainItem.getTextview3().setText("");
		deliverySubSysMainItem.getTextview4().setText((String)getText(R.string.statestr)+"：");
		deliverySubSysMainItem.getTextview5().setText("");
        textView_arr = new TextView[3];
		textView_arr[0] = deliverySubSysMainItem.getTextview1();
		textView_arr[1] = deliverySubSysMainItem.getTextview3();
		textView_arr[2] = deliverySubSysMainItem.getTextview5();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if("".equals(MyConfig.getNOW_LOGINNAME())){
    		//Intent intent = new Intent(AccessRightActivity.this,LoginActivity.class);
			Intent intent = new Intent(DeliveryActivity.this,Main.class);
			Main.setChecked(R.id.main_tab_login);
    		startActivity(intent);
    		finish();
    	}else{
			//设置系统当前所处界面
			MyConfig.setCurrent_interface(MyConfig.DELIVERY);
			Delivery_RefreshHandler = new RefreshHandler(textView_arr,
	        		MyConfig.getCurrent_interface());
			ReaderConfig.setUART_STATE(Linuxc.openUart(3));// 打开串口3
			if (ReaderConfig.getUART_STATE() > 0) {// 判断串口是否打开
				System.out.println("Uart OPEN");
				Linuxc.setUart(115200);// 设置串口波特率115200
				Linuxc.setParity(8, 1, 'N');// 设置数据校验
				ReaderConfig.SEARCHER_THREAD_FLAG = false;
				Thread scThread = new Thread(new SearchCardThread(DeliveryActivity.this,
						MyConfig.getCurrent_interface()));//启动寻卡业务处理线程
				scThread.start();
			}
    	}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//设置系统当前所处界面
		MyConfig.setCurrent_interface(MyConfig.NONE_INTERFACE);
		ReaderConfig.SEARCHER_THREAD_FLAG = true;
	}

	public static void ParseMessage(Bundle message) {
		Message msg = new Message();
		msg.setData(message);
		Delivery_RefreshHandler.sendMessage(msg);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view instanceof ImageView){
			switch (view.getId()) {
			case R.id.common_title_revert:
				//startActivity(new Intent(DeliveryActivity.this,DeliveryMain.class));
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
			//startActivity(new Intent(DeliveryActivity.this,DeliveryMain.class));
			finish();
		}
		return false;
	}
}
