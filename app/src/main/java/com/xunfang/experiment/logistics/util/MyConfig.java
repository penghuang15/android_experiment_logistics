package com.xunfang.experiment.logistics.util;

import java.util.Arrays;
import java.util.List;

import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.bean.UserTypeBean;


public class MyConfig {
	private static String NOW_LOGINNAME = "";
	private static int NOW_USERTYPE = -1;
	private static int NOW_LOGINID = -1;
	private static int NOW_STATION_ID = -1;
	public static StationBean nextStation = null;
	private static List<UserTypeBean> weekctrList = Arrays.asList(
													new UserTypeBean(0, "周一到周日"),
													new UserTypeBean(1, "周一到周六"),
													new UserTypeBean(2, "周一到周五"));
	private static List<UserTypeBean> userTypeList = Arrays.asList(
													new UserTypeBean(0,"操作员"),
													new UserTypeBean(1,"派件员"));
	private static List<String> inoroutList = Arrays.asList("入站","出站","等待派送");
	public static byte CARDTYPE_LH = 0x01;//13.56M IC卡
	//串口状态
	private static int UART_STATE = -1;
	/**
	 * 入站扫描界面
	 */
	public static final int STATION_IN = 1;
	/**
	 * 出站扫描界面
	 */
	public static final int STATION_OUT = 2;
	/**
	 * 派件扫描界面
	 */
	public static final int DELIVERY = 3;
	/**
	 * 签收扫描界面
	 */
	public static final int SIGN = 4;
	/**
	 * 等待派送扫描界面
	 */
	public static final int FOR_DELIVERY = 5;
	/**
	 * 系统当前所处界面
	 */
	private static int current_interface = 0;
	/**
	 * 无界面
	 */
	public static int NONE_INTERFACE = 0;
	/**
	 * 门禁控制器所控制的门号数组
	 */
	//public static int[] DOOR_NO = {1,2};
	public static int[] DOOR_NO = {1};
	
	/**
	 * 门禁刷卡状态-读卡器刷卡开门
	 */
	public static int DOORACCESS_CARDSTATE_OPEN = 0;
	/**
	 * 门禁刷卡状态-读卡器刷卡禁止通过:原因不明
	 */
	public static int DOORACCESS_CARDSTATE_NO_REASON = 1;
	/**
	 * 门禁刷卡状态-读卡器刷卡禁止通过:卡无效或不在有效时段
	 */
	public static int DOORACCESS_CARDSTATE_CARDERROR = 2;
	/**
	 * 门禁刷卡状态-读卡器刷卡禁止通过:系统有故障
	 */
	public static int DOORACCESS_CARDSTATE_SYSTEMERROR = 3;
	/**
	 * 星期控制-无限制
	 */
	public static int[] WEEK_ALLDAYS = {0,1,2,3,4,5,6};
	/**
	 * 星期控制-工作日(包括周六)
	 */
	public static int[] WEEK_WORKDAY1 = {1,2,3,4,5,6};
	/**
	 * 星期控制-工作日
	 */
	public static int[] WEEK_WORKDAY2 = {1,2,3,4,5};
	/**
	 * 星期控制
	 */
	public static int[][] WEEK_CONTRO = {{0,1,2,3,4,5,6},{1,2,3,4,5,6},{1,2,3,4,5}};

	public synchronized static String getNOW_LOGINNAME() {
		return NOW_LOGINNAME;
	}

	public synchronized static void setNOW_LOGINNAME(String nOWLOGINNAME) {
		NOW_LOGINNAME = nOWLOGINNAME;
	}

	public synchronized static List<UserTypeBean> getWeekctrList() {
		return weekctrList;
	}

	public synchronized static void setWeekctrList(List<UserTypeBean> weekctrList) {
		MyConfig.weekctrList = weekctrList;
	}

	public synchronized static int getUART_STATE() {
		return UART_STATE;
	}

	public synchronized static void setUART_STATE(int uARTSTATE) {
		UART_STATE = uARTSTATE;
	}

	public synchronized static int getCurrent_interface() {
		return current_interface;
	}

	public synchronized static void setCurrent_interface(int currentInterface) {
		current_interface = currentInterface;
	}

	public synchronized static int getNOW_USERTYPE() {
		return NOW_USERTYPE;
	}

	public synchronized static void setNOW_USERTYPE(int nOWUSERTYPE) {
		NOW_USERTYPE = nOWUSERTYPE;
	}

	public synchronized static List<UserTypeBean> getUserTypeList() {
		return userTypeList;
	}

	public synchronized static void setUserTypeList(List<UserTypeBean> userTypeList) {
		MyConfig.userTypeList = userTypeList;
	}

	public synchronized static int getNOW_STATION_ID() {
		return NOW_STATION_ID;
	}

	public synchronized static void setNOW_STATION_ID(int nOWSTATIONID) {
		NOW_STATION_ID = nOWSTATIONID;
	}

	public synchronized static int getNOW_LOGINID() {
		return NOW_LOGINID;
	}

	public synchronized static void setNOW_LOGINID(int nOWLOGINID) {
		NOW_LOGINID = nOWLOGINID;
	}

	public synchronized static List<String> getInoroutList() {
		return inoroutList;
	}

	public synchronized static void setInoroutList(List<String> inoroutList) {
		MyConfig.inoroutList = inoroutList;
	}
	
}
