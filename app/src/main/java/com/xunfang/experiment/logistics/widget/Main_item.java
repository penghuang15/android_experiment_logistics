package com.xunfang.experiment.logistics.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunfang.experiment.logistics.R;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：自定义的显示控件</p>

 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author sas 
 */
public class Main_item extends LinearLayout{
	
	private ImageView imageview;//图片控件
	private TextView  textview;//文本控件  
  
    public Main_item(Context context) {  
        this(context, null);  
    }
  
    public Main_item(Context context,AttributeSet attrs) {  
        super(context,attrs);  
        // 导入布局  
        LayoutInflater.from(context).inflate(R.layout.main_item, this, true);
        //获得控件实例
        imageview = (ImageView) findViewById(R.id.main_item_imageview);  
        textview = (TextView) findViewById(R.id.main_item_textview);  
    }  
  
    public ImageView getImageview() {
		return imageview;
	}

	public void setImageview(ImageView imageview) {
		this.imageview = imageview;
	}

	public TextView getTextview() {
		return textview;
	}

	public void setTextview(TextView textview) {
		this.textview = textview;
	}
}
