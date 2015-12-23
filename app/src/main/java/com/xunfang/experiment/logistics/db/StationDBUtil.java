package com.xunfang.experiment.logistics.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xunfang.experiment.logistics.bean.CityBean;
import com.xunfang.experiment.logistics.bean.ProvinceBean;
import com.xunfang.experiment.logistics.bean.StationBean;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：站点信息(station)——数据库操作类
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
public class StationDBUtil {
	private LogisticsSqliteHelper dbHelper;// 定义数据库操作帮助类

	public StationDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = LogisticsSqliteHelper.getInstance(context);
	}
	/*id	编号	integer		not	主键	自增
	name	站点名称	varchar	50	not	U	
	addr	所属城市	integer		not	外键，城市信息表中的code	*/

	/**
	 * 添加站点
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addStation(ContentValues values) {
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
			if(!isStationExist(values.getAsString("name"), 0)){
				System.out.println("不存在"+values.getAsString("name"));
				id = db.insert("station", null, values);
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
	 * 根据id删站点信息
	 * 
	 * @param id 站点的编号
	 */
	public boolean deleteStation(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除站点---->"+id);
			if(!isStationUsing(id)){
				db.execSQL("delete from station where id=?",
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
	 * 批量删除站点信息
	 * 
	 * @param ids 站点的编号列表
	 */
	public int deleteStation(List<Integer> ids) {
		SQLiteDatabase db = null;
		int i = 0;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			for(int id:ids){
				System.out.println("删除站点---->"+id);
				if(!isStationUsing(id)){
					db.execSQL("delete from station where id=?",
							new Object[] { id });
					i++;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		} finally {
			db.close();
		}
		return i;
	}
	/**
	 * 修改站点信息
	 * @param id 编号
	 * @param name 站点名称
	 * @param addr 所属城市编号
	 * @return flag 修改成功返回true，否则返回false
	 */
	public boolean updateStation(String name,int addr,int id) {
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
			if(!isStationExist(name,id)){
				db.execSQL("update station set name = ?,addr = ? where id = ?", 
						new Object[]{name,addr,id});
			}else return false;
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			return false;
		} finally {
			db.endTransaction();//事务结束
			if(db != null) db.close();
		}
		return true;
	}

	/**
	 * 判断是否存在同名的站点
	 * @param name 站点名称
	 * @param id 站点编号
	 * @return boolean
	 */
	public boolean isStationExist(String name,int id) {
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
					"select id from station where name=? and id!=?",
					new String[] { name,id+"" });
			}else{
				cursor = db.rawQuery(
						"select id from station where name=?",
						new String[] { name });
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
	 * 判断该站点是否正在使用
	 * @param stationid 站点编号
	 * @return boolean
	 */
	public boolean isStationUsing(int stationid) {
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
				"select id from goodsfollow where currentstation = ?",
				new String[] { stationid+"" });
			if (cursor.getCount() > 0) {
				if(cursor != null) cursor.close();
				return true;
			} else {
				if(cursor != null) cursor.close();
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
	 * 获得站点列表
	 * @return list 返回站点信息列表
	 */
	public List<StationBean> getStationList(){
		//实例化列表
		ArrayList<StationBean> list = new ArrayList<StationBean>();
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
				cursor = db.rawQuery("select a.id,a.name,b.code,b.name,c.code,c.name from station a,province b,city c where a.addr = c.code and c.p_code = b.code " +
						" order by a.id asc",null);
				while(cursor.moveToNext()){
					//提取数据
					StationBean stationBean = new StationBean(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
							cursor.getInt(4),cursor.getString(5));
					list.add(stationBean);
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
	 * 获得省份/直辖市列表
	 * @return list 返回省份/直辖市列表
	 */
	public List<ProvinceBean> getProvinceList(){
		//实例化列表
		ArrayList<ProvinceBean> list = new ArrayList<ProvinceBean>();
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
				cursor = db.rawQuery("select id,code,name from province order by id asc", null);
				while(cursor.moveToNext()){
					//提取数据
					ProvinceBean province = new ProvinceBean(cursor.getInt(0),cursor.getInt(1), cursor.getString(2));
					list.add(province);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				e.printStackTrace();
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
	 * 根据省份/直辖市编号获得城市列表
	 * @return list 返回城市列表
	 */
	public List<CityBean> getCityList(int provinceCode){
		//实例化列表
		ArrayList<CityBean> list = new ArrayList<CityBean>();
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
				cursor = db.rawQuery("select id,code,name from city where p_code=? order by id asc", new String[]{provinceCode+""});
				while(cursor.moveToNext()){
					//提取数据
					CityBean city = new CityBean(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
					list.add(city);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
				e.printStackTrace();
				return null;
			}finally{
				if(cursor != null) cursor.close();
				db.close();
			}
		}else{
			return null;
		}
		return list;
	}
	/**
	 * 根据编号获得站点信息
	 * @return StationBean 返回站点信息
	 */
	public StationBean getStationById(int id){
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
				cursor = db.rawQuery("select a.id,a.name,b.code,b.name,c.code,c.name from station a,province b,city c where a.addr = c.code and c.p_code = b.code " +
						" and a.id=?",new String[]{id+""});
				if(cursor.getCount()==1){
					cursor.moveToFirst();
					//提取数据
					StationBean stationBean = new StationBean(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
							cursor.getInt(4),cursor.getString(5));
					return stationBean;
				}else{
					return null;
				}
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
	}
}
