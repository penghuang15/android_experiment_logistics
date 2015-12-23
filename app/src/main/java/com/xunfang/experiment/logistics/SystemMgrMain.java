package com.xunfang.experiment.logistics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.xunfang.experiment.logistics.bean.FunctionMenu;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：系统管理主界面
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
public class SystemMgrMain extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	// 定义主界面上的组件
	private ListView functionList;
	private MenuAdapter adapter;
	private List<FunctionMenu> data = new ArrayList<FunctionMenu>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemmgr_main);
		init();
	}
	/**
	 * 自定义初始化方法
	 */
	private void init() {
		functionList = (ListView) findViewById(R.id.roundList);
		data.add(new FunctionMenu(R.drawable.user, "操作员管理", new Intent(
				SystemMgrMain.this, UserActivity.class)));
		data.add(new FunctionMenu(R.drawable.storage, "站点管理", new Intent(
				SystemMgrMain.this, StationActivity.class)));
		data.add(new FunctionMenu(R.drawable.goodstype, "货物类型", new Intent(
				SystemMgrMain.this, GoodsTypeActivity.class)));
		data.add(new FunctionMenu(R.drawable.goodsinfo, "货物信息", new Intent(
				SystemMgrMain.this, GoodsInfoActivity.class)));
		adapter = new MenuAdapter(SystemMgrMain.this, data);
		functionList.setAdapter(adapter);
		functionList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FunctionMenu functionMenu = data.get(position);
				startActivity(functionMenu.getIntent());
			}
		});
	}

	/**
	 * 自定义适配器类
	 */
	static class MenuAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private List<FunctionMenu> list;

		public MenuAdapter(Context content, List<FunctionMenu> list) {
			this.mContext = content;
			this.mInflater = LayoutInflater.from(mContext);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public FunctionMenu getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.function_menu_item,
						null);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.menu_item_imageview);
				holder.func_name = (TextView) convertView
						.findViewById(R.id.menu_item_textview);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();
			FunctionMenu menu = list.get(position);
			holder.icon.setImageResource(menu.getIcon());
			holder.func_name.setText(menu.getFunc_name());
			return convertView;
		}
	}

	static final class ViewHolder {
		ImageView icon;// 图标
		TextView func_name;// 功能名称
		ImageView go_icon;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_revert:// 返回按钮
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_OK implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			MyConfig.setNOW_LOGINNAME("");
			finish();
			System.exit(0);
		}

	}

	/**
	 * alertDialog监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class OnClickLiner_Cancel implements
			android.content.DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}

	}
}