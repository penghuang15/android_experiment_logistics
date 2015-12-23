package com.xunfang.experiment.logistics.util;

/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description: 双模读头操作配置文件
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
public class ReaderConfig {
	/**
	 * 读卡数据块 0
	 */
	public static final int M_READ_CARD = 0;
	/**
	 * 写卡数据块 1
	 */
	public static final int M_WRITE_CARD = 1;
	/**
	 * 初始化卡 2
	 */
	public static final int M_RESET_CARD = 2;
	/**
	 * 读卡余额 3
	 */
	public static final int M_READ_CARD_MONEY = 3;
	/**
	 * 充值 4
	 */
	public static final int M_RECHARGE_CARD_MONEY = 4;
	/**
	 * 消费 5
	 */
	public static final int M_CONSUMPTION_CARD_MONEY = 5;

	/**
	 * 寻卡（指令） 10
	 */
	public static final int M_SEARCH_CARD = 10;
	/**
	 * 选择卡（指令） 11
	 */
	public static final int M_SELECT_CARD = 11;
	/**
	 * 密钥认证（指令） 12
	 */
	public static final int M_KEY_CERTIFICATION_CARD = 12;
	/**
	 * 读数据块（指令）13
	 */
	public static final int M_READ_DATA_CARD = 13;
	/**
	 * 写数据块（指令）14
	 */
	public static final int M_WRITE_DATA_CARD = 14;
	/**
	 * 初始化块(数据)15
	 */
	public static final int M_RESET_AREA_CARD = 15;

	/**
	 * 空闲（指令）19
	 */
	public static final int M_IDLE_CARD = 19;
	/**
	 * 开通道（指令）
	 */
	public static final int G_OPEN_CHANNEL = 20;
	/**
	 * 关通道（指令）
	 */
	public static final int G_CLOSE_CHANNEL = 21;
	/**
	 * 选择应用（指令）
	 */
	public static final int G_SELECT_AP = 22;
	/**
	 * 接收串口线程标志
	 */
	public static boolean R_THREAD_FLAG = false;
	/**
	 * 原始卡号
	 */
	public static String FRIST_CARD_ID = null;
	/**
	 * 寻卡验证原始卡号
	 */
	public static String FRIST_CARD_ID_S = null;
	/**
	 * 寻卡线程标志
	 */
	public static boolean SEARCHER_THREAD_FLAG = false;
	/**
	 * 串口状态
	 */
	private static int UART_STATE = 0;
	/**
	 * 消费金额
	 */
	public static String CONSUME_MONEY = null;
	/**
	 * 充值金额
	 */
	public static String RECHARGE_MONEY = null;
	/**
	 * 出纳卡序号
	 */
	public static String  CASHIER_CARDSN=null;
	/**
	 * 物流到达城市的编号
	 */
	public static int LOGISTICS_CITY = -1;
	/**
	 * 卡信息
	 */
	public static String[] CardInfo=null;
	/**
	 * 寻卡权限
	 */
	public  static boolean SEARCHERFLAG=false;
	
	public synchronized static int getUART_STATE() {
		return UART_STATE;
	}
	public synchronized static void setUART_STATE(int uARTSTATE) {
		UART_STATE = uARTSTATE;
	}
	
}
