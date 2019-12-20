package com.wzg.core.uiKit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by wzg on 15/6/3.
 * 取消viewPager滑动效果
 */
public class IndexViewPager extends ViewPager {

    private boolean isCanScroll = true;


    public IndexViewPager(Context context) {
        super(context);
//判断用户在进行滑动操作的最小距离
    }

    public IndexViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //判断用户在进行滑动操作的最小距离
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isCanScroll) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}