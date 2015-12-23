package com.xunfang.experiment.logistics.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xunfang.experiment.logistics.bean.GoodsTypeBean;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：货物类型信息(goodstype)——数据库操作类
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
public class GoodsTypeDBUtil {
	private LogisticsSqliteHelper dbHelper;// 定义数据库操作帮助类

	public GoodsTypeDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = LogisticsSqliteHelper.getInstance(context);
	}
	/*id	编号	integer		not	主键	自增
		code	类型编号	integer		not	U	
		name	类型名称	varchar	100	not	U	
*/

	/**
	 * 添加货物类型
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addGoodsType(ContentValues values) {
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
			if(!isGoodsTypeExist(values.getAsInteger("code"), values.getAsString("name"), 0)){
				System.out.println("不存在"+values.getAsString("name"));
				id = db.insert("goodstype", null, values);
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
	 * 根据id删货物类型信息
	 * 
	 * @param id 站点的编号
	 */
	public boolean deleteGoodsType(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除货物类型---->"+id);
			if(!isGoodsTypeUsing(id)){
				db.execSQL("delete from goodstype where id=?",
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
	 * 批量删除货物类型信息
	 * 
	 * @param ids 货物类型的编号列表
	 */
	public int deleteGoodstype(List<Integer> ids) {
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
				System.out.println("删除货物类型---->"+id);
				if(!isGoodsTypeUsing(id)){
					db.execSQL("delete from goodstype where id=?",
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
	 * 修改货物类型信息
	 * @param id 编号
	 * @param code 类型编号
	 * @param name 名称
	 * @return flag 修改成功返回true，否则返回false
	 */
	public boolean updateGoodsType(int code ,String name, int id) {
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
			if(!isGoodsTypeExist(code,name,id)){
				db.execSQL("update goodstype set code = ?,name = ? where id = ?", 
						new Object[]{code,name,id});
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
	 * 判断是否存在同名的货物类型
	 * @param code 类型编号
	 * @param name 类型
	 * @param id 编号
	 * @return boolean
	 */
	public boolean isGoodsTypeExist(int code,String name,int id) {
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
					"select id from goodstype where (code = ? or name = ?) and id!=?",
					new String[] {code+"", name,id+"" });
			}else{
				cursor = db.rawQuery(
						"select id from goodstype where (code = ? or name=?)",
						new String[] {code+"", name });
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
	 * 判断该货物类型是否正在使用
	 * @param typeid 类型编号
	 * @return boolean
	 */
	public boolean isGoodsTypeUsing(int typeid) {
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
				"select id from goodsinfo where typeid = ?",
				new String[] { typeid+"" });
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
	 * 获得货物类型列表
	 * @return list 返回货物类型信息列表
	 */
	public List<GoodsTypeBean> getGoodsTypeList(){
		//实例化列表
		ArrayList<GoodsTypeBean> list = new ArrayList<GoodsTypeBean>();
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
				cursor = db.rawQuery("select id,code,name from goodstype " +
						" order by id asc",null);
				while(cursor.moveToNext()){
					//提取数据
					GoodsTypeBean goodsTypeBean = new GoodsTypeBean(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
					list.add(goodsTypeBean);
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
}
