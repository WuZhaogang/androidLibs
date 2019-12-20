package com.wzg.core.uiKit;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/5/28
 * 修改时间:  2018/5/28
 * 修改备注:  说明本次修改内容
 */
public class CustomRefreshLayout extends SmartRefreshLayout {
    private boolean isNeedAutoRefresh = true;

    public void setNeedAutoRefresh(boolean needAutoRefresh) {
        isNeedAutoRefresh = needAutoRefresh;
    }

    public CustomRefreshLayout(Context context) {
        super(context);
        setDefaultHeaderView();
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultHeaderView();
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultHeaderView();
    }

    private void setDefaultHeaderView() {
        setRefreshHeader(new MaterialHeader(getContext()));
        setEnableLoadMore(false);
    }

    @Override
    public SmartRefreshLayout setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
        if (isNeedAutoRefresh) {
            autoRefresh();
        }
        return this;
    }
}
