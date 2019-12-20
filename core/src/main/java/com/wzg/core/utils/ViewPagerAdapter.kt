package com.wzg.core.utils

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/9/25
 * 修改备注:  说明本次修改内容
 */
class ViewPagerAdapter : PagerAdapter() {
    var views: ArrayList<View>? = null

    override fun isViewFromObject(view: View, arg2: Any): Boolean {
        return view == (arg2)
    }

    override fun getCount(): Int {
        return views?.size!!
    }

    override fun finishUpdate(arg0: View) {}

    override fun instantiateItem(arg0: View, arg1: Int): Any {
        (arg0 as ViewPager).addView(views?.get(arg1), 0)
        return views?.get(arg1)!!
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(views?.get(arg1))
    }


}