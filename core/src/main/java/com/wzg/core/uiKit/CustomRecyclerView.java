package com.wzg.core.uiKit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.wzg.core.R;
import com.wzg.core.base.BaseRecyclerAdapter;

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/4/24
 * 修改时间:  2018/4/24
 * 修改备注:  说明本次修改内容
 */
public class CustomRecyclerView extends RecyclerView {
    boolean isNestedEnable = true;
    boolean isNestedEmptyView = false;

    public void setNestedEmptyView(boolean nestedEmptyView) {
        isNestedEmptyView = nestedEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (isNestedEmptyView) {
            ((BaseRecyclerAdapter) adapter).bindToRecyclerView(this);
        }
    }

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isNestedEnable() {
        return isNestedEnable;
    }

    public void setNestedEnable(boolean nestedEnable) {
        isNestedEnable = nestedEnable;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return isNestedEnable && super.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        if (isNestedEnable) {
            super.stopNestedScroll(type);
        }
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return isNestedEnable && super.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return isNestedEnable && super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return isNestedEnable && super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    public void addItemDecoration10dp(@NonNull Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_divider_ver);
        if (drawable != null) {
            itemDecoration.setDrawable(drawable);
            addItemDecoration(itemDecoration);
        }
    }

    public void addItemDecoration10dpNormal(@NonNull Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_divider_ver_nromal);
        if (drawable != null) {
            itemDecoration.setDrawable(drawable);
            addItemDecoration(itemDecoration);
        }
    }

    public void addItemDecorationPaddingLeft15(@NonNull Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_divider_ver_15);
        if (drawable != null) {
            itemDecoration.setDrawable(drawable);
            addItemDecoration(itemDecoration);
        }
    }


    public void addItemDecoration1dp(@NonNull Context context) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_divider_ver_1dp);
        if (drawable != null) {
            itemDecoration.setDrawable(drawable);
            addItemDecoration(itemDecoration);
        }
    }

}
