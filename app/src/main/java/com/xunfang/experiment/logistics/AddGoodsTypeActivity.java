package com.xunfang.experiment.logistics;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xunfang.experiment.logistics.bean.GoodsTypeBean;
import com.xunfang.experiment.logistics.db.GoodsTypeDBUtil;
import com.xunfang.experiment.logistics.util.MyConfig;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：货物类型添加/编辑界面</p>
 * <p>Company </p>
 * <p>Copyright: Copyright (c) 2012 </p>
 * @version 1.0.0.0
 * @author sas 
 */
public class AddGoodsTypeActivity extends Activity implements OnClickListener{
	
	private final int DIALOG_ID = 2;
	//定义控件
	private ImageView revert;//返回"按钮"
	private TextView title;//标题
	private TextView codestr;//货物类型编号(字符串)
	private EditText code;//货物类型编号
	private TextView namestr;//货物类型名称(字符串)
	private EditText name;//站点名称
	private Button save_btn;//保存按钮
	private Button cancel_btn;//取消按钮
	
	private GoodsTypeBean goodsTypeBean;
	private String operate;//操作(添加或编辑)
	private String type_name = "";
	private String type_code = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.add_goodstype);
        //自定义窗口样式
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView)findViewById(R.id.common_title_revert);
		title = (TextView)findViewById(R.id.common_title_title);
		codestr = (TextView)findViewById(R.id.goodstype_add_codestr);
		code = (EditText)findViewById(R.id.goodstype_add_code);
		namestr = (TextView)findViewById(R.id.goodstype_add_namestr);
		name = (EditText)findViewById(R.id.goodstype_add_name);
		save_btn = (Button)findViewById(R.id.goodstype_add_save);
		cancel_btn = (Button)findViewById(R.id.goodstype_add_cancel);
		goodsTypeBean = (GoodsTypeBean)getIntent().getSerializableExtra("goodsTypeBean");
		System.out.println("goodsTypeBean==null--->"+goodsTypeBean==null);
		revert.setOnClickListener(this);
		codestr.setText(getText(R.string.typecodestr)+"：");
		namestr.setText(getText(R.string.typenamestr)+"：");
		save_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	if("".equals(MyConfig.getNOW_LOGINNAME())){
    		//Intent intent = new Intent(AccessRightActivity.this,LoginActivity.class);
			Intent intent = new Intent(AddGoodsTypeActivity.this,Main.class);
			Main.setChecked(R.id.main_tab_login);
    		startActivity(intent);
    		finish();
    	}
    	if(goodsTypeBean != null){
    		operate = "edit";
    		title.setText(getText(R.string.goodstypeMgr)+"——"+getText(R.string.edit));
    		code.setText(goodsTypeBean.getCode()+"");
    		name.setText(goodsTypeBean.getName());
    		type_code = goodsTypeBean.getCode()+"";
    		type_name = goodsTypeBean.getName();
    	}else{
    		operate = "add";
    		title.setText(getText(R.string.goodstypeMgr)+"——"+getText(R.string.add));
    	}
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goodstype_add_save:
			type_code = code.getText().toString();
			type_name = name.getText().toString();
			if("add".equals(operate)){
				if("".equals(type_code)){
					Toast.makeText(AddGoodsTypeActivity.this, getText(R.string.typecode_empty), Toast.LENGTH_SHORT).show();
				}else if("".equals(type_name)){
					Toast.makeText(AddGoodsTypeActivity.this, getText(R.string.typename_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}else if("edit".equals(operate)){
				System.out.println("edit");
				if("".equals(type_code)){
					Toast.makeText(AddGoodsTypeActivity.this, getText(R.string.typecode_empty), Toast.LENGTH_SHORT).show();
				}else if("".equals(type_name)){
					Toast.makeText(AddGoodsTypeActivity.this, getText(R.string.typename_empty), Toast.LENGTH_SHORT).show();
				}else{
					new OperateTask().execute(operate);
				}
			}
			break;
		case R.id.common_title_revert:
		case R.id.goodstype_add_cancel:
			//startActivity(new Intent(AddGoodsTypeActivity.this,GoodsTypeActivity.class));
			finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 异步操作——添加或编辑货物类型
	 * @author sas
	 *
	 */
	class OperateTask extends AsyncTask<String, String, String>{

		/**
		 * 后台执行添加或编辑货物类型操作
		 */
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean flag = false;
			GoodsTypeDBUtil goodsTypeDBUtil = new GoodsTypeDBUtil(AddGoodsTypeActivity.this);
			if("add".equals(params[0])){
				ContentValues values = new ContentValues();
				values.put("code", type_code);
				values.put("name", type_name);
				flag = goodsTypeDBUtil.addGoodsType(values);
			}else if("edit".equals(params[0])){
				int code = -1;
				try {
					code = Integer.parseInt(type_code);
					flag = goodsTypeDBUtil.updateGoodsType(code, type_name, goodsTypeBean.getId());
				} catch (Exception e) {
					// TODO: handle exception
					return false+"";
				}
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
				Intent intent = new Intent(AddGoodsTypeActivity.this,GoodsTypeActivity.class);
				startActivity(intent);
				finish();
			}else if("false".equals(result)){
				Toast.makeText(AddGoodsTypeActivity.this, getText(R.string.operate_failure), Toast.LENGTH_SHORT).show();
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
			//startActivity(new Intent(AddGoodsTypeActivity.this,GoodsTypeActivity.class));
			finish();
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(AddGoodsTypeActivity.this); 
			dialog.setMessage(getText(R.string.operating)); 
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog; 
		default:
			return null;
		}
	}
}