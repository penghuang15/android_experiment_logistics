package com.xunfang.experiment.logistics.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xunfang.experiment.logistics.bean.GoodsFollowBean;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：货物跟踪(goodsfollow)——数据库操作类
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
public class GoodsFollowDBUtil {
	private LogisticsSqliteHelper dbHelper;// 定义数据库操作帮助类

	public GoodsFollowDBUtil(Context context) {
		// 实例化数据库操作帮助类
		this.dbHelper = LogisticsSqliteHelper.getInstance(context);
	}
	/*id	编号	integer		not	主键	自增
	name	站点名称	varchar	50	not	U	
	addr	所属城市	integer		not	外键，城市信息表中的code	*/

	/**
	 * 添加跟踪记录
	 * 
	 * @param values
	 * @return 添加成功返回true,否则返回false
	 */
	public boolean addGoodsFollow(ContentValues values) {
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
			id = db.insert("goodsfollow", null, values);
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
	 * 根据id删除跟踪记录
	 * 
	 * @param id 跟踪记录的编号
	 */
	public boolean deleteGoodsFollow(int id) {
		SQLiteDatabase db = null;
		try {
			// 获得SQLiteDatebase实例
			try {
				db = dbHelper.getWritableDatabase();
			} catch (Exception e) {
				// TODO: handle exception
				db = dbHelper.getReadableDatabase();
			}
			System.out.println("删除跟踪--->"+id);
			db.execSQL("delete from goodsfollow where id=?",
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
	 * 批量删除跟踪记录
	 * 
	 * @param ids 跟踪记录的编号列表
	 */
	public int deleteGoodsFollow(List<Integer> ids) {
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
				System.out.println("删除跟踪记录---->"+id);
				db.execSQL("delete from goodsfollow where id=?",
							new Object[] { id });
				i++;
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
	 * 获得跟踪记录列表
	 * @param ordernumber
	 * @param cardsn
	 * @return list 返回跟踪记录列表
	 */
	public List<GoodsFollowBean> getFollowList(String ordernumber, String cardsn){
		//实例化列表
		ArrayList<GoodsFollowBean> list = new ArrayList<GoodsFollowBean>();
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
				if((ordernumber != null && ordernumber.length() == 20) && (cardsn != null && cardsn.length() == 8)){
					String queryStr = "select a.id,a.ordernumber,a.currentstation,b.name,a.information,a.handletime," +
							"a.operater,c.username,a.state from goodsfollow a,station b,user c,orderform d " +
							" where a.currentstation = b.id and a.operater = c.id " +
							" and a.ordernumber=d.ordernumber and a.ordernumber = ? and d.cardid=? order by a.id asc";
					//获得游标
					cursor = db.rawQuery(queryStr, new String[]{ordernumber,cardsn});
					
				}else{
					if(ordernumber != null && ordernumber.length() == 20){
						String queryStr = "select a.id,a.ordernumber,a.currentstation,b.name,a.information,a.handletime," +
								"a.operater,c.username,a.state from goodsfollow a,station b,user c " +
								" where a.currentstation = b.id and a.operater = c.id " +
								" and a.ordernumber = ? order by a.id asc";
						//获得游标
						cursor = db.rawQuery(queryStr, new String[]{ordernumber});
					}else if(cardsn != null && cardsn.length() == 8){
						String queryStr = "select a.id,a.ordernumber,a.currentstation,b.name,a.information,a.handletime," +
								"a.operater,c.username,a.state from goodsfollow a,station b,user c,orderform d " +
								" where a.currentstation = b.id and a.operater = c.id " +
								" and a.ordernumber=d.ordernumber and d.cardid=? order by a.id asc";
						//获得游标
						cursor = db.rawQuery(queryStr, new String[]{cardsn});
					}
				}
				while(cursor.moveToNext()){
					//提取数据
					GoodsFollowBean goodsFollowBean = new GoodsFollowBean(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
							cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7),cursor.getInt(8));
					list.add(goodsFollowBean);
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
