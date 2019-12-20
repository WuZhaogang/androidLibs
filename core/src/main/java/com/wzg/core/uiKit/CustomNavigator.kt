package com.wzg.core.uiKit

import android.content.Context
import android.graphics.Color
import android.view.View
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/11/7
 * 修改备注:  说明本次修改内容
 */
class CustomNavigator : CommonNavigator {

    constructor(context: Context, mViewPager: IndexViewPager, mTitleDataList: Array<String>) : super(context) {
        adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return if (mTitleDataList == null) 0 else mTitleDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#aaaaaa")
                colorTransitionPagerTitleView.selectedColor = Color.parseColor("#64a1eb")
                colorTransitionPagerTitleView.textSize = 12f
                colorTransitionPagerTitleView.setText(mTitleDataList.get(index))
                colorTransitionPagerTitleView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        mViewPager.setCurrentItem(index)
                    }

                })
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
//                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(Color.parseColor("#64a1eb"))
                return indicator
            }
        }
    }


}