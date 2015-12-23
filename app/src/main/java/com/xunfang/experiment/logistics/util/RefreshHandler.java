 package com.xunfang.experiment.logistics.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.xunfang.experiment.logistics.DeliveryActivity;
import com.xunfang.experiment.logistics.SignActivity;
import com.xunfang.experiment.logistics.StationInActivity;
import com.xunfang.experiment.logistics.StationOutActivity;
import com.xunfang.experiment.logistics.WaitforDeliveryActivity;
/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description：更新业务界面信息类
 * </p>
 * <p>
 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @author 3G终端应用开发组
 * @version 1.0.0.0
 */
public class RefreshHandler extends Handler {
	//界面上的TextView数组
	private TextView[] textView_arr = null;
	private int BUSINESS_NUM;// 业务类型编号

	public RefreshHandler(TextView[] textView_arr, int BUSINESS_NUM) {
		this.textView_arr = textView_arr;
		this.BUSINESS_NUM = BUSINESS_NUM;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		String[] reMsg = msg.getData().getStringArray("CARDINFO");// 获取卡信息
		switch (BUSINESS_NUM) {
		case MyConfig.STATION_IN:
			if (reMsg != null) {
				if(reMsg.length>1){
					textView_arr[0].setText(reMsg[0]);
					textView_arr[1].setText(reMsg[1]);
					textView_arr[2].setText(reMsg[2]);
					new ClsThread(BUSINESS_NUM).start();
				}else if(reMsg.length == 1){
				}
			} else {
				textView_arr[0].setText("");
				textView_arr[1].setText("");
				textView_arr[2].setText("");
			}
			break;
		case MyConfig.STATION_OUT:
			if (reMsg != null) {
				if(reMsg.length>1){
					textView_arr[0].setText(reMsg[0]);
					textView_arr[1].setText(reMsg[1]);
					textView_arr[2].setText(reMsg[2]);
					new ClsThread(BUSINESS_NUM).start();
				}else if(reMsg.length == 1){
				}
			} else {
				textView_arr[0].setText("");
				textView_arr[1].setText("");
				textView_arr[2].setText("");
			}
			break;
		case MyConfig.SIGN:
			if (reMsg != null) {
				if(reMsg.length>1){
					textView_arr[0].setText(reMsg[0]);
					textView_arr[1].setText(reMsg[1]);
					textView_arr[2].setText(reMsg[2]);
					new ClsThread(BUSINESS_NUM).start();
				}else if(reMsg.length == 1){
				}
			} else {
				textView_arr[0].setText("");
				textView_arr[1].setText("");
				textView_arr[2].setText("");
			}
			break;
		case MyConfig.DELIVERY:
			if (reMsg != null) {
				if(reMsg.length>1){
					textView_arr[0].setText(reMsg[0]);
					textView_arr[1].setText(reMsg[1]);
					textView_arr[2].setText(reMsg[2]);
					new ClsThread(BUSINESS_NUM).start();
				}else if(reMsg.length == 1){
				}
			} else {
				textView_arr[0].setText("");
				textView_arr[1].setText("");
				textView_arr[2].setText("");
			}
			break;
		case MyConfig.FOR_DELIVERY:
			if (reMsg != null) {
				if(reMsg.length>1){
					textView_arr[0].setText(reMsg[0]);
					textView_arr[1].setText(reMsg[1]);
					textView_arr[2].setText(reMsg[2]);
					new ClsThread(BUSINESS_NUM).start();
				}else if(reMsg.length == 1){
				}
			} else {
				textView_arr[0].setText("");
				textView_arr[1].setText("");
				textView_arr[2].setText("");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 清空内容线程
	 * 
	 * @author Administrator
	 * 
	 */
	class ClsThread extends Thread {
		int BUSINESS_NUM = 0;

		public ClsThread(int BUSINESS_NUM) {
			this.BUSINESS_NUM = BUSINESS_NUM;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Bundle budle = new Bundle();
			budle.putStringArray("CARDINFO", null);

			switch (BUSINESS_NUM) {
			case MyConfig.STATION_IN:// 入站
				StationInActivity.ParseMessage(budle);
				break;
			case MyConfig.STATION_OUT:// 出站
				StationOutActivity.ParseMessage(budle);
				break;
			case MyConfig.DELIVERY:// 派件
				DeliveryActivity.ParseMessage(budle);
				break;
			case MyConfig.SIGN:// 签收
				SignActivity.ParseMessage(budle);
				break;
			case MyConfig.FOR_DELIVERY:// 等待派送
				WaitforDeliveryActivity.ParseMessage(budle);
				break;
			default:
				break;
			}
		}
	}
}
