package com.xunfang.experiment.logistics.service;

import java.math.BigDecimal;

import com.xunfang.experiment.logistics.util.ReaderConfig;


/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description：双模读头操作数据处理类
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
public class ReaderDataProcess {
	/**
	 * 拼接操作指令
	 * 
	 * @param parms
	 *            操作类型
	 * @param cType
	 *            卡类型
	 * @param reMsg
	 *            返回数据（13.56卡号）
	 * @param wData
	 *            写块数据内容（卡号、金额）
	 * @param channel
	 *            开启的通道编号
	 * @return byte[]
	 */
	public static byte[] sendMsg(int parms, byte cType, byte[] reMsg,
			byte[] wData, byte channel) {
		byte[] senByte = null;
		switch (parms) {
		case ReaderConfig.M_SEARCH_CARD:// 寻卡指令
			byte[] data = new byte[10];
			data[0] = 0x5B;
			data[1] = 0x10;
			data[2] = 0x00;
			data[3] = 0x04;
			data[4] = 0x00;
			data[5] = 0x03;
			data[6] = 0x00;
			data[7] = 0x00;
			data[8] = 0x17;
			data[9] = (byte) 0xCB;
			senByte = data;
			break;
		case ReaderConfig.M_SELECT_CARD:// 选择卡
			byte[] data1 = new byte[15];
			data1[0] = 0x5B;
			data1[1] = 0x20;
			data1[2] = 0x00;
			data1[3] = 0x09;
			data1[4] = 0x00;
			data1[5] = 0x00;
			data1[6] = 0x07;
			data1[7] = 0x39;
			data1[8] = reMsg[5];
			data1[9] = reMsg[6];
			data1[10] = reMsg[7];
			data1[11] = reMsg[8];
			byte[] dataCheckdata = new byte[data1[6] + 1];
			System.arraycopy(data1, 4, dataCheckdata, 0, 8);
			data1[12] = getMsgCheck(dataCheckdata);// 数据校验
			byte[] xorCheckdata = new byte[data1.length - 3];
			System.arraycopy(data1, 1, xorCheckdata, 0, data1.length - 3);
			data1[13] = getMsgCheck(xorCheckdata);// 异或校验
			data1[14] = (byte) 0xCB;
			senByte = data1;
			break;
		case ReaderConfig.M_KEY_CERTIFICATION_CARD:// 密钥验证
			if (cType == 1) {
				byte[] data2 = new byte[19];
				data2[0] = 0x5B;
				data2[1] = 0x20;
				data2[2] = 0x00;
				data2[3] = 0x0D;
				data2[4] = 0x00;
				data2[5] = 0x00;
				data2[6] = 0x0B;
				data2[7] = 0x4A;
				data2[8] = 0x60;
				data2[9] = 0x07;
				data2[10] = (byte) 0xFF;
				data2[11] = (byte) 0xFF;
				data2[12] = (byte) 0xFF;
				data2[13] = (byte) 0xFF;
				data2[14] = (byte) 0xFF;
				data2[15] = (byte) 0xFF;
				data2[16] = 0x26;
				data2[17] = 0x2D;
				data2[18] = (byte) 0xCB;
				senByte = data2;
			} else if (cType == 2) {
				byte[] data2 = new byte[23];
				data2[0] = 0x5B;
				data2[1] = 0x30;
				data2[2] = 0x00;
				data2[3] = 0x11;
				data2[4] = 0x5A;
				data2[5] = 0x20;
				data2[6] = 0x00;
				data2[7] = 0x0B;
				data2[8] = 0x41;
				data2[9] = 0x08;
				data2[10] = 0x01;
				data2[11] = 0x01;
				data2[12] = 0x06;
				data2[13] = (byte) 0xFF;
				data2[14] = (byte) 0xFF;
				data2[15] = (byte) 0xFF;
				data2[16] = (byte) 0xFF;
				data2[17] = (byte) 0xFF;
				data2[18] = (byte) 0xFF;
				data2[19] = 0x64;
				data2[20] = (byte) 0xCA;
				data2[21] = (byte) 0xB1;
				data2[22] = (byte) 0xCB;
				senByte = data2;
			}
			break;
		case ReaderConfig.M_READ_DATA_CARD:// 读数据块
			if (cType == 1) {
				byte[] data3 = new byte[12];
				data3[0] = 0x5B;
				data3[1] = 0x20;
				data3[2] = 0x00;
				data3[3] = 0x06;
				data3[4] = 0x00;
				data3[5] = 0x00;
				data3[6] = 0x04;
				data3[7] = 0x4B;
				data3[8] = 0x04;
				data3[9] = 0x4B;
				data3[10] = 0x26;
				data3[11] = (byte) 0xCB;
				senByte = data3;
			} else if (cType == 2) {
				byte[] data3 = new byte[17];
				data3[0] = 0x5B;
				data3[1] = 0x30;
				data3[2] = 0x00;
				data3[3] = 0x0B;
				data3[4] = 0x5A;
				data3[5] = 0x20;
				data3[6] = 0x00;
				data3[7] = 0x05;
				data3[8] = 0x41;
				data3[9] = 0x02;
				data3[10] = 0x01;
				data3[11] = 0x00;
				data3[12] = 0x10;
				data3[13] = 0x77;
				data3[14] = (byte) 0xCA;
				data3[15] = (byte) 0xAB;
				data3[16] = (byte) 0xCB;
				senByte = data3;
			}
			break;
		case ReaderConfig.M_WRITE_DATA_CARD:// 写数据块
			if (cType == 1) {
				byte[] data4 = new byte[29];
				data4[0] = 0x5B;
				data4[1] = 0x20;
				data4[2] = 0x00;
				data4[3] = 0x16;
				data4[4] = 0x00;
				data4[5] = 0x00;
				data4[6] = 0x14;
				data4[7] = 0x4C;
				data4[8] = 0x04;
				data4[9] = 0x6B;
				data4[10] = 0x71;
				data4[11] = wData[0];// 卡号
				data4[12] = wData[1];
				data4[13] = wData[2];
				data4[14] = wData[3];
				data4[15] = wData[4];
				data4[16] = wData[5];
				data4[17] = wData[6];
				data4[18] = wData[7];
				data4[19] = wData[8];// 金额
				data4[20] = wData[9];
				data4[21] = wData[10];
				data4[22] = wData[11];
				data4[23] = 0x00;// 默认值
				data4[24] = 0x00;
				byte[] dataCheckdata4 = new byte[21];
				System.arraycopy(data4, 4, dataCheckdata4, 0, 21);
				data4[25] = getMsgCheck(dataCheckdata4);// 校验位
				byte[] xorCheckdata4 = new byte[data4.length - 3];
				System.arraycopy(data4, 1, xorCheckdata4, 0, data4.length - 3);
				data4[26] = getMsgCheck(xorCheckdata4);// 异或校验位
				data4[27] = (byte) 0xCB;
				senByte = data4;
			} else if (cType == 2) {
				byte[] data4 = new byte[33];
				data4[0] = 0x5B;
				data4[1] = 0x30;
				data4[2] = 0x00;
				data4[3] = 0x1B;
				data4[4] = 0x5A;
				data4[5] = 0x20;
				data4[6] = 0x00;
				data4[7] = 0x15;
				data4[8] = 0x41;
				data4[9] = 0x02;
				data4[10] = 0x02;
				data4[11] = 0x00;
				data4[12] = 0x10;
				data4[13] = 0x6B;
				data4[14] = 0x71;
				data4[15] = wData[0];
				data4[16] = wData[1];
				data4[17] = wData[2];
				data4[18] = wData[3];
				data4[19] = wData[4];
				data4[20] = wData[5];
				data4[21] = wData[6];
				data4[22] = wData[7];
				data4[23] = wData[8];// 金额
				data4[24] = wData[9];
				data4[25] = wData[10];
				data4[26] = wData[11];
				data4[27] = 0x00;// 默认值
				data4[28] = 0x00;
				byte[] dataCheckdata4 = new byte[24];
				System.arraycopy(data4, 5, dataCheckdata4, 0, 24);
				data4[29] = getMsgCheck(dataCheckdata4);// 校验位
				data4[30] = (byte) 0xCA;
				byte[] xorCheckdata4 = new byte[data4.length - 3];
				System.arraycopy(data4, 1, xorCheckdata4, 0, data4.length - 3);
				data4[31] = getMsgCheck(xorCheckdata4);// 异或校验位
				data4[32] = (byte) 0xCB;
				senByte = data4;
			}
			break;
		case ReaderConfig.M_IDLE_CARD:// 空闲指令
			byte[] data9 = new byte[10];
			data9[0] = 0x5B;
			data9[1] = 0x11;
			data9[2] = 0x00;
			data9[3] = 0x04;
			data9[4] = 0x00;
			data9[5] = 0x00;
			data9[6] = 0x00;
			data9[7] = 0x00;
			data9[8] = 0x15;
			data9[9] = (byte) 0xCB;
			senByte = data9;
			break;
		case ReaderConfig.G_OPEN_CHANNEL:// 开通道
			byte[] data10 = new byte[17];
			data10[0] = 0x5B;
			data10[1] = 0x30;
			data10[2] = 0x00;
			data10[3] = 0x0B;
			data10[4] = 0x5A;
			data10[5] = 0x20;
			data10[6] = 0x00;
			data10[7] = 0x05;
			data10[8] = 0x00;
			data10[9] = 0x70;
			data10[10] = 0x00;
			data10[11] = 0x00;
			data10[12] = 0x01;
			data10[13] = 0x54;
			data10[14] = (byte) 0xCA;
			data10[15] = (byte) 0xAB;
			data10[16] = (byte) 0xCB;
			senByte = data10;
			break;
		case ReaderConfig.G_CLOSE_CHANNEL:// 关通道
			byte[] data11 = new byte[17];
			data11[0] = 0x5B;
			data11[1] = 0x30;
			data11[2] = 0x00;
			data11[3] = 0x0B;
			data11[4] = 0x5A;
			data11[5] = 0x20;
			data11[6] = 0x00;
			data11[7] = 0x05;
			data11[8] = 0x00;
			data11[9] = 0x70;
			data11[10] = (byte) 0x80;
			data11[11] = channel;
			data11[12] = 0x00;
			byte[] dataCheckdata9 = new byte[8];
			System.arraycopy(data11, 5, dataCheckdata9, 0, 8);
			data11[13] = getMsgCheck(dataCheckdata9);// 校验位
			data11[14] = (byte) 0xCA;
			byte[] xorCheckdata9 = new byte[data11.length - 3];
			System.arraycopy(data11, 1, xorCheckdata9, 0, data11.length - 3);
			data11[15] = getMsgCheck(xorCheckdata9);// 异或校验位
			data11[16] = (byte) 0xCB;
			senByte = data11;
			break;
		case ReaderConfig.G_SELECT_AP:// 选择应用
			byte[] data12 = new byte[28];
			data12[0] = 0x5B;
			data12[1] = 0x30;
			data12[2] = 0x00;
			data12[3] = 0x16;
			data12[4] = 0x5A;
			data12[5] = 0x20;
			data12[6] = 0x00;
			data12[7] = 0x10;
			data12[8] = (byte) 0x91;
			data12[9] = (byte) 0xA4;
			data12[10] = 0x04;
			data12[11] = (byte) 0xF0;
			data12[12] = 0x0B;
			data12[13] = 0x4D;
			data12[14] = 0x69;
			data12[15] = 0x66;
			data12[16] = 0x61;
			data12[17] = 0x72;
			data12[18] = 0x65;
			data12[19] = 0x20;
			data12[20] = 0x41;
			data12[21] = 0x70;
			data12[22] = 0x70;
			data12[23] = 0x31;
			data12[24] = (byte) 0x9E;
			data12[25] = (byte) 0xCA;
			data12[26] = (byte) 0xB6;
			data12[27] = (byte) 0xCB;
			senByte = data12;
			break;
		case ReaderConfig.M_RESET_AREA_CARD://初始化数据块（数据）
			byte[] data13=new byte[12];
			data13[0]=0x00;
			data13[1]=0x00;
			data13[2]=0x00;
			data13[3]=0x00;
			data13[4]=0x00;
			data13[5]=0x00;
			data13[6]=0x00;
			data13[7]=0x00;
			data13[8]=0x00;
			data13[9]=0x00;
			data13[10]=0x00;
			data13[11]=0x00;
			senByte = data13;
			break;
		default:
		}

		return senByte;
	}

	/**
	 * 指令异或校验
	 * 
	 * @param msg
	 *            指令内容
	 * @return byte
	 */
	public static byte getMsgCheck(byte[] msg) {
		int msgcheck = 0;
		for (int i = 0; i < msg.length; i++) {
			if (i == 0) {
				msgcheck = msg[i];
			} else {
				msgcheck = msgcheck ^ (msg[i] & 0xFF);
			}
		}
		return (byte) msgcheck;
	}

	/**
	 * 获取卡信息
	 * 
	 * @param reMsg
	 *            卡块数据
	 * @return String[]
	 */
	public static String[] getcardinfo(byte[] reMsg) {
		String[] cardinfo = new String[3];
		cardinfo[0] = ReaderConfig.FRIST_CARD_ID;// 原始卡号
		cardinfo[1] = getCardsn(reMsg);// 卡序号
		cardinfo[2] = getMoney(reMsg);// 金额
		return cardinfo;
	}

	/**
	 * 获取卡内金额
	 * 
	 * @param reMsg
	 *            卡块数据
	 * @return String
	 */
	public static String getMoney(byte[] reMsg) {
		float sMoney = 0;
		int sMoney1 = 0;
		int sMoney2 = 0;
		int sMoney3 = 0;
		int sMoney4 = 0;
		if (reMsg[1] == 0x20 && reMsg.length > 23) {// 13.56M卡
			sMoney1 = (reMsg[19]&0xFF) << 24;
			sMoney2 = (reMsg[20]&0xFF) << 16;
			sMoney3 = (reMsg[21]&0xFF) << 8;
			sMoney4 = reMsg[22]&0xFF;
			//sMoney = (sMoney1 + sMoney2 + sMoney3 + sMoney4) / 100.00f;
			// sMoney = (reMsg[19] << 24 + reMsg[20] << 16 + reMsg[21] << 8 +
			// reMsg[22]) / 100;
		} else if (reMsg[1] == 0x30 && reMsg.length > 22) {// 2.4G卡
			sMoney1 = (reMsg[18]&0xFF) << 24;
			sMoney2 = (reMsg[19]&0xFF) << 16;
			sMoney3 = (reMsg[20]&0xFF) << 8;
			sMoney4 = reMsg[21]&0xFF;
			//sMoney = (sMoney1 + sMoney2 + sMoney3 + sMoney4) / 100.00f;
			
			
//			sMoney = (reMsg[23] << 24 + reMsg[24] << 16 + reMsg[25] << 8 + reMsg[26]) / 100;
		}
		BigDecimal b1 = new BigDecimal(Integer.toString(sMoney1 + sMoney2 + sMoney3 + sMoney4));
    	BigDecimal b2 = new BigDecimal("100.00");
    	
    	sMoney = b1.divide(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//卡内的金额
    	
		return Float.toString(sMoney);
	}

	/**
	 * 获取卡序号
	 * 
	 * @param reMsg
	 *            卡块数据
	 * @return String
	 */
	public static String getCardsn(byte[] reMsg) {
		String cardsn = null;
		byte[] cardsn_byte = new byte[8];
		if (reMsg[1] == 0x20 && reMsg.length > 19) {// 13.56M卡
			System.arraycopy(reMsg, 11, cardsn_byte, 0, 8);// 将卡序号复制到新数组
			for (int i = 0; i < cardsn_byte.length; i++) {
				if (cardsn_byte[i] < 48) {
					cardsn_byte[i] = (byte) (cardsn_byte[i] + 48);// 转换为0-9的数字
				}
			}
			cardsn = new String(cardsn_byte);
		} else if (reMsg[1] == 0x30 && reMsg.length > 17) {// 2.4G卡
			System.arraycopy(reMsg, 10, cardsn_byte, 0, 8);// 将卡序号复制到新数组
			for (int i = 0; i < cardsn_byte.length; i++) {
				if (cardsn_byte[i] < 48) {
					cardsn_byte[i] = (byte) (cardsn_byte[i] + 48);// 转换为0-9的数字
				}
			}
			cardsn = new String(cardsn_byte);
		}
		return cardsn;
	}

	/**
	 * 获取原始卡号
	 * 
	 * @param reMsg
	 *            寻卡返回数据
	 * @return String
	 */
	public static String getFristCardID(byte[] reMsg) {
		String fristCardID = null;
		byte[] cardid = new byte[8];
		if(reMsg!=null){
		if (reMsg[4] == 0x01 && reMsg.length > 8) {// 13.56M(将原始卡号拆分成8位0-9或a-z之间)
			/*if ((reMsg[5] & 0x0F) > 9) {
				cardid[0] = (byte) ((reMsg[5] & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
			} else {
				cardid[0] = (byte) ((reMsg[5] & 0x0F) + 48);// 将原始卡号转换为0-9之间
			}
			if (((reMsg[5] >> 4) & 0x0F) > 9) {
				cardid[1] = (byte) (((reMsg[5] >> 4) & 0x0F) - 10 + 97);
			} else {
				cardid[1] = (byte) (((reMsg[5] >> 4) & 0x0F) + 48);
			}

			if ((reMsg[6] & 0x0F) > 9) {
				cardid[2] = (byte) ((reMsg[6] & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
			} else {
				cardid[2] = (byte) ((reMsg[6] & 0x0F) + 48);// 将原始卡号转换为0-9之间
			}
			if (((reMsg[6] >> 4) & 0x0F) > 9) {
				cardid[3] = (byte) (((reMsg[6] >> 4) & 0x0F) - 10 + 97);
			} else {
				cardid[3] = (byte) (((reMsg[6] >> 4) & 0x0F) + 48);
			}

			if ((reMsg[7] & 0x0F) > 9) {
				cardid[4] = (byte) ((reMsg[7] & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
			} else {
				cardid[4] = (byte) ((reMsg[7] & 0x0F) + 48);// 将原始卡号转换为0-9之间
			}
			if (((reMsg[7] >> 4) & 0x0F) > 9) {
				cardid[5] = (byte) (((reMsg[7] >> 4) & 0x0F) - 10 + 97);
			} else {
				cardid[5] = (byte) (((reMsg[7] >> 4) & 0x0F) + 48);
			}

			if ((reMsg[8] & 0x0F) > 9) {
				cardid[6] = (byte) ((reMsg[8] & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
			} else {
				cardid[6] = (byte) ((reMsg[8] & 0x0F) + 48);// 将原始卡号转换为0-9之间
			}
			if (((reMsg[8] >> 4) & 0x0F) > 9) {
				cardid[7] = (byte) (((reMsg[8] >> 4) & 0x0F) - 10 + 97);
			} else {
				cardid[7] = (byte) (((reMsg[8] >> 4) & 0x0F) + 48);
			}*/
			for(int i =0;i<4;i++){
				if ((reMsg[i+5] & 0x0F) > 9) {
					cardid[i*2] = (byte) ((reMsg[i+5] & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
				} else {
					cardid[i*2] = (byte) ((reMsg[i+5] & 0x0F) + 48);// 将原始卡号转换为0-9之间
				}
				if (((reMsg[i+5] >> 4) & 0x0F) > 9) {
					cardid[i*2+1] = (byte) (((reMsg[i+5] >> 4) & 0x0F) - 10 + 97);// 将原始卡号转换为a-z之间
				} else {
					cardid[i*2+1] = (byte) (((reMsg[i+5] >> 4) & 0x0F) + 48);// 将原始卡号转换为0-9之间
				}
			}
			// 将原始卡号转换为字符串
			fristCardID = new String(cardid);
		} else if (reMsg[4] == 0x02 && reMsg.length > 12) {// 2.4G卡
			System.arraycopy(reMsg, 5, cardid, 0, 8);
			for (int i = 0; i < cardid.length; i++) {
				while (!(((cardid[i] & 0xFF) < 58 && (cardid[i] & 0xFF) > 47) || ((cardid[i] & 0xFF) < 123 && (cardid[i] & 0xFF) > 97))) {// 变到0~9//
					cardid[i] += 25;
				}
			}
			// 将原始卡号转换为字符串
			fristCardID = new String(cardid);
		}
		}
		return fristCardID;
	}

	/**
	 * 获取卡内能保存的金额
	 * 
	 * @param money
	 *            金额
	 * @return byte[]
	 */
	public static byte[] getCardMoney(float money) {
		//int cardmoney = (int) (money * 100);
		BigDecimal b1 = new BigDecimal(Float.toString(money));
    	BigDecimal b2 = new BigDecimal("100.00");
    	
    	int cardmoney = (int)b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();//写入卡的金额
		
		System.out.println("写入金额-->"+cardmoney);
		byte[] money_array = new byte[4];
		int m1 = 0;
		int m2 = 0;
		int m3 = 0;
		int m4 = 0;
		if (money > 0x00FFFFFF) {
			m1 = cardmoney >> 24;
			m2 = (cardmoney & 0x00FFFFFF) >> 16;
			m3 = (cardmoney & 0x0000FFFF) >> 8;
			m4 = cardmoney & 0x000000FF;
		} else if (cardmoney > 0x0000FFFF) {
			m1 = 0x00;
			m2 = cardmoney >> 16;
			m3 = (cardmoney & 0x0000FFFF) >> 8;
			m4 = cardmoney & 0x000000FF;
		} else if (cardmoney > 0x000000FF) {
			m1 = 0x00;
			m2 = 0x00;
			m3 = cardmoney >> 8;
			m4 = cardmoney & 0x000000FF;
		} else {
			m1 = 0x00;
			m2 = 0x00;
			m3 = 0x00;
			m4 = cardmoney;
		}
		money_array[0] = (byte) m1;
		money_array[1] = (byte) m2;
		money_array[2] = (byte) m3;
		money_array[3] = (byte) m4;
		return money_array;
	}

}
