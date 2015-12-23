package com.xunfang.experiment.logistics.service;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.xunfang.experiment.logistics.DeliveryActivity;
import com.xunfang.experiment.logistics.SignActivity;
import com.xunfang.experiment.logistics.StationInActivity;
import com.xunfang.experiment.logistics.StationOutActivity;
import com.xunfang.experiment.logistics.WaitforDeliveryActivity;
import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.db.CarduserDBUtil;
import com.xunfang.experiment.logistics.db.GoodsFollowDBUtil;
import com.xunfang.experiment.logistics.db.StationDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.util.ReaderConfig;
import com.xunfang.experiment.logistics.util.Tools;

/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description: 寻卡和业务处理线程
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
public class SearchCardThread implements Runnable {
	private int BUSINESS_NUM = 0;// 业务类型编号
	private Context context = null;

	public SearchCardThread(Context context, int BUSINESS_NUM) {
		this.BUSINESS_NUM = BUSINESS_NUM;
		this.context = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!ReaderConfig.SEARCHER_THREAD_FLAG) {// 判断寻卡线程是否开启
			byte[] sreMsg = ReaderOperating.searchCard();// 寻卡
			if (sreMsg != null && sreMsg.length > 5 && sreMsg[4] != 0x05 && sreMsg[4] != 0x04) {// 判断是否寻到卡
				if (sreMsg[4] != 0x04 && sreMsg[1] != 0x30) {
/*					 if (sreMsg != null) { Log.d("rdebug", "revice--------"); 
					 for (int i = 0; i < sreMsg.length; i++) { 
						 Log.d("rdebug", Byte.toString(sreMsg[i])); 
					 } 
					 }*/
					String cardid = ReaderDataProcess.getFristCardID(sreMsg);// 获取原始卡号
					if (cardid != null) {
						if (ReaderConfig.FRIST_CARD_ID_S == null
								|| !cardid.equals(ReaderConfig.FRIST_CARD_ID_S)) {// 判断卡是否移开
							String formatDate = Tools.formatDateString1(new Date());// 格式化日期时间
							//根据当前所处的界面来执行相应业务
							switch (BUSINESS_NUM) {
								case MyConfig.STATION_IN:// 入站
										Log.d("SendThread", "入站操作");
										String[] cardinfo_in = ReaderOperating.readCard(sreMsg);//执行读卡
										if (cardinfo_in != null && cardinfo_in[1] != null) {
											String[] result = new String[3];
											if(!"00000000".equals(cardinfo_in[1])){
												//String card_type = cardinfo_d[1].substring(0, 2);
												CarduserDBUtil carduserDBUtil = new CarduserDBUtil(context);
												StationDBUtil stationDBUtil = new StationDBUtil(context);
												//验证卡片是否为合法卡片
												//if (!"25".equals(card_type)&&!"56".equals(card_type)){
												String checkResult = carduserDBUtil.checkCardsn(cardinfo_in[1]);
												StationBean stationBean = stationDBUtil.getStationById(MyConfig.getNOW_STATION_ID());
												if(checkResult.length() == 20){
													result[0] = cardinfo_in[1];
													result[1] = formatDate;
													result[2] = "物品入站";
													GoodsFollowDBUtil goodsFollowDBUtil = new GoodsFollowDBUtil(context);
													ContentValues values = new ContentValues();
													values.put("ordernumber", checkResult);
													values.put("currentstation", MyConfig.getNOW_STATION_ID());
													values.put("information", "进入"+stationBean.getName());
													values.put("handletime", Tools.formatDateString2(Tools.getDateFromString(formatDate)));
													values.put("operater", MyConfig.getNOW_LOGINID());
													values.put("state", 0);
													
													goodsFollowDBUtil.addGoodsFollow(values);
												}else if("-1".equals(checkResult)){
													result[0] = cardinfo_in[1];
													result[1] = formatDate;
													result[2] = "非法卡";
												}else if("-2".equals(checkResult)){
													result[0] = cardinfo_in[1];
													result[1] = formatDate;
													result[2] = "验证失败";
												}
											}else {
												result[0] = "";
												result[1] = formatDate;
												result[2] = "非法卡";
											}
											/*} else {
												result[0] = cardinfo_d[1];
												result[1] = formatDate;
												result[2] = "非法用户";
											}*/
											//更新界面
											Bundle budle = new Bundle();
											budle.putStringArray("CARDINFO", result);
											StationInActivity.ParseMessage(budle);
										}
										break;
								case MyConfig.STATION_OUT:// 出站
									Log.d("SendThread", "出站操作");
									String[] cardinfo_out = ReaderOperating.readCard(sreMsg);//执行读卡
									if (cardinfo_out != null && cardinfo_out[1] != null) {
										String[] result = new String[3];
										if(!"00000000".equals(cardinfo_out[1])){
											//String card_type = cardinfo_d[1].substring(0, 2);
											CarduserDBUtil carduserDBUtil = new CarduserDBUtil(context);
											StationDBUtil stationDBUtil = new StationDBUtil(context);
											//验证卡片是否为合法卡片
											//if (!"25".equals(card_type)&&!"56".equals(card_type)){
											String checkResult = carduserDBUtil.checkCardsn(cardinfo_out[1]);
											System.out.println("校验结果："+checkResult);
											StationBean stationBean = stationDBUtil.getStationById(MyConfig.getNOW_STATION_ID());
											if(checkResult.length() == 20){
												result[0] = cardinfo_out[1];
												result[1] = formatDate;
												result[2] = "物品出站";
												GoodsFollowDBUtil goodsFollowDBUtil = new GoodsFollowDBUtil(context);
												ContentValues values = new ContentValues();
												values.put("ordernumber", checkResult);
												values.put("currentstation", MyConfig.getNOW_STATION_ID());
												values.put("information", "离开"+stationBean.getName()+"，发往"+MyConfig.nextStation.getName());
												values.put("handletime", Tools.formatDateString2(Tools.getDateFromString(formatDate)));
												values.put("operater", MyConfig.getNOW_LOGINID());
												values.put("state", 0);
												
												goodsFollowDBUtil.addGoodsFollow(values);
											}else if("-1".equals(checkResult)){
												result[0] = cardinfo_out[1];
												result[1] = formatDate;
												result[2] = "非法卡";
											}else if("-2".equals(checkResult)){
												result[0] = cardinfo_out[1];
												result[1] = formatDate;
												result[2] = "验证失败";
											}
										}else {
											result[0] = "";
											result[1] = formatDate;
											result[2] = "非法卡";
										}
										/*} else {
											result[0] = cardinfo_d[1];
											result[1] = formatDate;
											result[2] = "非法用户";
										}*/
										//更新界面
										Bundle budle = new Bundle();
										budle.putStringArray("CARDINFO", result);
										StationOutActivity.ParseMessage(budle);
									}
									break;
								case MyConfig.FOR_DELIVERY:// 等待派送
									Log.d("SendThread", "等待派送操作");
									String[] cardinfo_fordeliver = ReaderOperating.readCard(sreMsg);//执行读卡
									if (cardinfo_fordeliver != null && cardinfo_fordeliver[1] != null) {
										String[] result = new String[3];
										if(!"00000000".equals(cardinfo_fordeliver[1])){
											//String card_type = cardinfo_d[1].substring(0, 2);
											CarduserDBUtil carduserDBUtil = new CarduserDBUtil(context);
											StationDBUtil stationDBUtil = new StationDBUtil(context);
											//验证卡片是否为合法卡片
											//if (!"25".equals(card_type)&&!"56".equals(card_type)){
											String checkResult = carduserDBUtil.checkCardsn(cardinfo_fordeliver[1]);
											System.out.println("校验结果："+checkResult);
											StationBean stationBean = stationDBUtil.getStationById(MyConfig.getNOW_STATION_ID());
											if(checkResult.length() == 20){
												result[0] = cardinfo_fordeliver[1];
												result[1] = formatDate;
												result[2] = "等待派送";
												GoodsFollowDBUtil goodsFollowDBUtil = new GoodsFollowDBUtil(context);
												ContentValues values = new ContentValues();
												values.put("ordernumber", checkResult);
												values.put("currentstation", MyConfig.getNOW_STATION_ID());
												values.put("information", "离开"+stationBean.getName()+"，等待派送");
												values.put("handletime", Tools.formatDateString2(Tools.getDateFromString(formatDate)));
												values.put("operater", MyConfig.getNOW_LOGINID());
												values.put("state", 0);
												
												goodsFollowDBUtil.addGoodsFollow(values);
											}else if("-1".equals(checkResult)){
												result[0] = cardinfo_fordeliver[1];
												result[1] = formatDate;
												result[2] = "非法卡";
											}else if("-2".equals(checkResult)){
												result[0] = cardinfo_fordeliver[1];
												result[1] = formatDate;
												result[2] = "验证失败";
											}
										}else {
											result[0] = "";
											result[1] = formatDate;
											result[2] = "非法卡";
										}
										/*} else {
											result[0] = cardinfo_d[1];
											result[1] = formatDate;
											result[2] = "非法用户";
										}*/
										//更新界面
										Bundle budle = new Bundle();
										budle.putStringArray("CARDINFO", result);
										WaitforDeliveryActivity.ParseMessage(budle);
									}
									break;
								case MyConfig.DELIVERY:// 派件
									Log.d("SendThread", "派件操作");
									String[] cardinfo_delivery = ReaderOperating.readCard(sreMsg);//执行读卡
									if (cardinfo_delivery != null && cardinfo_delivery[1] != null) {
										String[] result = new String[3];
										if(!"00000000".equals(cardinfo_delivery[1])){
											//String card_type = cardinfo_d[1].substring(0, 2);
											CarduserDBUtil carduserDBUtil = new CarduserDBUtil(context);
											StationDBUtil stationDBUtil = new StationDBUtil(context);
											//验证卡片是否为合法卡片
											//if (!"25".equals(card_type)&&!"56".equals(card_type)){
											String checkResult = carduserDBUtil.checkCardsn(cardinfo_delivery[1]);
											System.out.println("校验结果："+checkResult);
											if(checkResult.length() == 20){
												result[0] = cardinfo_delivery[1];
												result[1] = formatDate;
												result[2] = "等待派送";
												GoodsFollowDBUtil goodsFollowDBUtil = new GoodsFollowDBUtil(context);
												ContentValues values = new ContentValues();
												values.put("ordernumber", checkResult);
												values.put("currentstation", MyConfig.getNOW_STATION_ID());
												values.put("information", "派件员正在派件");
												values.put("handletime", Tools.formatDateString2(Tools.getDateFromString(formatDate)));
												values.put("operater", MyConfig.getNOW_LOGINID());
												values.put("state", 0);
												
												goodsFollowDBUtil.addGoodsFollow(values);
											}else if("-1".equals(checkResult)){
												result[0] = cardinfo_delivery[1];
												result[1] = formatDate;
												result[2] = "非法卡";
											}else if("-2".equals(checkResult)){
												result[0] = cardinfo_delivery[1];
												result[1] = formatDate;
												result[2] = "验证失败";
											}
										}else {
											result[0] = "";
											result[1] = formatDate;
											result[2] = "非法卡";
										}
										/*} else {
											result[0] = cardinfo_d[1];
											result[1] = formatDate;
											result[2] = "非法用户";
										}*/
										//更新界面
										Bundle budle = new Bundle();
										budle.putStringArray("CARDINFO", result);
										DeliveryActivity.ParseMessage(budle);
									}
									break;
								case MyConfig.SIGN:// 签收
									Log.d("SendThread", "签收操作");
									String[] cardinfo_sign = ReaderOperating.readCard(sreMsg);//执行读卡
									if (cardinfo_sign != null && cardinfo_sign[1] != null) {
										String[] result = new String[3];
										if(!"00000000".equals(cardinfo_sign[1])){
											//String card_type = cardinfo_d[1].substring(0, 2);
											CarduserDBUtil carduserDBUtil = new CarduserDBUtil(context);
											//验证卡片是否为合法卡片
											//if (!"25".equals(card_type)&&!"56".equals(card_type)){
											String checkResult = carduserDBUtil.checkCardsn(cardinfo_sign[1]);
											System.out.println("校验结果："+checkResult);
											if(checkResult.length() == 20){
												result[0] = cardinfo_sign[1];
												result[1] = formatDate;
												result[2] = "签收完成";
												GoodsFollowDBUtil goodsFollowDBUtil = new GoodsFollowDBUtil(context);
												ContentValues values = new ContentValues();
												values.put("ordernumber", checkResult);
												values.put("currentstation", MyConfig.getNOW_STATION_ID());
												values.put("information", "签收完成");
												values.put("handletime", Tools.formatDateString2(Tools.getDateFromString(formatDate)));
												values.put("operater", MyConfig.getNOW_LOGINID());
												values.put("state", 1);
												
												boolean flag = goodsFollowDBUtil.addGoodsFollow(values);
												if(flag){
													carduserDBUtil.sign(cardinfo_sign[1]);//改变卡状态
												}
											}else if("-1".equals(checkResult)){
												result[0] = cardinfo_sign[1];
												result[1] = formatDate;
												result[2] = "非法卡";
											}else if("-2".equals(checkResult)){
												result[0] = cardinfo_sign[1];
												result[1] = formatDate;
												result[2] = "验证失败";
											}
										}else {
											result[0] = "";
											result[1] = formatDate;
											result[2] = "非法卡";
										}
										/*} else {
											result[0] = cardinfo_d[1];
											result[1] = formatDate;
											result[2] = "非法用户";
										}*/
										//更新界面
										Bundle budle = new Bundle();
										budle.putStringArray("CARDINFO", result);
										SignActivity.ParseMessage(budle);
									}
									break;
									default:
										break;
							}

						}
					}
				}
			} else {
				ReaderConfig.FRIST_CARD_ID_S = null;
				Log.d("pasteFile", "无卡片！");
				
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}