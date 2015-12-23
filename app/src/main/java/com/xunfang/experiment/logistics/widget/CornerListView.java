package com.xunfang.experiment.logistics.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xunfang.experiment.logistics.R;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：圆角ListView</p>

 * <p>Copyright: Copyright (c) 2012</p>
 * @version 1.0.0.0
 * @author sas 
 */
public class CornerListView extends ListView {
    public CornerListView(Context context) {
        super(context);
    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION)
                        break;                 
                else{
                	if(itemnum == 0){//第一项
                        if(itemnum == (getAdapter().getCount()-1)){//只有一项                                    
                            setSelector(R.drawable.app_list_corner_round);
                        }else{//列表不止一项
                            setSelector(R.drawable.app_list_corner_round_top);
                        }
	                }else if(itemnum == (getAdapter().getCount()-1)){//最后一项
	                        setSelector(R.drawable.app_list_corner_round_bottom);
	                }else{//中间项                            
	                    setSelector(R.drawable.app_list_corner_shape);
	                }
                }
                break;
        case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}