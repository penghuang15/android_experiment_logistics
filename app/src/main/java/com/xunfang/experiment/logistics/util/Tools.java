package com.xunfang.experiment.logistics.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

/**
 * <p>Title:3G物联网业务开发平台</p>
 * <p>Description: 工具类</p>
 * <p>Copyright: Copyright (c) 2011</p>

 * @author 鲁迎龙
 * @version 2.0.0.0
 */
public class Tools {
	/**
	 * int转化为byte数组
	 * @param n 整数
	 * @return 字节数组
	 */
	public static byte[] byteToByteArray(int n){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(out);
		try {
			dos.writeByte(n);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * int转化为byte数组
	 * @param n 整数
	 * @return 字节数组
	 */
	public static byte[] intToByteArray(int n){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(out);
		try {
			dos.writeInt(n);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * short转化为byte数组
	 * @param n 整数
	 * @return 字节数组
	 */
	public static byte[] shortToByteArray(int n){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(out);
		try {
			dos.writeShort(n);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * byte数组转化为byte
	 * @param byteArray 字节数组
	 * @return 整数
	 */
	public static int byteArrayToByte(byte[] byteArray){
		ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
		DataInputStream dis=new DataInputStream(input);
		try {
			return dis.readByte()&0xFF;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * byte数组转化为Short
	 * @param byteArray 字节数组
	 * @return 整数
	 */
	public static int byteArrayToShort(byte[] byteArray){
		ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
		DataInputStream dis=new DataInputStream(input);
		try {
			return dis.readShort()&0xFFFF;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * byte数组转化为int
	 * @param byteArray 字节数组
	 * @return 整数
	 */
	public static int byteArrayToInt(byte[] byteArray){
		ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
		DataInputStream dis=new DataInputStream(input);
		try {
			return dis.readInt()&0xFFFFFFFF;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 将指定日期转换为"yyyy-MM-dd HH:mm:ss"格式
	 * @param date Date 
	 * @return String 
	 */
	public static String formatDateString1(Date date){
		if (date == null) {
			return "";
		}else {
			String disp = "";
		    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    disp = bartDateFormat.format(date);
		    return disp;
		}
	}
	/**
	 * 将yyyyMMddHHmmss格式的日期字符串转换为yyyy-MM-dd HH:mm:ss格式
	 * @param dateStr
	 * @return
	 */
	public static String getDateFromString1(String dateStr){
    	Date datetime = null;
    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
         SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
         datetime = df.parse(dateStr);
      }
      catch (Exception ex) {
        datetime = null;
      }
    	return df1.format(datetime);
    }
	/**
	 * 将指定日期转换为"yyyyMMddHHmmss"格式
	 * @param date Date 
	 * @return String 
	 */
	public static String formatDateString2(Date date){
		if (date == null) {
			return "";
		}else {
			String disp = "";
		    SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    disp = bartDateFormat.format(date);
		    return disp;
		}
	}
	/**
	 * 将日期字符串转换为Date
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFromString(String dateStr){
    	Date datetime = null;
    	try {
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         datetime = df.parse(dateStr);
      }
      catch (Exception ex) {
        datetime = null;
      }
    	return datetime;
    }
	/**
	 * 判断数组array里是否包含原始element
	 * @param array
	 * @param element
	 * @return
	 */
	public static boolean isArrayContainsItem(int[] array,int element){
		boolean isContain = false;
		for(int item:array){
			if(item == element){
				isContain = true;
				break;
			}
		}
		return isContain;
	}
	/**
	 * 判断数组array里是否包含原始element
	 * @param array
	 * @param element
	 * @return
	 */
	public static boolean isArrayContainsByteItem(byte[] array,byte element){
		boolean isContain = false;
		for(byte item:array){
			if(item == element){
				isContain = true;
				break;
			}
		}
		return isContain;
	}
	public static void main(String[] args) {
		byte[] byteArray = {0,-29};
		System.out.println(byteArrayToShort(byteArray));
	}
	/**
	 * 返回为原字符串往左补满count位"0"后的字符串
	 * @param tmp  原字符串
	 * @param count 补满位数
	 * @return String 新字符串
	 */
	public static String addLeft0Str(String tmp,int count){
		String str = tmp;
		for (int i = 0; i < count - tmp.length(); i++) {
			str = "0" + str;
		}
		return str;
	}
	/**
	 * 生成订单号
	 * @return String 
	 */
	public static String createOrderNum(){
		String disp = "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		disp = bartDateFormat.format(new Date());
		Random random = new Random();
		int random_int = random.nextInt(100)+100;
		return disp+random_int;
	}
}
