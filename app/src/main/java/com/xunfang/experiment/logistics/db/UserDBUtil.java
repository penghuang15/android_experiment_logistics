package com.xunfang.experiment.logistics.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xunfang.experiment.logistics.bean.UserBean;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：操作员(user)——数据库操作类
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
public class UserDBUtil {
	private LogisticsSqliteHelper dbHelper;// 定义数据库操作帮助类

	public UserDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = LogisticsSqliteHelper.getInstance(context);
	}
	//id integer primary key autoincrement,loginname varchar(20) not null," +
	//password varchar(20) not null,username varchar(20) not null," +
	//unique(loginname)
	/**
	 * 添加操作员
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addUserInfo(ContentValues values) {
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
			System.out.println("loginname-->"+values.getAsString("loginname"));
			System.out.println("password-->"+values.getAsString("password"));
			System.out.println("username-->"+values.getAsString("username"));
			System.out.println("type-->"+values.getAsString("type"));
			System.out.println("stationid-->"+values.getAsInteger("stationid"));
			if(!isUserExist(values.getAsString("loginname"), 0)){
				System.out.println("不存在"+values.getAsString("loginname"));
				id = db.insert("user", null, values);
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
	 * 根据id删除操作员信息
	 * 
	 * @param id 操作员的编号
	 */
	public boolean deleteUserInfo(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除操作员---->"+id);
			db.execSQL("delete from user where id=?",
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
	 * 批量删除操作员信息
	 * 
	 * @param ids 操作员的编号列表
	 */
	public boolean deleteUserInfo(List<Integer> ids) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			for(int id:ids){
				System.out.println("删除操作员---->"+id);
				db.execSQL("delete from user where id=?",
					new Object[] { id });
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
	 * 修改操作员信息
	 * @param id 编号
	 * @param type 操作员类型
	 * @param loginname 登录名
	 * @param password 登录密码
	 * @param username 用户名称
	 * @param station 所属站点编号
	 * @return flag 修改成功返回true，否则返回false
	 */
	public boolean updateUserInfo(String loginname,String password,String username,int type,int stationid, int id) {
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
			if(!isUserExist(loginname,id))
				db.execSQL("update user set loginname = ?,password = ?,username = ?,type = ?,stationid = ? where id = ?", new Object[]{loginname,password,username,type,stationid,id});
			else return false;
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
	 * 判断是否存在同名的操作员
	 * @param loginname 操作员登录名
	 * @param id 操作员编号
	 * @return boolean
	 */
	public boolean isUserExist(String loginname,int id) {
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
			if(id>0){
				cursor = db.rawQuery(
					"select id from user where loginname=? and id!=?",
					new String[] { loginname,id+"" });
			}else{
				cursor = db.rawQuery(
						"select id from user where loginname=?",
						new String[] { loginname });
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
	 * 操作员登录验证
	 * @param loginname
	 * @param password
	 * @return
	 */
	public int login(String loginname,String password) {
		System.out.println("loginname---->"+loginname);
		System.out.println("password---->"+password);
		SQLiteDatabase db = null;
		// 获得SQLiteDatebase实例
		try {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			Cursor cursor = db.rawQuery(
					"select id,type,stationid from user where loginname=? and password=?",
					new String[] { loginname,password });
			if (cursor.getCount() == 1) {
				cursor.moveToFirst();
				int type = cursor.getInt(1);
				MyConfig.setNOW_LOGINID(cursor.getInt(0));
				MyConfig.setNOW_STATION_ID(cursor.getInt(2));
				if(cursor!=null) cursor.close();
				return type;
			} else {
				if(cursor!=null) cursor.close();
				return -1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return -2;
		} finally {
			db.close();
		}
	}
	/**
	 * 获得操作员列表
	 * @return list 返回操作员列表
	 */
	public List<UserBean> getUserList(){
		//实例化列表
		ArrayList<UserBean> list = new ArrayList<UserBean>();
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
				cursor = db.rawQuery("select a.id,a.loginname,a.password,a.username,a.type,a.stationid,b.name from user a,station b where loginname!=? and loginname!=? and a.stationid=b.id order by a.id asc", 
									  new String[]{"admin",MyConfig.getNOW_LOGINNAME()});
				while(cursor.moveToNext()){
					//提取数据
					UserBean userBean = new UserBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), 
								cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
					list.add(userBean);
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
}
