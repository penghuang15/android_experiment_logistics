package com.xunfang.experiment.logistics;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.bean.UserBean;
import com.xunfang.experiment.logistics.bean.UserTypeBean;
import com.xunfang.experiment.logistics.db.StationDBUtil;
import com.xunfang.experiment.logistics.db.UserDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：操作员添加界面
 * </p>
 * <p>
 * Company
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author sas
 */
public class AddUserActivity extends Activity implements OnClickListener {

	private final int DIALOG_ID = 4;
	// 定义控件
	private ImageView revert;// 返回"按钮"
	private TextView title;// 标题
	private TextView typestr;// 操作员类型(字符串)
	private Button type;// 操作员类型
	private TextView stationstr;// 所属站点(字符串)
	private Button station;// 所属站点
	private TextView loginnamestr;// 登录名(字符串)
	private EditText loginname;// 登录名
	private TextView passwordstr;// 密码(字符串)
	private EditText password;// 密码
	private TextView repasswordstr;// 确认密码(字符串)
	private EditText repassword;// 确认密码
	private TextView usernamestr;// 用户名(字符串)
	private EditText username;// 用户名
	private Button save_btn;// 保存按钮
	private Button cancel_btn;// 取消按钮

	private UserBean userBean;
	private String operate;// 操作(添加或编辑)
	private String login_name = "";
	private String login_password = "";
	private String login_repassword = "";
	private String login_username = "";
	private int login_type = -1;
	private int login_station = -1;
	private List<UserTypeBean> userTypeList;
	private UserTypeBean selectedType;
	private TypeAdapter adapter;
	private List<StationBean> stationList;
	private StationBean selectedStation;
	private StationAdapter s_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.add_user);
		// 自定义窗口样式
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView) findViewById(R.id.common_title_revert);
		title = (TextView) findViewById(R.id.common_title_title);
		typestr = (TextView) findViewById(R.id.user_add_typestr);
		type = (Button) findViewById(R.id.user_add_type);
		stationstr = (TextView) findViewById(R.id.user_add_stationstr);
		station = (Button) findViewById(R.id.user_add_station);
		loginnamestr = (TextView) findViewById(R.id.user_add_loginnamestr);
		loginname = (EditText) findViewById(R.id.user_add_loginname);
		passwordstr = (TextView) findViewById(R.id.user_add_passwordstr);
		password = (EditText) findViewById(R.id.user_add_password);
		repasswordstr = (TextView) findViewById(R.id.user_add_repasswordstr);
		repassword = (EditText) findViewById(R.id.user_add_repassword);
		usernamestr = (TextView) findViewById(R.id.user_add_usernamestr);
		username = (EditText) findViewById(R.id.user_add_username);
		save_btn = (Button) findViewById(R.id.user_add_save);
		cancel_btn = (Button) findViewById(R.id.user_add_cancel);
		userBean = (UserBean) getIntent().getSerializableExtra("userBean");
		revert.setOnClickListener(this);
		loginnamestr.setText(getText(R.string.loginnamestr) + "：");
		typestr.setText(getText(R.string.typestr) + "：");
		stationstr.setText(getText(R.string.stationstr) + "：");
		passwordstr.setText(getText(R.string.passwordstr) + "：");
		repasswordstr.setText(getText(R.string.repasswordstr) + "：");
		usernamestr.setText(getText(R.string.usernamestr) + "：");
		save_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		type.setOnClickListener(this);
		station.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ("".equals(MyConfig.getNOW_LOGINNAME())) {
			Intent intent = new Intent(AddUserActivity.this, Main.class);
			Main.setChecked(R.id.main_tab_login);
			startActivity(intent);
			finish();
		}
		if (userBean != null) {
			operate = "edit";
			title.setText(getText(R.string.userMgr) + "——"
					+ getText(R.string.edit));
			loginname.setText(userBean.getLoginname());
			username.setText(userBean.getUsername());
			type.setText(MyConfig.getUserTypeList().get(userBean.getType())
					.getTypename());
			login_type = userBean.getType();
			login_station = userBean.getStationid();
			station.setText(userBean.getStationname());
		} else {
			operate = "add";
			title.setText(getText(R.string.userMgr) + "——"
					+ getText(R.string.add));
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_add_save:
			login_name = loginname.getText().toString();
			login_password = password.getText().toString();
			login_repassword = repassword.getText().toString();
			login_username = username.getText().toString();
			if ("add".equals(operate)) {
				if (login_type == -1) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.select_usertype),
							Toast.LENGTH_SHORT).show();
				} else if (login_station == -1) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.select_station),
							Toast.LENGTH_SHORT).show();
				} else if (login_name == null || "".equals(login_name)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.loginname_empty),
							Toast.LENGTH_SHORT).show();
				} else if (login_password == null || "".equals(login_password)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.password_empty),
							Toast.LENGTH_SHORT).show();
				} else if (login_repassword == null
						|| "".equals(login_repassword)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.repassword_empty),
							Toast.LENGTH_SHORT).show();
				} else if (!login_password.equals(login_repassword)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.repassword_err),
							Toast.LENGTH_SHORT).show();
				} else if (login_username == null || "".equals(login_username)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.username_empty),
							Toast.LENGTH_SHORT).show();
				} else {
					new OperateTask().execute(operate);
				}
			} else if ("edit".equals(operate)) {
				System.out.println("edit");
				if (login_type != -1) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.select_usertype),
							Toast.LENGTH_SHORT).show();
				} else if (login_station == -1) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.select_station),
							Toast.LENGTH_SHORT).show();
				} else if ("".equals(login_name)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.loginname_empty),
							Toast.LENGTH_SHORT).show();
				} else if ("".equals(login_password)) {
					if (!"".equals(login_repassword)) {
						Toast.makeText(AddUserActivity.this,
								getText(R.string.password_empty),
								Toast.LENGTH_SHORT).show();
					} else {
						if (!login_password.equals(login_repassword)) {
							Toast.makeText(AddUserActivity.this,
									getText(R.string.repassword_err),
									Toast.LENGTH_SHORT).show();
						} else if ("".equals(login_username)) {
							Toast.makeText(AddUserActivity.this,
									getText(R.string.username_empty),
									Toast.LENGTH_SHORT).show();
						} else if (login_type == -1) {
							Toast.makeText(AddUserActivity.this,
									getText(R.string.select_usertype),
									Toast.LENGTH_SHORT).show();
						} else {
							if ("".equals(login_password)) {
								login_password = userBean.getPassword();
							}
							new OperateTask().execute(operate);
						}
					}
				} else if (!login_password.equals(login_repassword)) {
					Toast.makeText(AddUserActivity.this,
							getText(R.string.repassword_err),
							Toast.LENGTH_SHORT).show();
				} else {
					if ("".equals(login_password)) {
						login_password = userBean.getPassword();
					}
					new OperateTask().execute(operate);
				}
			}
			break;
		case R.id.common_title_revert:
		case R.id.user_add_cancel:
			finish();
			break;
		case R.id.user_add_type:
			// 获得操作员类型列表
			userTypeList = MyConfig.getUserTypeList();
			// 创建操作员类型适配器的对象
			adapter = new TypeAdapter(AddUserActivity.this, userTypeList);
			// 通过AlertDialog列出操作员类型列表供用户选择
			new AlertDialog.Builder(AddUserActivity.this)
					.setTitle(getText(R.string.select_usertype))
					// 设置标题
					.setNegativeButton(getText(R.string.cancel), null)
					.setAdapter(adapter, new DialogInterface.OnClickListener() {
						// 通过适配器给AlertDialog设置内容，并设置监听器
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 获得选中的操作员类型，并设置选中的操作员类型
							selectedType = userTypeList.get(which);
							type.setText(selectedType.getTypename());
							login_type = selectedType.getId();
						}
					}).create().show();
			break;
		case R.id.user_add_station:
			new GetStationListTask().execute("");
			break;
		default:
			break;
		}
	}

	/**
	 * 异步操作——添加或编辑操作员
	 * 
	 * @author sas
	 * 
	 */
	class OperateTask extends AsyncTask<String, String, String> {

		/**
		 * 后台执行添加或编辑操作员操作
		 */
		@Override
		protected String doInBackground(String... params) {
			boolean flag = false;
			UserDBUtil userDBUtil = new UserDBUtil(AddUserActivity.this);
			if ("add".equals(params[0])) {
				ContentValues values = new ContentValues();
				values.put("loginname", login_name);
				values.put("password", login_password);
				values.put("username", login_username);
				values.put("type", login_type);
				values.put("stationid", login_station);
				flag = userDBUtil.addUserInfo(values);
			} else if ("edit".equals(params[0])) {
				flag = userDBUtil.updateUserInfo(login_name, login_password,
						login_username, login_type, login_station,
						userBean.getId());
			}
			return flag + "";
		}

		/**
		 * 执行doInBackground前被调用
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID);
		}

		/**
		 * 执行doInBackground后被调用
		 */
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID);
			if ("true".equals(result)) {
				Intent intent = new Intent(AddUserActivity.this,
						UserActivity.class);
				startActivity(intent);
				finish();
			} else if ("false".equals(result)) {
				Toast.makeText(AddUserActivity.this,
						getText(R.string.operate_failure), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * 自定义适配器
	 * 
	 * @author sas
	 * 
	 */
	class TypeAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private List<UserTypeBean> list;

		public TypeAdapter(Context content, List<UserTypeBean> list) {
			this.mContext = content;
			this.mInflater = LayoutInflater.from(mContext);
			this.list = list;
		}

		/**
		 * 返回适配器中子选项的数目
		 */
		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * 返回数据集中指定位置的对象
		 */
		@Override
		public UserTypeBean getItem(int position) {
			return list.get(position);
		}

		/**
		 * 返回数据集中指定位置的row id
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 获得数据集中指定位置的视图
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.spinner_item, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.spinner_item_name);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();
			UserTypeBean usertype = list.get(position);
			holder.name.setText(usertype.getTypename());
			return convertView;
		}

		final class ViewHolder {
			TextView id;
			TextView name;
		}
	}

	/**
	 * 异步操作类——执行获取站点信息列表的操作
	 * 
	 * @author sas
	 * 
	 */
	class GetStationListTask extends AsyncTask<String, Integer, String> {

		/**
		 * 后台执行获取站点信息列表的操作
		 */
		@Override
		protected String doInBackground(String... params) {
			StationDBUtil stationDBUtil = new StationDBUtil(
					AddUserActivity.this);
			stationList = stationDBUtil.getStationList();
			if (stationDBUtil != null)
				return "true";
			else
				return "false";
		}

		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			if ("true".equals(result)) {
				// 创建站点信息适配器的对象
				s_adapter = new StationAdapter(AddUserActivity.this,
						stationList);
				// 通过AlertDialog列出站点信息列表供用户选择
				new AlertDialog.Builder(AddUserActivity.this)
						.setTitle(getText(R.string.select_station))
						// 设置标题
						.setNegativeButton(getText(R.string.cancel), null)
						.setAdapter(s_adapter,
								new DialogInterface.OnClickListener() {
									// 通过适配器给AlertDialog设置内容，并设置监听器
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 获得选中的站点信息，并设置选中的站点
										selectedStation = stationList
												.get(which);
										station.setText(selectedStation
												.getName());
										login_station = selectedStation.getId();
									}
								}).create().show();
			} else {
				Toast.makeText(AddUserActivity.this,
						getText(R.string.query_failure), Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	/**
	 * 自定义适配器
	 * 
	 * @author sas
	 * 
	 */
	class StationAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private List<StationBean> list;

		public StationAdapter(Context content, List<StationBean> list) {
			this.mContext = content;
			this.mInflater = LayoutInflater.from(mContext);
			this.list = list;
		}

		/**
		 * 返回适配器中子选项的数目
		 */
		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * 返回数据集中指定位置的对象
		 */
		@Override
		public StationBean getItem(int position) {
			return list.get(position);
		}

		/**
		 * 返回数据集中指定位置的row id
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 获得数据集中指定位置的视图
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.spinner_item, null);
				holder.name = (TextView) convertView
						.findViewById(R.id.spinner_item_name);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();
			StationBean station = list.get(position);
			holder.name.setText(station.getName());
			return convertView;
		}

		final class ViewHolder {
			TextView id;
			TextView name;
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
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(AddUserActivity.this);
			dialog.setMessage(getText(R.string.operating));
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog;
		default:
			return null;
		}
	}
}