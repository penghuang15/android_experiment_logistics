package com.xunfang.experiment.logistics.so;

import android.util.Log;
/**
 * <p>
 * Title:物流管理系统
 * </p>
 * <p>
 * Description: JNI
 * </p>
 * <p>
 * Company：深圳市讯方通信技术有限公司
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @author 3G终端应用开发组
 * @version 1.0.0.0
 */
public class Linuxc {
	static {
		try {
			System.loadLibrary("uart");
			Log.i("JNI", "Trying to load libuart.so");
		} catch (UnsatisfiedLinkError ule) {
			Log.e("JNI", "WARNING:could not load libuart.so");
		}
	}

	/**
	 * 打开串口
	 * 
	 * @param i
	 *            串口号
	 * @return 成功：fd>0;失败：-1
	 */
	public static native int openUart(int i); // 打开串口

	/**
	 * 关闭串口
	 * 
	 * @param i
	 */
	public static native void closeUart(int i);// 关闭串口

	/**
	 * 设置串口波特率
	 * 
	 * @param i
	 *            波特率 例如：38400、115200
	 * @return 成功：0;失败：-1
	 */
	public static native int setUart(int i);// 设置串口波特率

	/**
	 * 发送数据
	 * 
	 * @param msg
	 *            数据
	 * @return
	 */
	public static native int sendMsgUart(String msg);// 发送信息

	/**
	 * 接收数据
	 * 
	 * @return
	 */
	public static native String receiveMsgUart();// 接收信息

	/**
	 * 
	 * @param databits
	 *            数据长度
	 * @param stopbits
	 *            停止位
	 * @param parity
	 *            校验类型（(int)n/N 无校验；(int)o/O 奇校验;(int)e/E 偶校验）
	 * @return
	 */
	public static native int setParity(int databits, int stopbits, int parity);// 设置奇偶校验

	/**
	 * 发送字节数组数据
	 * 
	 * @param msg
	 *            字节数组数据
	 * @return int
	 */
	public static native int sendByteUart(byte[] msg);

	/**
	 * 接收字节数组数据
	 * 
	 * @return
	 */
	public static native byte[] receiveByteUart();
}
