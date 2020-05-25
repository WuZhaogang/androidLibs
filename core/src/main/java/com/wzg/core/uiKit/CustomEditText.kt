package com.wzg.core.uiKit

import android.content.Context
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/4/24
 * 修改时间:  2018/4/24
 * 修改备注:  说明本次修改内容
 */
class CustomEditText : AppCompatEditText {
    private var cursorPos: Int = 0
    //输入表情前EditText中的文本
    private var inputAfterText: String? = null
    //是否重置了EditText的内容
    private var resetText: Boolean = false

    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        initEditText()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initEditText()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initEditText()
    }

    // 初始化edittext 控件
    private fun initEditText() {
        filters = arrayOf(EmojiFilter())
    }

    class EmojiFilter : InputFilter {
        override fun filter(
            source: CharSequence, start: Int, end: Int,
            dest: Spanned, dstart: Int, dend: Int
        ): CharSequence {
            val buffer = StringBuffer()
            var i = start
            while (i < end) {
                val codePoint = source[i]
                if (!getIsEmoji(codePoint)) {
                    buffer.append(codePoint)
                } else {
                    i++
                }
                i++
            }
            return if (source is Spanned) {
                val sp = SpannableString(buffer)
                TextUtils.copySpansFrom(
                    source, start, end, null,
                    sp, 0
                )
                sp
            } else {
                buffer
            }
        }

        fun getIsEmoji(codePoint: Char): Boolean {
            return if (codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA
                || codePoint.toInt() == 0xD
                || codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0x29
                || codePoint.toInt() >= 0x2A && codePoint.toInt() <= 0x3A
                || codePoint.toInt() >= 0x40 && codePoint.toInt() <= 0xA8
                || codePoint.toInt() >= 0xAF && codePoint.toInt() <= 0x203B
                || codePoint.toInt() >= 0x203D && codePoint.toInt() <= 0x2048
                || codePoint.toInt() >= 0x2050 && codePoint.toInt() <= 0x20e2
                || codePoint.toInt() >= 0x20e4 && codePoint.toInt() <= 0x2100
                || codePoint.toInt() >= 0x21AF && codePoint.toInt() <= 0x2300
                || codePoint.toInt() >= 0x23FF && codePoint.toInt() <= 0X24C1
                || codePoint.toInt() >= 0X24C3 && codePoint.toInt() <= 0x2500
                || codePoint.toInt() >= 0x2800 && codePoint.toInt() <= 0x2933
                || codePoint.toInt() >= 0x2936 && codePoint.toInt() <= 0x2AFF
                || codePoint.toInt() >= 0x2C00 && codePoint.toInt() <= 0x3029
                || codePoint.toInt() >= 0x3031 && codePoint.toInt() <= 0x303C
                || codePoint.toInt() >= 0x303E && codePoint.toInt() <= 0x3296
                || codePoint.toInt() >= 0x32A0 && codePoint.toInt() <= 0xD7FF
                || codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFE0E
                || codePoint.toInt() >= 0xFE10 && codePoint.toInt() <= 0xFFFD
                || codePoint.toInt() >= 0x10000 && codePoint.toInt() <= 0x10FFFF
            ) {
                false
            } else true
        }
    }

}
