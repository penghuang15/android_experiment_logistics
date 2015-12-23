package com.xunfang.experiment.logistics.service;

import java.math.BigDecimal;

import android.util.Log;

import com.xunfang.experiment.logistics.so.Linuxc;
import com.xunfang.experiment.logistics.util.ReaderConfig;
/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description: 双模读头操作类
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
public class ReaderOperating {
	/**
	 * 寻卡
	 * 
	 * @return byte[] 寻卡返回值
	 */
	public  synchronized static byte[] searchCard() {
		byte channel = 0;
		byte cType = 0;
		byte[] sr_value = null;
		if (ReaderConfig.getUART_STATE() > 0) {
			byte[] searchCard = ReaderDataProcess.sendMsg(
					ReaderConfig.M_SEARCH_CARD, cType, null, null, channel);// 获取寻卡指令
			Linuxc.sendByteUart(searchCard);// 发送寻卡指令
			sr_value = getDataofSearchCard();// 获取串口数据
		} else {
			//System.out.println("Uart-open-error");
			Log.d("searchCardLog", "Uart-open-error");
		}
		return sr_value;
	}

	/**
	 * 读卡(13.56M、2.4G)
	 * 
	 * @param cardData
	 * @return String[] 卡信息（原始卡号、卡序号、金额）
	 */
	public synchronized static String[] readCard(byte[] cardData) {
		Log.d("mydebug", "read-----card");
		byte channel = 0;
		byte cType = 0;
		String[] cardinfo = null;// 卡信息
		byte[] reMsg = cardData;// 卡数据
		if (reMsg != null && reMsg[4] != 0x05
				&& reMsg[reMsg.length - 3] == 0x00) { // 判断是否寻到卡
			ReaderConfig.FRIST_CARD_ID = ReaderDataProcess
					.getFristCardID(reMsg);// 获取原始卡号
			ReaderConfig.FRIST_CARD_ID_S = ReaderConfig.FRIST_CARD_ID;
			cType = reMsg[4];
			if (cType == 0x01) {// 13.56M读卡
				byte[] selectOrder = ReaderDataProcess
						.sendMsg(ReaderConfig.M_SELECT_CARD, cType, reMsg,
								null, channel);// 获取选卡指令
				Linuxc.sendByteUart(selectOrder);// 发送选卡指令
				reMsg = getUartData();// 获取串口数据
				if (reMsg != null && reMsg.length > 8 && reMsg[7] == 0x39
						&& reMsg[reMsg.length - 3] == 0x00) {// 判断选卡是否成功
					byte[] certification = ReaderDataProcess.sendMsg(
							ReaderConfig.M_KEY_CERTIFICATION_CARD, cType, null,
							null, channel);// 获取卡密钥认证指令
					Linuxc.sendByteUart(certification);// 发送认证指令
					reMsg = getUartData();// 获取串口数据
					if (reMsg != null && reMsg.length > 8 && reMsg[7] == 0x4A && reMsg[9] == 0x4E
							&& reMsg[8] == 0x00 && reMsg[reMsg.length - 3] == 0x00) {// 判断卡密钥认证是否成功
						byte[] readCard = ReaderDataProcess.sendMsg(
								ReaderConfig.M_READ_DATA_CARD, cType, null,
								null, channel);// 获取读卡命令
						Linuxc.sendByteUart(readCard);// 发送读数据块指令
						reMsg = getUartData();// 获取串口数据
						if (reMsg != null && reMsg.length > 8
								&& reMsg[7] == 0x4B
								&& reMsg[reMsg.length - 3] == 0x00) {
/*							byte[] leisure = ReaderDataProcess.sendMsg(
									ReaderConfig.M_IDLE_CARD, cType, null,
									null, channel);// 获取空闲指令
							Linuxc.sendByteUart(leisure);// 发送空闲指令
*/							cardinfo = ReaderDataProcess.getcardinfo(reMsg);// 返回数据
						} else {
							Log.d("readCardLog","读数据块失败！");
						}
					} else {
						Log.d("readCardLog","认证失败！");
					}
				} else {
					Log.d("readCardLog","选卡失败！");
				}
			} else if (cType == 0x02) {// 2.4G读卡
				
				byte[] openChannel = ReaderDataProcess.sendMsg(
						ReaderConfig.G_OPEN_CHANNEL, cType, reMsg, null,
						channel);// 获取开通道指令
				Linuxc.sendByteUart(openChannel);// 发送开通道指令
				reMsg = getUartData();// 获取串口数据
				if (reMsg != null && reMsg.length > 8
						&& reMsg[reMsg.length - 8] == (byte) 0x90
						&& reMsg[reMsg.length - 7] == 0x00
						&& reMsg[reMsg.length - 6] == 0x00
						&& reMsg[reMsg.length - 3] == 0x00) {// 判断开通道是否成功
					channel = reMsg[8];// 通道编号
					byte[] selectAp = ReaderDataProcess.sendMsg(
							ReaderConfig.G_SELECT_AP, cType, null, null,
							channel);// 获取选择应用指令
					Linuxc.sendByteUart(selectAp);// 发送选择应用指令
					reMsg = getUartData();// 获取串口数据
					if (reMsg != null && reMsg.length > 8
							&& reMsg[reMsg.length - 8] == (byte) 0x90
							&& reMsg[reMsg.length - 7] == 0x00
							&& reMsg[reMsg.length - 6] == 0x00
							&& reMsg[reMsg.length - 3] == 0x00) {// 判断选择应用是否成功
						byte[] certification = ReaderDataProcess.sendMsg(
								ReaderConfig.M_KEY_CERTIFICATION_CARD, cType,
								null, null, channel);// 密钥认证指令
						Linuxc.sendByteUart(certification);// 发送密钥认证指令
						reMsg = getUartData();// 获取串口数据
						if (reMsg != null && reMsg.length > 8
								&& reMsg[reMsg.length - 8] == (byte) 0x90
								&& reMsg[reMsg.length - 7] == 0x00
								&& reMsg[reMsg.length - 6] == 0x00
								&& reMsg[reMsg.length - 3] == 0x00) {// 判断密钥认证是否成功

							byte[] readCard = ReaderDataProcess.sendMsg(
									ReaderConfig.M_READ_DATA_CARD, cType, null,
									null, channel);// 获取读数据块指令
							Linuxc.sendByteUart(readCard);// 发送读数据块指令
							reMsg = getUartData();// 获取串口数据
							if (reMsg != null && reMsg.length > 8
									&& reMsg[reMsg.length - 8] == (byte) 0x90
									&& reMsg[reMsg.length - 7] == 0x00
									&& reMsg[reMsg.length - 6] == 0x00
									&& reMsg[reMsg.length - 3] == 0x00) {// 判断读数据块是否成功
								cardinfo = ReaderDataProcess.getcardinfo(reMsg);// 返回数据								
								byte[] closeChannel = ReaderDataProcess
										.sendMsg(ReaderConfig.G_CLOSE_CHANNEL,
												cType, null, null, channel);// 获取关闭通道指令
								Linuxc.sendByteUart(closeChannel);// 发送关闭通道指令
							} else {
								Log.d("readCardLog","读数据块失败！");
							}
						} else {
							Log.d("readCardLog","密钥认证失败！");
						}
					} else {
						Log.d("readCardLog","选择应用失败！");
					}
				} else {
					Log.d("readCardLog","开通道失败！");
				}
			}
		} else {
			Log.d("readCardLog","寻卡失败！");
		}
		return cardinfo;
	}

	/**
	 * 写卡(13.56M、2.4G)
	 * 
	 * @param cardData
	 *            卡数据
	 * @param cardsn
	 *            卡序号
	 * @param money
	 *            金额
	 * @return boolean 写卡是否成功
	 */
	public synchronized static boolean writeCard(byte[] cardData, String cardsn, String money) {
		 Log.d("mydebug", "write-----card");
		byte channel = 0;
		byte cType = 0;
		byte[] cardsn_b = cardsn.getBytes();
		byte[] money_b = ReaderDataProcess.getCardMoney(Float.valueOf(money));
		byte[] cardinfo = new byte[cardsn_b.length + money_b.length];
		System.arraycopy(cardsn_b, 0, cardinfo, 0, cardsn_b.length);
		System.arraycopy(money_b, 0, cardinfo, cardsn_b.length, money_b.length);
		byte[] reMsg = cardData;// 卡数据
		boolean flag = false;
		if (reMsg != null && reMsg[4] != 0x05
				&& reMsg[reMsg.length - 3] == 0x00) {// 判断是否寻到卡
			ReaderConfig.FRIST_CARD_ID = ReaderDataProcess
					.getFristCardID(reMsg);// 获取原始卡号
			ReaderConfig.FRIST_CARD_ID_S = ReaderConfig.FRIST_CARD_ID;
			cType = reMsg[4];
			if (cType == 0x01) {// 13.56M写卡
				byte[] selectOrder = ReaderDataProcess
						.sendMsg(ReaderConfig.M_SELECT_CARD, cType, reMsg,
								null, channel);// 获取选卡指令
				Linuxc.sendByteUart(selectOrder);// 发送选卡指令
				reMsg = getUartData();// 获取串口数据		
				if (reMsg != null && reMsg.length > 8 && reMsg[7] == 0x39
						&& reMsg[reMsg.length - 3] == 0x00) {// 判断选卡是否成功
					byte[] certification = ReaderDataProcess.sendMsg(
							ReaderConfig.M_KEY_CERTIFICATION_CARD, cType, null,
							null, channel);// 获取卡密钥认证指令
					Linuxc.sendByteUart(certification);// 发送认证指令
					reMsg = getUartData();// 获取串口数据
					if (reMsg != null && reMsg.length > 8 && reMsg[7] == 0x4A
							&& reMsg[reMsg.length - 3] == 0x00) {// 判断卡密钥认证是否成功
						byte[] writeCard = ReaderDataProcess.sendMsg(
								ReaderConfig.M_WRITE_DATA_CARD, cType, null,
								cardinfo, channel);// 获取写卡数据块指令
						Linuxc.sendByteUart(writeCard);// 发送写卡数据块指令
						reMsg = getUartData();// 获取串口数据
						if (reMsg != null && reMsg.length > 8
								&& reMsg[7] == 0x4C
								&& reMsg[reMsg.length - 3] == 0x00) {
							flag = true;// 写卡成功
/*							byte[] wleisure = ReaderDataProcess.sendMsg(
									ReaderConfig.M_IDLE_CARD, cType, null,
									null, channel);// 获取空闲指令
							Linuxc.sendByteUart(wleisure);// 发送空闲指令
*/						} else {
	                         Log.d("writeCardLog", "写数据块失败！");
						}
					} else {
						Log.d("writeCardLog", "密钥认证失败！");
					}
				} else {
					Log.d("writeCardLog", "选卡失败！");
				}
			} else if (cType == 0x02) {// 2.4G卡							
				byte[] openChannel = ReaderDataProcess.sendMsg(
						ReaderConfig.G_OPEN_CHANNEL, cType, reMsg, null,
						channel);// 获取开通道指令
				Linuxc.sendByteUart(openChannel);// 发送开通道指令
				reMsg = getUartData();// 获取串口数据
				if (reMsg != null && reMsg.length > 8
						&& reMsg[reMsg.length - 8] == (byte) 0x90
						&& reMsg[reMsg.length - 7] == 0x00
						&& reMsg[reMsg.length - 6] == 0x00
						&& reMsg[reMsg.length - 3] == 0x00) {// 判断开通道是否成功
					channel = reMsg[8];// 通道编号
					byte[] selectAp = ReaderDataProcess.sendMsg(
							ReaderConfig.G_SELECT_AP, cType, null, null,
							channel);// 获取选择应用指令
					Linuxc.sendByteUart(selectAp);// 发送选择应用指令
					reMsg = getUartData();// 获取串口数据
					if (reMsg != null && reMsg.length > 8
							&& reMsg[reMsg.length - 8] == (byte) 0x90
							&& reMsg[reMsg.length - 7] == 0x00
							&& reMsg[reMsg.length - 6] == 0x00
							&& reMsg[reMsg.length - 3] == 0x00) {// 判断选择应用是否成功
						byte[] certification = ReaderDataProcess.sendMsg(
								ReaderConfig.M_KEY_CERTIFICATION_CARD, cType,
								null, null, channel);// 密钥认证指令
						Linuxc.sendByteUart(certification);// 发送密钥认证指令
						reMsg = getUartData();// 获取串口数据
						if (reMsg != null && reMsg.length > 8
								&& reMsg[reMsg.length - 8] == (byte) 0x90
								&& reMsg[reMsg.length - 7] == 0x00
								&& reMsg[reMsg.length - 6] == 0x00
								&& reMsg[reMsg.length - 3] == 0x00) {// 判断密钥认证是否成功
							byte[] writeCard = ReaderDataProcess.sendMsg(
									ReaderConfig.M_WRITE_DATA_CARD, cType,
									null, cardinfo, channel);// 获取写数据块指令
							Linuxc.sendByteUart(writeCard);// 发送写数据块指令
							reMsg = getUartData();// 获取串口数据							
							if (reMsg != null && reMsg.length > 8
									&& reMsg[reMsg.length - 8] == (byte) 0x90
									&& reMsg[reMsg.length - 7] == 0x00
									&& reMsg[reMsg.length - 6] == 0x00
									&& reMsg[reMsg.length - 3] == 0x00) {// 判断读数据块是否成功
								flag = true;// 写卡成功
								byte[] closeChannel = ReaderDataProcess
										.sendMsg(ReaderConfig.G_CLOSE_CHANNEL,
												cType, null, null, channel);// 获取关闭通道指令
								Linuxc.sendByteUart(closeChannel);// 发送关闭通道指令
							} else {
								Log.d("writeCardLog", "写数据块失败！");
							}
						} else {
							Log.d("writeCardLog", "密钥认证失败！");
						}
					} else {
						Log.d("writeCardLog", "选择应用失败！");
					}
				} else {
					Log.d("writeCardLog", "打开通道失败！");
				}
			}
		} else {
			Log.d("writeCardLog", "寻卡失败！");
		}
		return flag;
	}

	/**
	 * 机具消费
	 * 
	 * @param money
	 *            消费金额 、卡信息(原始卡号、卡序号、卡金额)
	 * @return 卡余额
	 */
	public synchronized static String gwConsumption(String money,String[] cardinfo_s) {
		String card_money = null;// 返回数据信息
		float cardmoney_s = 0;// 写卡钱的卡金额
		float cardmoney_e_p = 0;// 消费后的金额
		byte[] cardData = null;// 寻卡返回数据
		cardmoney_s = Float.valueOf(cardinfo_s[2]);
		//cardmoney_e_p = (cardmoney_s*100 - (Float.valueOf(money))*100)/100;// 获得消费后的金额
		
		BigDecimal b1 = new BigDecimal(cardinfo_s[2]);
    	BigDecimal b2 = new BigDecimal(money);
    	
    	cardmoney_e_p = b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//获取消费后的金额
		
    	Log.d("tdebug", "卡内余额："+cardinfo_s[2]);
		Log.d("tdebug", "消费金额："+money);
		Log.d("tdebug", "消费后余额："+cardmoney_e_p);
		if (cardmoney_e_p < 0) {//余额不足
			return "moneyNotEnough";
		} else {
			cardData = searchCard();// 寻卡
			//处理2.4G二次寻卡时返回错误数据
			if(cardData!=null&&cardData[1]==0x30){
				cardData = searchCard();// 寻卡
			}else  if(cardData!=null&&cardData[4]==0x04){
				   cardData = searchCard();// 寻卡
			   }
			boolean flag = writeCard(cardData, cardinfo_s[1],
					Float.toString(cardmoney_e_p));// 写卡
			if (flag) {
				card_money=Float.toString(cardmoney_e_p);
			} else {
				Log.d("consumptionLog", "消费写卡失败");
			}
		}
		return card_money;
	}

	/**
	 * 充值
	 * 
	 * @param money
	 *            充值金额 、卡信息(原始卡号、卡序号、卡金额)
	 * @return 卡余额
	 */
	public synchronized static String gwRecharge(String money,String[] cardinfo_s) {
		String card_money= null;// 返回数据信息
		//float cardmoney_s = 0;// 写卡前的卡金额
		float cardmoney_e_p = 0;// 充值后的金额
     	byte[] cardData = null;// 寻卡返回数据
		//cardmoney_s = Float.valueOf(cardinfo_s[2]);	
		//cardmoney_e_p = (cardmoney_s*100 + (Float.valueOf(money))*100)/100;// 获得充值后的金额
		
		BigDecimal b1 = new BigDecimal(cardinfo_s[2]);
    	BigDecimal b2 = new BigDecimal(money);
    	
    	cardmoney_e_p = b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//获取充值后的金额
    	
    	Log.d("tdebug", "原卡内余额："+cardinfo_s[2]);
		Log.d("tdebug", "充值金额："+money);
		Log.d("tdebug", "充值后的卡内余额："+cardmoney_e_p);
		if (cardmoney_e_p >1000) {//卡内金额不能大于1000
			return "moneyOvertop";//new String[] { "充值金额超出卡的最大金额！" };
		} else {
		   cardData = searchCard();// 寻卡
		     //处理2.4G二次寻卡时返回错误数据
			if(cardData!=null&&cardData[1]==0x30){
				cardData = searchCard();// 寻卡
			}else if(cardData!=null&&cardData[4]==0x04){
				   cardData = searchCard();// 寻卡
			   }
			boolean flag = writeCard(cardData, cardinfo_s[1],
					Float.toString(cardmoney_e_p));// 写卡
			if (flag) {
				card_money=Float.toString(cardmoney_e_p);
			} else {
				Log.d("rechargeLog", "充值写卡失败");
			}
		}
		return card_money;
	}

	/**
	 * 接收串口数据
	 * 
	 * @return byte[] 发送指令反馈数据
	 */
	public  static byte[] getUartData() {
		byte[] greMsg = null;
		byte[] greMsg_S = null;
		boolean flag = false;
		long startTime = System.currentTimeMillis();
		while (!flag) {
			byte[] reData = Linuxc.receiveByteUart();// 接收串口数据
			if (System.currentTimeMillis() - startTime > 1100) {// 接收串口数据超时
				flag = true;
			}
			if (reData != null) {// 如果接收到数据
				if (reData[reData.length - 1] == (byte) 0xCB) {// 判断数据是否接收完成
					greMsg = reData;
					if (greMsg_S != null && greMsg_S[0] == 0x5B) {// 消息数据是否存在
						byte[] msg = new byte[greMsg.length + greMsg_S.length];
						System.arraycopy(greMsg_S, 0, msg, 0, greMsg_S.length);
						System.arraycopy(greMsg, 0, msg, greMsg_S.length,
								greMsg.length);
						greMsg = msg;
					}
					flag = true;
				} else {
					greMsg_S = reData;
				}
			}
		}
		return greMsg;
	}
	/**
	 * 接收串口数据
	 * 
	 * @return byte[] 发送指令反馈数据
	 */
	public  static byte[] getDataofSearchCard() {
		byte[] greMsg = null;
		byte[] greMsg_S = null;
		boolean flag = false;
		long startTime = System.currentTimeMillis();
		while (!flag) {
			byte[] reData = Linuxc.receiveByteUart();// 接收串口数据
			if (System.currentTimeMillis() - startTime > 5000) {// 接收串口数据超时
				flag = true;
			}
			if (reData != null) {// 如果接收到数据
				if (reData[reData.length - 1] == (byte) 0xCB) {// 判断数据是否接收完成
					greMsg = reData;
					if (greMsg_S != null && greMsg_S[0] == 0x5B) {// 消息数据是否存在
						byte[] msg = new byte[greMsg.length + greMsg_S.length];
						System.arraycopy(greMsg_S, 0, msg, 0, greMsg_S.length);
						System.arraycopy(greMsg, 0, msg, greMsg_S.length,
								greMsg.length);
						greMsg = msg;
					}
					flag = true;
				} else {
					greMsg_S = reData;
				}
			}
		}
		return greMsg;
	}
}
