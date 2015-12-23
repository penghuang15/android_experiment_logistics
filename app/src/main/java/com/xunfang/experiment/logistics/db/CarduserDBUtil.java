package com.xunfang.experiment.logistics.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xunfang.experiment.logistics.bean.CardinfoBean;
import com.xunfang.experiment.logistics.bean.CarduserBean;
import com.xunfang.experiment.logistics.bean.GoodsInfoBean;
import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.util.Tools;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：卡用户(cardinfo、customer)——数据库操作类
 * </p>
 * <p>

 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author sas
 */
public class CarduserDBUtil {
	private LogisticsSqliteHelper dbHelper;// 定义数据库操作帮助类

	public CarduserDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = LogisticsSqliteHelper.getInstance(context);
	}
	/**
	 * 物品打包
	 * @param cardsn 卡序号
	 * @param cardid8bit 原始卡号
	 * @param cardtype 卡类型
	 * @param ordernumber 订单号
	 * @param receiveaddr 收件地址编号
	 * @param goodsInfo 物品信息
	 * @return
	 */
	public synchronized int goodsPackage(String cardsn,String cardid8bit,byte cardtype,String ordernumber,int receiveaddr,Map<Integer, GoodsInfoBean> goodsInfo) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			System.out.println("开卡-即打包");
			//if(!isCustidExist(userid, 0)){
				int flag = isCardExist(cardid8bit, null);
				if(flag == -1){
					//String maxCardsn = getMaxCardsn();
					//if(maxCardsn!=null){
						Object[] values = goodsInfo.values().toArray();
						StringBuffer goodsStr = new StringBuffer(); 
						for(int i=0;i<values.length;i++){
							GoodsInfoBean goods = (GoodsInfoBean)values[i];
							goodsStr.append(goods.getType_id()).append(",").append(goods.getNum()).append("|");
						}
						goodsStr.deleteCharAt(goodsStr.length()-1);
						//int max_cardsn = Integer.parseInt(maxCardsn)+1;
						//String cardsn = Tools.addLeft0Str(max_cardsn+"", 8);
						db.execSQL("insert into cardinfo(cardsn,cardid8bit,cardtype,state) values(?,?,?,?)", 
								new Object[]{cardsn,cardid8bit,MyConfig.CARDTYPE_LH,1});
						db.execSQL("insert into orderform(cardid,ordernumber,goodsinfo,sendaddr,receiveaddr,state) values(?,?,?,?,?,?)", 
								new Object[]{cardsn,ordernumber,goodsStr.toString(),MyConfig.getNOW_STATION_ID(),receiveaddr,1});
						db.execSQL("insert into goodsfollow(ordernumber,currentstation,information,handletime,operater,state) values(?,?,?,?,?,?)", 
								new Object[]{ordernumber,MyConfig.getNOW_STATION_ID(),"打包完毕",Tools.formatDateString2(new Date()),MyConfig.getNOW_LOGINID(),1});
						db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
						return 0;
					//}else{
					//	return -3;//数据库操作失败LoginActivity.javaLoginActivity.javaordernum.getText().toString()ordernum.getText().toString()
					//}
					//开卡
				}else if(flag == 1){
					return -1;//此卡正在正常使用
				}else{
					return -3;
				}
			//}else{
				//return -4;//工号已存在
			//}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return -3;//数据库操作失败
		} finally {
			db.endTransaction();//处理完成
			db.close();
		}
	}
	/**
	 * 销户
	 * @param values
	 * @return
	 */
	public synchronized int closeAccount(String cardsn) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			System.out.println("销户");
			int state = getCardState(db, cardsn);
			if(state == 2){
				db.execSQL("update cardinfo set state = ? where cardsn = ?", new Object[]{3,cardsn});
				db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
				return 0;
			}else{
				return state;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return -1;//数据库操作失败
		} finally {
			db.endTransaction();//处理完成
			db.close();
		}
	}
	/**
	 * 获取卡信息表中最大的卡序号
	 * @return
	 */
	public synchronized String getMaxCardsn(){
		SQLiteDatabase db = null;
		// 获得SQLiteDatebase实例
		try {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			Cursor cursor = null;
			cursor = db.rawQuery(
					"select max(cardsn) from cardinfo",null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				String cardsn = cursor.getString(0);
				if(cursor!=null) cursor.close();
				if(cardsn==null){
					return "00000000";
				}else{
					return cardsn;
				}
			} else {
				if(cursor!=null) cursor.close();
				return "00000000";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			//db.close();
		}
	}
	/**
	 * 根据cardsn删除卡信息
	 * 
	 * @param cardsn 卡序号
	 */
	public boolean deleteCardInfo(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除卡信息---->"+id);
			db.execSQL("delete from cardinfo where cardsn=?",
					new Object[] { id });
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	/**
	 * 批量删除卡信息
	 * 
	 * @param cardsns 卡序号列表
	 */
	public boolean deleteCardinfo(List<String> cardsns) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			for(String cardsn:cardsns){
				System.out.println("删除卡信息---->"+cardsn);
				db.execSQL("delete from cardinfo where cardsn=?",
					new Object[] { cardsn });
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	/**
	 * 修改卡状态
	 * @param cardsn 编号
	 * @param state 卡状态：1 正常、2 挂失、3 销户
	 * @return flag 修改成功返回true，否则返回false
	 */
	public boolean updateCardState(String cardsn,int state) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			db.execSQL("update cardinfo set state = ? where cardsn = ?", new Object[]{state,cardsn});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.endTransaction();//事务结束
			if(db != null) db.close();
		}
		return true;
	}
	/**
	 * 挂失
	 * @param cardsn 编号
	 * @return 
	 */
	public int cardTimeout(String cardsn) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			int state = getCardState(db, cardsn);
			if(state==1){
				db.execSQL("delete from dooraccess where cardcode = ?", new Object[]{cardsn});//删除门禁权限
				db.execSQL("update cardinfo set state = ? where cardsn = ?", new Object[]{2,cardsn});
				db.setTransactionSuccessful();
				return 0;
			}else{
				return state;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return -1;
		} finally {
			db.endTransaction();//事务结束
			if(db != null) db.close();
		}
	}
	/**
	 * 解挂
	 * @param cardsn 编号
	 * @return 
	 */
	public int cardRestart(String cardsn) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			int state = getCardState(db, cardsn);
			if(state==2){
				db.execSQL("update cardinfo set state = ? where cardsn = ?", new Object[]{1,cardsn});
				db.setTransactionSuccessful();
				return 0;
			}else{
				return state;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		} finally {
			db.endTransaction();//事务结束
			if(db != null) db.close();
		}
	}
	/**
	 * 获得卡状态
	 * @param db
	 * @param cardsn 卡序号
	 * @return boolean
	 */
	public int getCardState(SQLiteDatabase db,String cardsn) {
		try {
			Cursor cursor = null;
			cursor = db.rawQuery(
						"select state from cardinfo where cardsn=?",
						new String[] { cardsn });
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				int state = cursor.getInt(0);
				if(cursor!=null) cursor.close();
				return state;
			} else {
				if(cursor!=null) cursor.close();
				return -1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		} finally {
			//db.close();
		}
	}
	/**
	 * 判断是否存在8位原始卡号相同的卡
	 * @param cardid8bit 8位原始卡号
	 * @param cardsn 卡序号
	 * @return boolean
	 */
	public int isCardExist(String cardid8bit,String cardsn) {
		SQLiteDatabase db = null;
		// 获得SQLiteDatebase实例
		try {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			Cursor cursor = null;
			if(cardsn!=null){
				cursor = db.rawQuery(
					"select state from cardinfo where cardid8bit=? and cardsn!=? and state = ?",
					new String[] { cardid8bit,cardsn,1+""});
			}else{
				cursor = db.rawQuery(
						"select state from cardinfo where cardid8bit=? and state = ?",
						new String[] { cardid8bit,1+""});
			} 
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				int state = cursor.getInt(0);
				if(cursor!=null) cursor.close();
				return state;
			} else {
				if(cursor!=null) cursor.close();
				return -1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		} finally {
			//db.close();
		}
	}
	/**
	 * 获得卡信息列表
	 * @param state 卡状态
	 * @return list 返回卡信息列表
	 */
	public List<CardinfoBean> getCardinfoList(int state){
		//实例化列表
		ArrayList<CardinfoBean> list = new ArrayList<CardinfoBean>();
		//定义SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//获得SQLiteDatabase实例
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//获得SQLiteDatabase实例
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//获得游标
				cursor = db.rawQuery("select cardsn,cardid8bit,cardtype,state from cardinfo where state=? order by id asc", 
									  new String[]{state+""});
				while(cursor.moveToNext()){
					//提取数据
					CardinfoBean cardinfoBean = new CardinfoBean(cursor.getString(0), cursor.getString(1), cursor.getInt(2),cursor.getInt(3));
					list.add(cardinfoBean);
				}
				System.out.println("list.size()--->"+list.size());
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else {
			return null;
		}
		return list;
	}
	/**
	 * 添加客户信息
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addCustomer(ContentValues values) {
		if (values == null){
			return false;
		}
		SQLiteDatabase db = null;
		long id = -1;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			if(!isCustidExist(values.getAsString("userid"), 0)){
				System.out.println("不存在"+values.getAsString("userid"));
				id = db.insert("customer", null, values);
				System.out.println("插入成功--->"+id);
			}
			else return false;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return false;
		} finally {
			db.close();
		}
		return id > 0;// id>0表示插入成功
	}

	/**
	 * 根据custid删除客户信息
	 * 
	 * @param custid 客户编号
	 */
	public boolean deleteCustomer(int custid) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除客户信息---->"+custid);
			db.execSQL("delete from customer where custid=?",
					new Object[] { custid });
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	/**
	 * 批量删除客户信息
	 * 
	 * @param custids 客户编号列表
	 */
	public boolean deleteCustomer(List<Integer> custids) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			for(int custid:custids){
				System.out.println("删除卡信息---->"+custid);
				db.execSQL("delete from customer where custid=?",
					new Object[] { custid });
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.close();
		}
		return true;
	}
	/**
	 * 修改客户信息
	 * @param name 客户姓名
	 * @param userid 工号
	 * @param postcode 职位编号
	 * @param deptcode 部门编号
	 * @param cardsn 卡序号
	 * @param custid 客户编号
	 * @return flag 修改成功返回true，否则返回false
	 */
	public boolean updateCardState(String name,String userid,int postcode,int deptcode,String cardsn,int custid) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			db.execSQL("update customer set name = ?,userid = ?,postcode = ?,deptcode = ?,cardsn = ? where custid = ?", 
					new Object[]{name,userid,postcode,deptcode,cardsn,custid});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			db.endTransaction();//事务结束
			if(db != null) db.close();
		}
		return true;
	}

	/**
	 * 判断工号是否已经存在
	 * @param userid 工号
	 * @param custid 客户编号
	 * @return boolean
	 */
	public boolean isCustidExist(String userid,int custid) {
		SQLiteDatabase db = null;
		// 获得SQLiteDatebase实例
		try {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			Cursor cursor = null;
			if(custid>0){
				cursor = db.rawQuery(
					"select cardsn from customer where userid=? and custid!=?",
					new String[] { userid,custid+"" });
			}else{
				cursor = db.rawQuery(
						"select cardsn from customer where userid=?",
						new String[] { userid });
			} 
			if (cursor.getCount() > 0) {
				if(cursor!=null) cursor.close();
				return true;
			} else {
				if(cursor!=null) cursor.close();
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		} finally {
			//db.close();
		}
	}
	
	/**
	 * 根据卡序号获得客户信息
	 * @param cardsn
	 * @param cardid8bit
	 * @return CustomerBean 返回客户信息
	 */
	public CarduserBean getCarduserBean(String cardsn,String cardid8bit){
		//定义SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//获得SQLiteDatabase实例
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//获得SQLiteDatabase实例
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//获得游标
				cursor = db.rawQuery("select a.custid,a.name,a.userid,a.postcode,a.deptcode,a.cardsn,b.postname,c.deptname," +
							"d.cardid8bit,d.state from customer a,position b,department c,cardinfo d where a.postcode=b.id " +
							"and a.deptcode=c.id and a.cardsn=d.cardsn and a.cardsn=? and a.d.cardid8bit=?",
							new String[]{cardsn,cardid8bit});
				while(cursor.moveToNext()){
					cursor.moveToFirst();
					//提取数据
					CarduserBean carduserBean = new CarduserBean(cursor.getInt(0),cursor.getString(1), cursor.getString(2), 
							cursor.getInt(3), cursor.getInt(4), cursor.getString(5),cursor.getString(6),cursor.getString(7),
							cursor.getString(8),cursor.getInt(9));
					return carduserBean;
				}
				return null;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else {
			return null;
		}
	}
	/**
	 * 销户
	 * @param values
	 * @return
	 */
	public synchronized int sign(String cardsn) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			db.beginTransaction();//事务开始
			System.out.println("签收");
			int state = getCardState(db, cardsn);
			if(state == 1){
				db.execSQL("update cardinfo set state = ? where cardsn = ?", new Object[]{2,cardsn});
				db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
				return 0;
			}else{
				return state;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return -1;//数据库操作失败
		} finally {
			db.endTransaction();//处理完成
			db.close();
		}
	}
	/**
	 * 获得卡用户信息列表
	 * @return list 返回卡用户信息列表
	 */
	public List<CarduserBean> getCarduserList(){
		//实例化列表
		ArrayList<CarduserBean> list = new ArrayList<CarduserBean>();
		//定义SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//获得SQLiteDatabase实例
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//获得SQLiteDatabase实例
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//获得游标
				cursor = db.rawQuery("select a.custid,a.name,a.userid,a.postcode,a.deptcode,a.cardsn,b.postname,c.deptname,d.cardid8bit,d.state from customer a,position b,department c," +
						"cardinfo d where a.postcode=b.id and a.deptcode=c.id and a.cardsn=d.cardsn order by a.custid asc",null);
				//select a.*,b.postname,c.deptname,d.cardid8bit,d.state from customer a,positon b,department c,cardinfo d where a.postcode=b.id and a.deptcode=c.id and a.cardsn=d.cardsn;
				while(cursor.moveToNext()){
					//提取数据
					CarduserBean carduserBean = new CarduserBean(cursor.getInt(0),cursor.getString(1), cursor.getString(2), 
							cursor.getInt(3), cursor.getInt(4), cursor.getString(5),cursor.getString(6),cursor.getString(7),
							cursor.getString(8),cursor.getInt(9));
					list.add(carduserBean);
				}
				System.out.println("list.size()--->"+list.size());
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else {
			return null;
		}
		return list;
	}
	/**
	 * 获得卡用户信息列表(正常使用)
	 * @return list 返回卡用户信息列表
	 */
	public List<CarduserBean> getCarduserUsingList(){
		//实例化列表
		ArrayList<CarduserBean> list = new ArrayList<CarduserBean>();
		//定义SQLiteDatabase
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//获得SQLiteDatabase实例
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			//获得SQLiteDatabase实例
			db = dbHelper.getReadableDatabase();
		}	
		if(db != null){
			try {
				//获得游标
				cursor = db.rawQuery("select a.custid,a.name,a.userid,a.postcode,a.deptcode,a.cardsn,b.postname,c.deptname,d.cardid8bit,d.state from customer a,position b,department c," +
						"cardinfo d where a.postcode=b.id and a.deptcode=c.id and a.cardsn=d.cardsn and d.state=? order by a.custid asc",new String[]{1+""});
				//select a.*,b.postname,c.deptname,d.cardid8bit,d.state from customer a,positon b,department c,cardinfo d where a.postcode=b.id and a.deptcode=c.id and a.cardsn=d.cardsn;
				while(cursor.moveToNext()){
					//提取数据
					CarduserBean carduserBean = new CarduserBean(cursor.getInt(0),cursor.getString(1), cursor.getString(2), 
							cursor.getInt(3), cursor.getInt(4), cursor.getString(5),cursor.getString(6),cursor.getString(7),
							cursor.getString(8),cursor.getInt(9));
					list.add(carduserBean);
				}
				System.out.println("list.size()--->"+list.size());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else {
			return null;
		}
		return list;
	}
	/**
	 * 验证是否为合法卡
	 * 
	 * @return boolean
	 */
	public String checkCardsn(String cardsn) {
		SQLiteDatabase db = null;
		// 获得SQLiteDatebase实例
		try {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			Cursor cursor = db.rawQuery("select b.ordernumber from cardinfo a,orderform b where a.cardsn = ? and a.state = ? and a.cardsn = b.cardid",new String[]{cardsn,1+""});
			if (cursor.getCount() == 1) {
				cursor.moveToFirst();
				String ordernumber = cursor.getString(0);
				if(cursor!=null) cursor.close();
				return ordernumber;
			}else {
				if(cursor!=null) cursor.close();
				return "-1";
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return "-2";
		} finally {
			db.close();
		}
	}
}
