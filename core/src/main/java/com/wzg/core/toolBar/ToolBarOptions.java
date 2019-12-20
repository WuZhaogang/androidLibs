package com.wzg.core.toolBar;


import com.wzg.core.R;

/**
 * 类描述:    ToolBar选项
 * 创建人:    wzg
 * 创建时间:  2016/12/7
 * 创建人:    wzg
 * 修改时间:  2016/12/7 15:49
 * 修改备注:  说明本次修改内容
 */
public class ToolBarOptions {
    public ToolBarOptions() {
    }

    public ToolBarOptions(String title) {
        this.titleString = title;
    }

    /**
     * toolbar的title资源id
     */
    public int titleId = 0;
    /**
     * toolbar的title
     */
    public String titleString = "";
    /**
     * toolbar的logo资源id
     */
    public int logoId;
    /**
     * toolbar的返回按钮资源id，默认开启的资源nim_actionbar_dark_back_icon
     */
    public int navigateId = R.mipmap.ic_back;
    /**
     * toolbar的返回按钮，默认开启
     */
    public boolean isNeedNavigate = true;

    public int bgColor = R.color.c2e313d;

    public String titleRightString;

    public int titleTextColor;

    public int rightTextColor;
}