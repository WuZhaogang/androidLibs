package com.wzg.core.uiKit

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *  @author  bulus
@date  2018/7/31
 */
class IconTextView : AppCompatTextView {
    constructor(context: Context?) : super(context) {
        setTextType(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setTextType(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setTextType(context)
    }

    private fun setTextType(context: Context?) {
        var tf = Typeface.createFromAsset(context?.assets, "iconfont.ttf")
        setTypeface(tf);
    }
}
