package com.xunfang.experiment.logistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.xunfang.experiment.logistics.bean.UserBean;
import com.xunfang.experiment.logistics.db.UserDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：操作员管理界面
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
public class UserActivity extends Activity implements OnClickListener,
		OnItemLongClickListener, OnItemClickListener {

	private final int DIALOG_ID_QUERY = 16;
	private final int DIALOG_ID_DELETE = 17;
	// 定义控件
	private ImageView revert;// "返回"按钮
	private TextView title;// 标题
	private ListView userListView;
	private Button selectall_btn;// 全选按钮
	private Button add_btn;// 添加按钮
	private Button delete_btn;// 删除按钮

	private List<UserBean> list = null;
	private UserAdapter adapter = null;
	private UserBean userBean;
	private int location;
	String operateStr = null;
	public static boolean isSelectedAll = false;
	private List<Integer> deleteList = new ArrayList<Integer>();// 删除列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.user);
		// 自定义窗口样式
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView) findViewById(R.id.common_title_revert);
		title = (TextView) findViewById(R.id.common_title_title);
		title.setText(R.string.userMgr);
		selectall_btn = (Button) findViewById(R.id.user_select_all);
		add_btn = (Button) findViewById(R.id.user_add);
		delete_btn = (Button) findViewById(R.id.user_delete);
		revert.setOnClickListener(this);
		selectall_btn.setOnClickListener(this);
		add_btn.setOnClickListener(this);
		delete_btn.setOnClickListener(this);
		userListView = (ListView) findViewById(R.id.user_list_content);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ("".equals(MyConfig.getNOW_LOGINNAME())) {
			Intent intent = new Intent(UserActivity.this, Main.class);
			Main.setChecked(R.id.main_tab_login);
			startActivity(intent);
			finish();
		} else {
			isSelectedAll = false;
			new GetUserListTask().execute("");
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ImageView || view instanceof Button) {
			switch (view.getId()) {
			case R.id.common_title_revert:
				finish();
				break;
			case R.id.user_add:
				Intent intent = new Intent(UserActivity.this,
						AddUserActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.user_select_all:
				isSelectedAll = !isSelectedAll;
				for (int i = 0; i < list.size(); i++)
					UserAdapter.isSelectedMap.put(i, isSelectedAll);
				if (adapter != null)
					adapter.notifyDataSetChanged();
				break;
			case R.id.user_delete:
				new DeleteUserTask().execute("");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 异步操作类——执行获取操作员列表的操作
	 * 
	 * @author sas
	 * 
	 */
	class GetUserListTask extends AsyncTask<String, Integer, String> {

		/**
		 * 后台执行获取操作员列表的操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserDBUtil userDBUtil = new UserDBUtil(UserActivity.this);
			list = userDBUtil.getUserList();
			if (list != null)
				return "true";
			else
				return "false";
		}

		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID_QUERY);
			if ("true".equals(result)) {
				if (list == null || list.size() == 0) {
					Toast.makeText(UserActivity.this,
							getText(R.string.userlist_empty),
							Toast.LENGTH_SHORT).show();
					// 实例化适配器
					adapter = new UserAdapter(UserActivity.this,
							new ArrayList<UserBean>());
					// 为ListView添加适配器
					userListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					// 实例化适配器
					adapter = new UserAdapter(UserActivity.this, list);
					// 为ListView添加适配器
					userListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					// 为ListView设置监听器
					userListView.setOnItemClickListener(UserActivity.this);
					userListView.setOnItemLongClickListener(UserActivity.this);
				}
			} else {
				Toast.makeText(UserActivity.this,
						getText(R.string.query_failure), Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID_QUERY);
		}
	}

	/**
	 * 自定义适配器类
	 */
	static class UserAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private List<UserBean> list;
		public static Map<Integer, Boolean> isSelectedMap;

		public UserAdapter(Context content, List<UserBean> list) {
			this.mContext = content;
			this.mInflater = LayoutInflater.from(mContext);
			this.list = list;
			init();
		}

		// 初始化 设置所有checkbox都为未选择
		public void init() {
			isSelectedMap = new HashMap<Integer, Boolean>();
			for (int i = 0; i < list.size(); i++) {
				isSelectedMap.put(i, isSelectedAll);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public UserBean getItem(int position) {
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
				convertView = mInflater.inflate(R.layout.user_item, null);
				holder.select = (CheckBox) convertView
						.findViewById(R.id.user_item_select);
				holder.select.setChecked(isSelectedAll);
				holder.loginname = (TextView) convertView
						.findViewById(R.id.user_item_loginname);
				holder.username = (TextView) convertView
						.findViewById(R.id.user_item_username);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();
			UserBean user = list.get(position);
			holder.select.setChecked(isSelectedMap.get(position));
			holder.loginname.setText(user.getLoginname());
			holder.username.setText(user.getUsername());
			return convertView;
		}
	}

	static final class ViewHolder {
		CheckBox select;
		TextView loginname;
		TextView username;
	}

	/**
	 * ListView中的选项被长按时的回调方法
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		AlertDialog.Builder alertdb = new AlertDialog.Builder(UserActivity.this);
		// 选中项的位置
		location = position;
		// 获得选中项上的User对象
		userBean = (UserBean) list.get(position);

		// 设置标题
		alertdb.setTitle(getText(R.string.operate_select));
		alertdb.setNegativeButton(getText(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		final CharSequence[] items = { getText(R.string.edit),
				getText(R.string.delete) };
		alertdb.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {// 选择编辑
					Intent intent = new Intent(UserActivity.this,
							AddUserActivity.class);// 编辑操作员
					intent.putExtra("userBean", userBean);
					startActivity(intent);
					finish();
				} else if (item == 1) {// 选择删除
					AlertDialog.Builder alertdb = new AlertDialog.Builder(
							UserActivity.this);
					alertdb.setTitle(getText(R.string.delete_confirm));
					alertdb.setMessage(getText(R.string.delete_ensure));
					alertdb.setPositiveButton(getText(R.string.delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deleteList.clear();
									deleteList.add(userBean.getId());
									new DeleteUserTask().execute("single");
								}
							});
					alertdb.setNegativeButton(getText(R.string.cancel), null);
					alertdb.show();
				} else {
				}
			}
		});
		alertdb.show();
		return false;
	}

	/**
	 * ListView中的选项被单击时的回调方法
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.select.toggle();
		if (viewHolder.select.isChecked() == false)
			isSelectedAll = false;
		UserAdapter.isSelectedMap.put(position, viewHolder.select.isChecked());
		for (int i = 0; i < list.size(); i++) {
			if (!UserAdapter.isSelectedMap.get(i)) {
				isSelectedAll = false;
				break;
			}
			isSelectedAll = true;
		}
		System.out.println("isSelectedAll---->" + isSelectedAll);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 异步操作类——执行删除操作员的操作
	 * 
	 * @author sas
	 * 
	 */
	class DeleteUserTask extends AsyncTask<String, Integer, String> {
		/**
		 * 后台执行删除操作员的操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (!"single".equals(params[0])) {
				deleteList.clear();
				for (int i = 0; i < list.size(); i++) {
					if (UserAdapter.isSelectedMap != null
							&& UserAdapter.isSelectedMap.get(i) == true) {
						deleteList.add(list.get(i).getId());
					}
				}
				if (deleteList.size() == 0) {
					return "empty";
				}
			}
			UserDBUtil userDBUtil = new UserDBUtil(UserActivity.this);
			boolean flag = userDBUtil.deleteUserInfo(deleteList);
			return flag + "";
		}

		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID_DELETE);
			if ("true".equals(result)) {
				List<UserBean> list_copy = new ArrayList<UserBean>();
				for (int i = 0; i < list.size(); i++) {
					if (!deleteList.contains(list.get(i).getId())) {
						list_copy.add(list.get(i));
					}
					;
				}
				list = list_copy;
				// 实例化适配器
				adapter = new UserAdapter(UserActivity.this, list);
				// 为ListView添加适配器
				userListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				Toast.makeText(UserActivity.this,
						getText(R.string.delete_success), Toast.LENGTH_SHORT)
						.show();
			} else if ("false".equals(result)) {
				Toast.makeText(UserActivity.this,
						getText(R.string.delete_failure), Toast.LENGTH_SHORT)
						.show();
			} else if ("empty".equals(result)) {
				Toast.makeText(UserActivity.this, getText(R.string.list_empty),
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID_DELETE);
		}
	}

	/**
	 * 按下menu键时的回调方法
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 为菜单添加子选项
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getText(R.string.exit)).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/**
	 * 菜单中选项被选中时的回调方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:// 退出
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(android.R.drawable.ic_menu_help);
			dialog.setTitle(getText(R.string.confirm_exit));
			dialog.setPositiveButton(getText(R.string.ensure),
					new OnClickLiner_OK());
			dialog.setNegativeButton(getText(R.string.cancel),
					new OnClickLiner_Cancel());
			dialog.show();
			return true;
		}
		return false;
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
			finish();
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

	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 判断返回键是否被按下
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID_QUERY:
			ProgressDialog dialog = new ProgressDialog(UserActivity.this);
			dialog.setMessage(getText(R.string.querying));
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog;
		case DIALOG_ID_DELETE:
			ProgressDialog dialog1 = new ProgressDialog(UserActivity.this);
			dialog1.setMessage(getText(R.string.deleting));
			dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog1;
		default:
			return null;
		}
	}
}
