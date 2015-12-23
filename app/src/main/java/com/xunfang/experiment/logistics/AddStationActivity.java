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

import com.xunfang.experiment.logistics.bean.CityBean;
import com.xunfang.experiment.logistics.bean.ProvinceBean;
import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.db.StationDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：站点添加界面</p>
 * <p>Company </p>
 * <p>Copyright: Copyright (c) 2012 </p>
 * @version 1.0.0.0
 * @author sas 
 */
public class AddStationActivity extends Activity implements OnClickListener{
	
	private final int DIALOG_ID = 3;
	//定义控件
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	private TextView provincestr;//所在省份(字符串)
	private Button province;//所在省份
	private TextView citystr;//所在城市(字符串)
	private Button city;//所在城市
	private TextView namestr;//站点名称(字符串)
	private EditText name;//站点名称
	private Button save_btn;//保存按钮
	private Button cancel_btn;//取消按钮
	
	private StationBean stationBean;
	private String operate;//操作(添加或编辑)
	private String station_name = "";
	private int station_addr = -1;
	private List<ProvinceBean> provinceList;
	private List<CityBean> cityList;
	private ProvinceBean selectedProvince = null;
	private CityBean selectedCity = null;
	private String list_type;//标识获取省份列表还是城市列表
	private ProvinceAdapter p_adapter;
	private CityAdapter c_adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.add_station);
        //自定义窗口样式
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		provincestr = (TextView)findViewById(R.id.station_add_provincestr);
		province = (Button)findViewById(R.id.station_add_province);
		citystr = (TextView)findViewById(R.id.station_add_citystr);
		city = (Button)findViewById(R.id.station_add_city);
		namestr = (TextView)findViewById(R.id.station_add_namestr);
		name = (EditText)findViewById(R.id.station_add_name);
		save_btn = (Button)findViewById(R.id.station_add_save);
		cancel_btn = (Button)findViewById(R.id.station_add_cancel);
		stationBean = (StationBean)getIntent().getSerializableExtra("stationBean");
		System.out.println("stationBean==null--->"+stationBean==null);
		revert.setOnClickListener(this);
		provincestr.setText(getText(R.string.provincestr)+"：");
		citystr.setText(getText(R.string.citystr)+"：");
		namestr.setText(getText(R.string.stationnamestr)+"：");
		save_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		province.setOnClickListener(this);
		city.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if("".equals(MyConfig.getNOW_LOGINNAME())){
    		//Intent intent = new Intent(AccessRightActivity.this,LoginActivity.class);
			Intent intent = new Intent(AddStationActivity.this,Main.class);
			Main.setChecked(R.id.main_tab_login);
    		startActivity(intent);
    		finish();
    	}
    	if(stationBean!=null){
    		operate = "edit";
    		title.setText(getText(R.string.stationMgr)+"——"+getText(R.string.edit));
    		province.setText(stationBean.getProvince_name());
    		city.setText(stationBean.getCity_name());
    		name.setText(stationBean.getName());
    		station_addr = stationBean.getCity_code();
    		station_name = stationBean.getName();
    	}else{
    		operate = "add";
    		title.setText(getText(R.string.stationMgr)+"——"+getText(R.string.add));
    	}
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.station_add_save:
			station_name = name.getText().toString();
			if("add".equals(operate)){
				if("".equals(station_name)){
					Toast.makeText(AddStationActivity.this, getText(R.string.stationname_empty), Toast.LENGTH_SHORT).show();
				}else if(station_addr == -1){
					Toast.makeText(AddStationActivity.this, getText(R.string.stationaddr_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}else if("edit".equals(operate)){
				System.out.println("edit");
				if("".equals(station_name)){
					Toast.makeText(AddStationActivity.this, getText(R.string.stationname_empty), Toast.LENGTH_SHORT).show();
				}else if(station_addr == -1){
					Toast.makeText(AddStationActivity.this, getText(R.string.stationaddr_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}
			break;
		case R.id.common_title_revert:
		case R.id.station_add_cancel:
			//startActivity(new Intent(AddStationActivity.this,StationActivity.class));
			finish();
			break;
		case R.id.station_add_province:
			list_type = "province";
			new GetProvinceorCityListTask().execute(list_type);
			break;
		case R.id.station_add_city:
			if(selectedProvince != null){
				list_type = "city";
				new GetProvinceorCityListTask().execute(list_type);
			}else{
				Toast.makeText(AddStationActivity.this, getText(R.string.select_province), Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 异步操作类——执行获取省份/城市列表的操作
	 * @author sas
	 *
	 */
	class GetProvinceorCityListTask extends AsyncTask<String, Integer, String>{

		/**
		 * 后台执行获取省份/城市列表的操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			StationDBUtil stationDBUtil = new StationDBUtil(AddStationActivity.this);
			if("province".equals(params[0])){
				provinceList = stationDBUtil.getProvinceList();
				System.out.println("provinceList.size()--->"+provinceList.size());
				if(provinceList != null) return "true";
				else return "false";
			}else if("city".equals(params[0])){
				cityList = stationDBUtil.getCityList(selectedProvince.getCode());
				System.out.println("cityList.size()--->"+cityList.size());
				if(cityList != null) return "true";
				else return "false";
			}else{
				return false+"";
			}
		}
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//dialog.dismiss();
			if("true".equals(result)){
				if("province".equals(list_type)){
					//创建省份适配器的对象
			        p_adapter = new ProvinceAdapter(AddStationActivity.this, provinceList);
					//通过AlertDialog列出省份列表供用户选择
					new AlertDialog.Builder(AddStationActivity.this)
		            .setTitle(getText(R.string.select_province))//设置标题
		            .setNegativeButton(getText(R.string.cancel), null)
		            .setAdapter(p_adapter,new DialogInterface.OnClickListener() {
						//通过适配器给AlertDialog设置内容，并设置监听器
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//获得选中的省份，并设置选中的省份
							selectedProvince = provinceList.get(which);
							province.setText(selectedProvince.getName());
							StationDBUtil stationDBUtil = new StationDBUtil(AddStationActivity.this);
							cityList = stationDBUtil.getCityList(selectedProvince.getCode());
							selectedCity = cityList.get(0);
							city.setText(selectedCity.getName());
							station_addr = selectedCity.getCode();
						}
					}).create().show();
				}else if("city".equals(list_type)){
					//创建城市适配器的对象
			        c_adapter = new CityAdapter(AddStationActivity.this, cityList);
					//通过AlertDialog列出城市列表供用户选择
					new AlertDialog.Builder(AddStationActivity.this)
		            .setTitle(getText(R.string.stationaddr_empty))//设置标题
		            .setNegativeButton(getText(R.string.cancel), null)
		            .setAdapter(c_adapter,new DialogInterface.OnClickListener() {
						//通过适配器给AlertDialog设置内容，并设置监听器
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//获得选中的城市，并设置选中的城市
							selectedCity = cityList.get(which);
							city.setText(selectedCity.getName());
							station_addr = selectedCity.getCode();
						}
					}).create().show();
				}
			}else{
				Toast.makeText(AddStationActivity.this, getText(R.string.query_failure), Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//dialog = ProgressDialog.show(AddStationActivity.this,null,getText(R.string.querying),true);
		}
	}
	
	/**
	 * 异步操作——添加或编辑站点
	 * @author sas
	 *
	 */
	class OperateTask extends AsyncTask<String, String, String>{

		/**
		 * 后台执行添加或编辑站点操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean flag = false;
			StationDBUtil stationDBUtil = new StationDBUtil(AddStationActivity.this);
			if("add".equals(params[0])){
				ContentValues values = new ContentValues();
				values.put("name", station_name);
				values.put("addr", station_addr);
				flag = stationDBUtil.addStation(values);
			}else if("edit".equals(params[0])){
				flag = stationDBUtil.updateStation(station_name, station_addr, stationBean.getId());
			}
			return flag+"";
		}
		/**
		 * 执行doInBackground前被调用
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(DIALOG_ID);
		}
		/**
		 * 执行doInBackground后被调用
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dismissDialog(DIALOG_ID);
			if("true".equals(result)){
				Intent intent = new Intent(AddStationActivity.this,StationActivity.class);
				startActivity(intent);
				finish();
			}else if("false".equals(result)){
				Toast.makeText(AddStationActivity.this, getText(R.string.operate_failure), Toast.LENGTH_SHORT).show();
			}
		}
	}
	/**
	 * 自定义适配器
	 * @author sas
	 *
	 */
	class ProvinceAdapter extends BaseAdapter{

		private Context mContext;   
	    private LayoutInflater mInflater; 
	    private List<ProvinceBean> list ;
	    
	    public ProvinceAdapter(Context content,List<ProvinceBean> list){
	    	this.mContext = content;
	    	this.mInflater = LayoutInflater.from(mContext);
	    	this.list = list;
	    }
	    /**
	     * 返回适配器中子选项的数目
	     */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		/**
		 * 返回数据集中指定位置的对象
		 */
		@Override
		public ProvinceBean getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}
		/**
		 * 返回数据集中指定位置的row id
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		/**
		 * 获得数据集中指定位置的视图
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();   
	            convertView = mInflater.inflate(R.layout.spinner_item, null);   
	            holder.name = (TextView) convertView.findViewById(R.id.spinner_item_name);   
	            convertView.setTag(holder);  
			}else holder = (ViewHolder)convertView.getTag();
			ProvinceBean province = list.get(position);
			holder.name.setText(province.getName());
			return convertView;
		}
		final class ViewHolder{
			TextView id;
			TextView name;
		}
	} 
	/**
	 * 自定义适配器
	 * @author sas
	 *
	 */
	class CityAdapter extends BaseAdapter{

		private Context mContext;   
	    private LayoutInflater mInflater; 
	    private List<CityBean> list ;
	    
	    public CityAdapter(Context content,List<CityBean> list){
	    	this.mContext = content;
	    	this.mInflater = LayoutInflater.from(mContext);
	    	this.list = list;
	    }
	    /**
	     * 返回适配器中子选项的数目
	     */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		/**
		 * 返回数据集中指定位置的对象
		 */
		@Override
		public CityBean getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}
		/**
		 * 返回数据集中指定位置的row id
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		/**
		 * 获得数据集中指定位置的视图
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();   
	            convertView = mInflater.inflate(R.layout.spinner_item, null);   
	            holder.name = (TextView) convertView.findViewById(R.id.spinner_item_name);   
	            convertView.setTag(holder);  
			}else holder = (ViewHolder)convertView.getTag();
			CityBean city = list.get(position);
			holder.name.setText(city.getName());
			return convertView;
		}
		final class ViewHolder{
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
			//startActivity(new Intent(AddStationActivity.this,StationActivity.class));
			finish();
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(AddStationActivity.this); 
			dialog.setMessage(getText(R.string.operating)); 
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog; 
		default:
			return null;
		}
	}
}