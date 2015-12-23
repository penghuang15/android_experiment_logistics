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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.experiment.logistics.bean.GoodsInfoBean;
import com.xunfang.experiment.logistics.bean.GoodsTypeBean;
import com.xunfang.experiment.logistics.db.GoodsInfoDBUtil;
import com.xunfang.experiment.logistics.db.GoodsTypeDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：货物信息添加/编辑界面</p>
 * <p>Company </p>
 * <p>Copyright: Copyright (c) 2012 </p>
 * @version 1.0.0.0
 * @author sas 
 */
public class AddGoodsInfoActivity extends Activity implements OnClickListener{
	
	private final int DIALOG_ID = 1;
	
	//定义控件
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	private TextView typestr;//所属类型(字符串)
	private Button type;//所属类型
	private TextView namestr;//货物名称(字符串)
	private EditText name;//货物名称
	private Button save_btn;//保存按钮
	private Button cancel_btn;//取消按钮
	
	private GoodsInfoBean goodsInfoBean;
	private String operate;//操作(添加或编辑)
	private String goods_name = "";
	private int goods_type = -1;
	private List<GoodsTypeBean> goodsTypeList;
	private GoodsTypeBean selectedType;
	private GoodsTypeAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.add_goodsinfo);
        //自定义窗口样式
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		typestr = (TextView)findViewById(R.id.goodsinfo_add_typestr);
		type = (Button)findViewById(R.id.goodsinfo_add_type);
		namestr = (TextView)findViewById(R.id.goodsinfo_add_namestr);
		name = (EditText)findViewById(R.id.goodsinfo_add_name);
		save_btn = (Button)findViewById(R.id.goodsinfo_add_save);
		cancel_btn = (Button)findViewById(R.id.goodsinfo_add_cancel);
		goodsInfoBean = (GoodsInfoBean)getIntent().getSerializableExtra("goodsInfoBean");
		System.out.println("goodsInfoBean==null--->"+goodsInfoBean==null);
		revert.setOnClickListener(this);
		typestr.setText(getText(R.string.goodstypestr)+"：");
		namestr.setText(getText(R.string.goodsnamestr)+"：");
		save_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		type.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if("".equals(MyConfig.getNOW_LOGINNAME())){
    		//Intent intent = new Intent(AccessRightActivity.this,LoginActivity.class);
			Intent intent = new Intent(AddGoodsInfoActivity.this,Main.class);
			Main.setChecked(R.id.main_tab_login);
    		startActivity(intent);
    		finish();
    	}
    	if(goodsInfoBean!=null){
    		operate = "edit";
    		title.setText(getText(R.string.goodsinfoMgr)+"——"+getText(R.string.edit));
    		type.setText(goodsInfoBean.getType_name());
    		name.setText(goodsInfoBean.getName());
    		goods_name = goodsInfoBean.getName();
    		goods_type = goodsInfoBean.getType_code();
    	}else{
    		operate = "add";
    		title.setText(getText(R.string.goodsinfoMgr)+"——"+getText(R.string.add));
    	}
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goodsinfo_add_save:
			goods_name = name.getText().toString();
			if("add".equals(operate)){
				if("".equals(goods_name)){
					Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.goodsname_empty), Toast.LENGTH_SHORT).show();
				}else if(goods_type == -1){
					Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.goodstype_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}else if("edit".equals(operate)){
				System.out.println("edit");
				if(goods_type == -1){
					Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.goodstype_empty), Toast.LENGTH_SHORT).show();
				}else if("".equals(goods_name)){
					Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.goodsname_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}
			break;
		case R.id.common_title_revert:
		case R.id.goodsinfo_add_cancel:
			//startActivity(new Intent(AddGoodsInfoActivity.this,GoodsInfoActivity.class));
			finish();
			break;
		case R.id.goodsinfo_add_type:
			new GetGoodsTypeListTask().execute("");
			break;
		default:
			break;
		}
	}
	/**
	 * 异步操作类——执行获取货物类型列表的操作
	 * @author sas
	 *
	 */
	class GetGoodsTypeListTask extends AsyncTask<String, Integer, String>{

		/**
		 * 后台执行获取货物类型列表的操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			GoodsTypeDBUtil goodsTypeDBUtil = new GoodsTypeDBUtil(AddGoodsInfoActivity.this);
			goodsTypeList = goodsTypeDBUtil.getGoodsTypeList();
			System.out.println("goodsTypeList.size()--->"+goodsTypeList.size());
			if(goodsTypeList != null) return "true";
			else return "false";
		}
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//dialog.dismiss();
			if("true".equals(result)){
				//创建货物类型适配器的对象
		        adapter = new GoodsTypeAdapter(AddGoodsInfoActivity.this, goodsTypeList);
				//通过AlertDialog列出省份列表供用户选择
				new AlertDialog.Builder(AddGoodsInfoActivity.this)
	            .setTitle(getText(R.string.select_goodstype))//设置标题
	            .setAdapter(adapter,new DialogInterface.OnClickListener() {
					//通过适配器给AlertDialog设置内容，并设置监听器
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//获得选中的货物类型，并设置选中的货物类型
						selectedType = goodsTypeList.get(which);
						type.setText(selectedType.getName());
						goods_type = selectedType.getId();
					}
				}).create().show();
			}else{
				Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.query_failure), Toast.LENGTH_SHORT).show();
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
	 * 异步操作——添加或编辑货物信息
	 * @author sas
	 *
	 */
	class OperateTask extends AsyncTask<String, String, String>{

		/**
		 * 后台执行添加或编辑货物信息操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean flag = false;
			GoodsInfoDBUtil goodsInfoDBUtil = new GoodsInfoDBUtil(AddGoodsInfoActivity.this);
			if("add".equals(params[0])){
				ContentValues values = new ContentValues();
				values.put("typeid", goods_type);
				values.put("name", goods_name);
				flag = goodsInfoDBUtil.addGoods(values);
			}else if("edit".equals(params[0])){
				flag = goodsInfoDBUtil.updateGoods(goods_type, goods_name, goodsInfoBean.getId());
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
				Intent intent = new Intent(AddGoodsInfoActivity.this,GoodsInfoActivity.class);
				startActivity(intent);
				finish();
			}else if("false".equals(result)){
				Toast.makeText(AddGoodsInfoActivity.this, getText(R.string.operate_failure), Toast.LENGTH_SHORT).show();
			}
		}
	}
	/**
	 * 自定义适配器
	 * @author sas
	 *
	 */
	class GoodsTypeAdapter extends BaseAdapter{

		private Context mContext;   
	    private LayoutInflater mInflater; 
	    private List<GoodsTypeBean> list ;
	    
	    public GoodsTypeAdapter(Context content,List<GoodsTypeBean> list){
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
		public GoodsTypeBean getItem(int position) {
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
			GoodsTypeBean goodstype = list.get(position);
			holder.name.setText(goodstype.getName());
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
			//startActivity(new Intent(AddGoodsInfoActivity.this,GoodsInfoActivity.class));
			finish();
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(AddGoodsInfoActivity.this); 
			dialog.setMessage(getText(R.string.operating)); 
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog; 
		default:
			return null;
		}
	}
}