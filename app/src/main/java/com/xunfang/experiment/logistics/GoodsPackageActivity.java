package com.xunfang.experiment.logistics;

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
import android.os.Message;
import android.util.Log;
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

import com.xunfang.experiment.logistics.bean.GoodsInfoBean;
import com.xunfang.experiment.logistics.bean.StationBean;
import com.xunfang.experiment.logistics.db.CarduserDBUtil;
import com.xunfang.experiment.logistics.db.GoodsInfoDBUtil;
import com.xunfang.experiment.logistics.db.StationDBUtil;
import com.xunfang.experiment.logistics.service.ReaderDataProcess;
import com.xunfang.experiment.logistics.service.ReaderOperating;
import com.xunfang.experiment.logistics.so.Linuxc;
import com.xunfang.experiment.logistics.util.MyConfig;
import com.xunfang.experiment.logistics.util.ReaderConfig;
import com.xunfang.experiment.logistics.util.Tools;

/**
 * <p>
 * Title：物流管理系统
 * </p>
 * <p>
 * Description：物品打包界面
 * </p>
 * <p>

 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * 
 * @version 1.0.0.0
 * @author sas
 * 
 */
public class GoodsPackageActivity extends Activity implements OnClickListener {

	private final int DIALOG_ID = 10;
	// 更新界面的Handler
	private ImageView revert;// 返回"按钮"
	private TextView title;// 标题
	private TextView cardid8bitstr;// 8位原始卡号(字符串)
	private TextView ordernumstr;// 订单号(字符串)
	private TextView goodsstr;// 添加货物(字符串)
	private TextView receiveaddr;// 收件地址(字符串)
	private Button readCard_btn;// 读卡按钮
	private EditText cardid8bit;// 原始卡号
	private EditText ordernum;// 订单号
	private EditText goodsinfo;// 物品信息
	private Button goodsadd_btn;// 物品选择按钮
	private Button receiveaddr_btn;// 收件地址选择按钮
	private Button operate_save;// 保存按钮
	private Button operate_cancel;// 取消按钮

	private Map<Integer, GoodsInfoBean> selectedGoods = new HashMap<Integer, GoodsInfoBean>();// 选择的物品信息
	private StationBean selectedStation;// 选择的收件地址
	private List<GoodsInfoBean> goodsList;
	private List<StationBean> stationList;

	private String cardid8bitStr = "";// 原始卡号
	private int goods_receiveaddr = -1;
	private String operate = "addgoods";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗口布局
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.goods_package);
		// 自定义窗口样式
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.common_title);
		revert = (ImageView) findViewById(R.id.common_title_revert);
		title = (TextView) findViewById(R.id.common_title_title);
		title.setText(R.string.packageMgr);
		cardid8bitstr = (TextView) findViewById(R.id.goods_package_cardid8bitstr);
		cardid8bit = (EditText) findViewById(R.id.goods_package_cardid8bit);
		readCard_btn = (Button) findViewById(R.id.goods_package_readcard);
		ordernumstr = (TextView) findViewById(R.id.goods_package_ordernumstr);
		ordernum = (EditText) findViewById(R.id.goods_package_ordernum);
		goodsstr = (TextView) findViewById(R.id.goods_package_goodsstr);
		goodsadd_btn = (Button) findViewById(R.id.goods_package_goods);
		receiveaddr = (TextView) findViewById(R.id.goods_package_receiveaddrstr);
		receiveaddr_btn = (Button) findViewById(R.id.goods_package_receiveaddr);
		operate_save = (Button) findViewById(R.id.goods_package_operate_save);
		operate_cancel = (Button) findViewById(R.id.goods_package_operate_cancel);
		cardid8bitstr.setText(getText(R.string.cardid8bitstr) + "：");
		ordernumstr.setText(getText(R.string.ordernumstr) + "：");
		goodsstr.setText(getText(R.string.goodsinfostr) + "：");
		receiveaddr.setText(getText(R.string.receiveaddrstr) + "：");
		revert.setOnClickListener(this);
		readCard_btn.setOnClickListener(this);
		receiveaddr_btn.setOnClickListener(this);
		goodsadd_btn.setOnClickListener(this);
		operate_save.setOnClickListener(this);
		operate_cancel.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ("".equals(MyConfig.getNOW_LOGINNAME())) {
			Intent intent = new Intent(GoodsPackageActivity.this, Main.class);
			Main.setChecked(R.id.main_tab_login);
			startActivity(intent);
			finish();
		} else {
			ordernum.setText(Tools.createOrderNum());
			ReaderConfig.setUART_STATE(Linuxc.openUart(3));// 打开串口3
			System.out.println(ReaderConfig.getUART_STATE());
		}
	}
	public static void ParseMessage(Bundle message) {
		Message msg = new Message();
		msg.setData(message);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ImageView || view instanceof Button) {
			switch (view.getId()) {
			case R.id.goods_package_goods:
				operate = "addgoods";
				new OperateTask().execute(operate);
				break;
			case R.id.goods_package_receiveaddr:
				operate = "receiveaddr";
				new OperateTask().execute(operate);
				break;
			case R.id.goods_package_readcard:
				operate = "readcard";
				new OperateTask().execute(operate);
				break;
			case R.id.goods_package_operate_save:
				cardid8bitStr = cardid8bit.getText().toString();
				if (cardid8bitStr == null || cardid8bitStr.length() != 8) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.cardid8bit_empty),
							Toast.LENGTH_SHORT).show();
				} else if (goods_receiveaddr == -1) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.receiveaddr_empty),
							Toast.LENGTH_SHORT).show();
				} else if (selectedGoods.size() == 0) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.goods_empty), Toast.LENGTH_SHORT)
							.show();
				} else {
					operate = "package";
					new OperateTask().execute(operate);
				}
				break;
			case R.id.goods_package_operate_cancel:
			case R.id.common_title_revert:
				finish();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 自定义站点信息适配器
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
	 * 自定义物品信息适配器
	 * 
	 * @author sas
	 * 
	 */
	class GoodsAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private List<GoodsInfoBean> list;

		public GoodsAdapter(Context content, List<GoodsInfoBean> list) {
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
		public GoodsInfoBean getItem(int position) {
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
			GoodsInfoBean goods = list.get(position);
			holder.name.setText(goods.getName());
			return convertView;
		}

		final class ViewHolder {
			TextView id;
			TextView name;
		}
	}

	/**
	 * 异步操作类
	 * 
	 * @author sas
	 * 
	 */
	class OperateTask extends AsyncTask<String, String, String> {

		/**
		 * 后台执行耗时操作
		 */
		@Override
		protected String doInBackground(String... params) {
			if ("addgoods".equals(params[0])) {
				GoodsInfoDBUtil goodsInfoDBUtil = new GoodsInfoDBUtil(
						GoodsPackageActivity.this);
				goodsList = goodsInfoDBUtil.getGoodsList();
				if (goodsList == null) {
					return false + "";
				} else {
					return true + "";
				}
			} else if ("receiveaddr".equals(params[0])) {
				StationDBUtil stationDBUtil = new StationDBUtil(
						GoodsPackageActivity.this);
				stationList = stationDBUtil.getStationList();
				if (stationList == null) {
					return false + "";
				} else {
					return true + "";
				}
			} else if ("readcard".equals(params[0])) {
				// 得到8byte的原始卡号
				byte[] cardData = null;
				if (ReaderConfig.getUART_STATE() > 0) {
					cardData = ReaderOperating.searchCard();
					// 处理2.4G二次寻卡时返回错误数据
					if (cardData != null && cardData[1] == 0x30) {
						cardData = ReaderOperating.searchCard();// 寻卡
					} else if (cardData != null && cardData[4] == 0x04) {
						cardData = ReaderOperating.searchCard();// 寻卡
					}
				} else {
					System.out.println("串口3打开失败");
				}
				if (cardData != null && cardData[4] != 0x05
						&& cardData[cardData.length - 3] == 0x00) { // 判断是否寻到卡
					byte cardType = (byte) 0x01;
					System.out.println("cardData[4]：" + cardData[4]);
					System.out.println("cardData[4] == cardType："
							+ (cardData[4] == cardType));
					if (cardData[4] == cardType) {// 判断选择的卡片类型与读到的卡片类型是否一致
						String cardid = ReaderDataProcess
								.getFristCardID(cardData);
						Log.d("readCardTag", "readCard---one");
						Log.d("Business", cardid);
						CarduserDBUtil carduserDBUtil = new CarduserDBUtil(
								GoodsPackageActivity.this);
						int flag = carduserDBUtil.isCardExist(cardid, null);
						cardid8bitStr = cardid;
						System.out.println("订单号：" + cardid);
						System.out.println("flag：" + flag);
						if (flag == -1) {
							return true + "";
						} else {
							return flag + "";
						}

					} else {
						return false + "";
					}
				} else {// 寻卡失败
					Log.d("readCardTag", "readCard---two");
					Log.d("Business", "读单号失败");
					return false + "";
				}
			} else if ("package".equals(params[0])) {
				CarduserDBUtil carduserDBUtil = new CarduserDBUtil(
						GoodsPackageActivity.this);
				String maxCardsn = carduserDBUtil.getMaxCardsn();
				if (maxCardsn == null)
					return "-3";
				int max_cardsn = Integer.parseInt(maxCardsn) + 1;
				String cardsn = Tools.addLeft0Str(max_cardsn + "", 8);
				byte[] cardData = null;
				if (ReaderConfig.getUART_STATE() > 0) {// 判断串口是否打开
					cardData = ReaderOperating.searchCard();
					// 处理2.4G二次寻卡时返回错误数据
					if (cardData != null && cardData[1] == 0x30) {
						cardData = ReaderOperating.searchCard();// 寻卡
					} else if (cardData != null && cardData[4] == 0x04) {
						cardData = ReaderOperating.searchCard();// 寻卡
					}
					if (cardData != null) {
						for (int i = 0; i < cardData.length; i++) {
						}
					}
				} else {
					System.out.println("串口3打开失败");
					return "-6";
				}
				boolean wflag = false;
				if (cardData != null && cardData[4] == 0x01)
					wflag = ReaderOperating.writeCard(cardData, cardsn, "0");// 写卡
				Log.d("openCardWriteTag", wflag + "");
				if (wflag) {// 写卡成功
					if (ReaderConfig.getUART_STATE() > 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						cardData = ReaderOperating.searchCard();
						// 处理2.4G二次寻卡时返回错误数据
						if (cardData != null && cardData[1] == 0x30) {
							cardData = ReaderOperating.searchCard();// 寻卡
						} else if (cardData != null && cardData[4] == 0x04) {
							cardData = ReaderOperating.searchCard();// 寻卡
						}
					} else {
						return "-6";
					}
					if (cardData != null && cardData[4] == 0x01) {
						int flag = carduserDBUtil.goodsPackage(cardsn,
								cardid8bitStr, MyConfig.CARDTYPE_LH, ordernum
										.getText().toString(),
								goods_receiveaddr, selectedGoods);
						return flag + "";
					} else
						return "-7";
				} else {// 写卡失败
					return "-5";
				}
			} else {
				return false + "";
			}
		}

		/**
		 * 执行doInBackground前被调用
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("操作--->" + operate);
			showDialog(DIALOG_ID);
		}

		/**
		 * 执行doInBackground后被调用
		 */
		@Override
		protected void onPostExecute(String result) {
			dismissDialog(DIALOG_ID);
			if ("addgoods".equals(operate)) {
				if ("true".equals(result)) {
					// 通过AlertDialog列出货物信息列表供用户选择
					new AlertDialog.Builder(GoodsPackageActivity.this)
							.setTitle(getText(R.string.select_goods))// 设置标题
							.setNegativeButton(getText(R.string.cancel), null)
							.setAdapter(
									new GoodsAdapter(GoodsPackageActivity.this,
											goodsList),
									new DialogInterface.OnClickListener() {
										// 通过适配器给AlertDialog设置内容，并设置监听器
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 获得选中的物品，并设置选中的物品
											GoodsInfoBean goods = goodsList
													.get(which);
											goods.setNum(1);
											selectedGoods.clear();
											selectedGoods.put(goods.getId(),
													goods);
											goodsadd_btn.setText(goods
													.getName());
										}
									}).create().show();
				} else if ("false".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.operate_failure),
							Toast.LENGTH_SHORT).show();
				}
			} else if ("receiveaddr".equals(operate)) {
				if ("true".equals(result)) {
					// 通过AlertDialog列出站点信息列表供用户选择
					new AlertDialog.Builder(GoodsPackageActivity.this)
							.setTitle(getText(R.string.select_station))// 设置标题
							.setNegativeButton(getText(R.string.cancel), null)
							.setAdapter(
									new StationAdapter(
											GoodsPackageActivity.this,
											stationList),
									new DialogInterface.OnClickListener() {
										// 通过适配器给AlertDialog设置内容，并设置监听器
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 获得选中的站点，并设置选中的站点
											selectedStation = stationList
													.get(which);
											receiveaddr_btn
													.setText(selectedStation
															.getName());
											goods_receiveaddr = selectedStation
													.getId();
										}
									}).create().show();
				} else if ("false".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.operate_failure),
							Toast.LENGTH_SHORT).show();
				}
			} else if ("readcard".equals(operate)) {
				if ("true".equals(result)) {
					operate_save.setClickable(true);
					cardid8bit.setText(cardid8bitStr);
				} else if ("1".equals(result)) {
					operate_save.setClickable(false);
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.card_using), Toast.LENGTH_SHORT)
							.show();
				} else if ("false".equals(result)) {
					operate_save.setClickable(false);
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.readcard_failure),
							Toast.LENGTH_SHORT).show();
				}
			} else if ("package".equals(operate)) {
				if ("0".equals(result)) {
					Intent intent = new Intent(GoodsPackageActivity.this,
							GoodsFollowActivity.class);
					intent.putExtra("ordernumber", ordernum.getText()
							.toString());
					startActivity(intent);
					finish();
				} else if ("-1".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.card_using), Toast.LENGTH_SHORT)
							.show();
				} else if ("-2".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.card_lost), Toast.LENGTH_SHORT)
							.show();
				} else if ("-3".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.database_err), Toast.LENGTH_SHORT)
							.show();
				} else if ("-5".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.write_card_err),
							Toast.LENGTH_SHORT).show();
				} else if ("-6".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.uart_open_err), Toast.LENGTH_SHORT)
							.show();
				} else if ("-7".equals(result)) {
					Toast.makeText(GoodsPackageActivity.this,
							getText(R.string.read_card_err), Toast.LENGTH_SHORT)
							.show();
				}
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
			finish();
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID:
			ProgressDialog dialog = new ProgressDialog(
					GoodsPackageActivity.this);
			dialog.setMessage(getText(R.string.operating));
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			return dialog;
		default:
			return null;
		}
	}
}
