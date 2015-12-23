package com.xunfang.experiment.logistics;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.db.StationDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：物品出入站选择界面
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
 * 
 */
public class GoodsInorOutActivity extends Activity implements OnClickListener{

	private final int DIALOG_ID = 9;
	//更新界面的Handler
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	private TextView typestr;//类型选择(字符串)
	private Button type;//类型选择(字符串)
	private TextView addrstr;//下一站地址(字符串)
	private Button nextstation_btn;//下一站地址(字符串)
	private Button type_btn;//类型选择按钮
	private Button operate_save;//保存按钮
	private Button operate_cancel;//取消按钮
	private RelativeLayout addr_module;//地址选择模块
	
	private StationBean selectedStation;//选择的收件地址
	private List<StationBean> stationList;
	private List<String> inoroutList;
	
	private InorOutAdapter i_adapter;
	private StationAdapter s_adapter;
	
	private int goods_receiveaddr = -1;
	private String operate = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.goods_inorout);
		// 自定义窗口样式
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		title.setText(R.string.inoroutMgr);
		typestr = (TextView)findViewById(R.id.goods_inorout_typestr);
		type_btn = (Button)findViewById(R.id.goods_inorout_type);
		addrstr = (TextView)findViewById(R.id.goods_inorout_addrstr);
		nextstation_btn = (Button)findViewById(R.id.goods_inorout_addr);
		addr_module = (RelativeLayout)findViewById(R.id.goods_inorout_addr_module);
		operate_save = (Button)findViewById(R.id.goods_inorout_operate_save);
		operate_cancel = (Button)findViewById(R.id.goods_inorout_operate_cancel);
		typestr.setText(getText(R.string.operate_select)+"：");
		addrstr.setText(getText(R.string.nextstationstr)+"：");
		revert.setOnClickListener(this);
		type_btn.setOnClickListener(this);
		nextstation_btn.setOnClickListener(this);
		operate_save.setOnClickListener(this);
		operate_cancel.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if("".equals(MyConfig.getNOW_LOGINNAME())){
			Intent intent = new Intent(GoodsInorOutActivity.this,Main.class);
			Main.setChecked(R.id.main_tab_login);
    		startActivity(intent);
    		finish();
    	}
	}
	public static void ParseMessage(Bundle message) {
		Message msg = new Message();
		msg.setData(message);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view instanceof ImageView || view instanceof Button){
			switch (view.getId()) {
			case R.id.goods_inorout_type:
				//获得出入站类型列表
				inoroutList = MyConfig.getInoroutList();
		        //创建出入站类型适配器的对象
		        i_adapter = new InorOutAdapter(GoodsInorOutActivity.this, inoroutList);
				//通过AlertDialog列出出入站类型列表供用户选择
				new AlertDialog.Builder(GoodsInorOutActivity.this)
	            .setTitle(getText(R.string.select_optype))//设置标题
	            .setNegativeButton(getText(R.string.cancel), null)
	            .setAdapter(i_adapter,new DialogInterface.OnClickListener() {
					//通过适配器给AlertDialog设置内容，并设置监听器
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//获得选中的出入站类型，并设置选中的出入站类型
						String type = inoroutList.get(which);
						operate = type;
						if(getText(R.string.station_in).equals(type)){
							addr_module.setVisibility(View.GONE);
							type_btn.setText(type);
							goods_receiveaddr = -1;
						}else if(getText(R.string.station_out).equals(type)){
							addr_module.setVisibility(View.VISIBLE);
							type_btn.setText(type);
							goods_receiveaddr = -1;
						}else if(getText(R.string.waitfordelivery).equals(type)){
							addr_module.setVisibility(View.GONE);
							type_btn.setText(type);
							goods_receiveaddr = -1;
						}
					}
				}).create().show();
				break;
			case R.id.goods_inorout_addr:
				new OperateTask().execute("");
				break;
			case R.id.goods_inorout_operate_save:
				if(getText(R.string.station_in).equals(operate)){
					Intent intent = new Intent(GoodsInorOutActivity.this,StationInActivity.class);
					startActivity(intent);
					//finish();
				}else if(getText(R.string.station_out).equals(operate)){
					if(goods_receiveaddr != -1){
						Intent intent = new Intent(GoodsInorOutActivity.this,StationOutActivity.class);
						//intent.putExtra("nextstation", selectedStation);
						MyConfig.nextStation = selectedStation;
						startActivity(intent);
						//finish();
					}else{
						Toast.makeText(GoodsInorOutActivity.this, "请选择下一站", Toast.LENGTH_SHORT).show();
					}
					
				}else if(getText(R.string.waitfordelivery).equals(operate)){
					Intent intent = new Intent(GoodsInorOutActivity.this,WaitforDeliveryActivity.class);
					startActivity(intent);
					//finish();
				}else{
					Toast.makeText(GoodsInorOutActivity.this, "请选择操作类型", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.goods_inorout_operate_cancel:
			case R.id.common_title_revert:
				/*if(MyConfig.getNOW_USERTYPE() == 0){//管理员
					startActivity(new Intent(GoodsInorOutActivity.this,Main_bak.class));
				}else if(MyConfig.getNOW_USERTYPE() == 1){//派件员
					startActivity(new Intent(GoodsInorOutActivity.this,DeliveryMain.class));
				}*/
				finish();
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 自定义站点信息适配器
	 * @author sas
	 *
	 */
	class StationAdapter extends BaseAdapter{

		private Context mContext;   
	    private LayoutInflater mInflater; 
	    private List<StationBean> list ;
	    
	    public StationAdapter(Context content,List<StationBean> list){
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
		public StationBean getItem(int position) {
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
			StationBean station = list.get(position);
			holder.name.setText(station.getName());
			return convertView;
		}
		final class ViewHolder{
			TextView id;
			TextView name;
		}
	} 
	/**
	 * 自定义出入站类型适配器
	 * @author sas
	 *
	 */
	class InorOutAdapter extends BaseAdapter{

		private Context mContext;   
	    private LayoutInflater mInflater; 
	    private List<String> list ;
	    
	    public InorOutAdapter(Context content,List<String> list){
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
		public String getItem(int position) {
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
			String type = list.get(position);
			holder.name.setText(type);
			return convertView;
		}
		final class ViewHolder{
			TextView id;
			TextView name;
		}
	} 
	/**
	 * 异步操作类
	 * @author sas
	 *
	 */
	class OperateTask extends AsyncTask<String, String, String>{

		/**
		 * 后台执行耗时操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			StationDBUtil stationDBUtil = new StationDBUtil(GoodsInorOutActivity.this);
			stationList = stationDBUtil.getStationList();
			if(stationList==null){
				return false+"";
			}else{
				return true+"";
			}
		}
		/**
		 * 执行doInBackground前被调用
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			System.out.println("操作--->"+operate);
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
					//通过AlertDialog列出站点信息列表供用户选择
					new AlertDialog.Builder(GoodsInorOutActivity.this)
		            .setTitle(getText(R.string.select_station))//设置标题
		            .setNegativeButton(getText(R.string.cancel), null)
		            .setAdapter(new StationAdapter(GoodsInorOutActivity.this, stationList),new DialogInterface.OnClickListener() {
						//通过适配器给AlertDialog设置内容，并设置监听器
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//获得选中的站点，并设置选中的站点
							selectedStation = stationList.get(which);
							nextstation_btn.setText(selectedStation.getName());
							goods_receiveaddr = selectedStation.getId();
						}
					}).create().show();
			}else if("false".equals(result)){
				Toast.makeText(GoodsInorOutActivity.this, getText(R.string.operate_failure), Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	/**
	 * 监听返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 判断返回键是否被按下
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			/*if(MyConfig.getNOW_USERTYPE() == 0){//管理员
				startActivity(new Intent(GoodsInorOutActivity.this,Main_bak.class));
			}else if(MyConfig.getNOW_USERTYPE() == 1){//派件员
				startActivity(new Intent(GoodsInorOutActivity.this,DeliveryMain.class));
			}*/
			finish();
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(GoodsInorOutActivity.this); 
			dialog.setMessage(getText(R.string.operating)); 
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog; 
		default:
			return null;
		}
	}
}
