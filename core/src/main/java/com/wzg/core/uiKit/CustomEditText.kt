package com.wzg.core.uiKit

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern


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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (!resetText) {
//                    cursorPos = selectionEnd
//                    inputAfterText = s.toString()
//                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (!resetText) {
//                    if (count >= 2) {
//                        val input = s.subSequence(cursorPos, cursorPos + count)
//                        if (containsEmoji(input.toString())) {
//                            resetText = true
//                            setText(inputAfterText)
//                            val text = text
//                            if (text is Spannable) {
//                                val spanText = text as Spannable
//                                Selection.setSelection(spanText, text.length)
//                            }
//                        }
//                    }
//                } else {
//                    resetText = false
//                }
                if (count - before >= 1) {
                    val input = s.subSequence(start + before, start + count);
                    if (isEmoji(input.toString())) {
                        (s as SpannableStringBuilder).delete(start + before, start + count);
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
//    fun containsEmoji(source: String): Boolean {
//        val len = source.length
//        for (i in 0 until len) {
//            val codePoint = source[i]
//            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
//                return true
//            }
//        }
//        return false
//    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
//    private fun isEmojiCharacter(codePoint: Char): Boolean {
//        return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA ||
//                codePoint.toInt() == 0xD || codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0xD7FF ||
//                codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFFFD ||
//                codePoint.toInt() >= 0x10000 && codePoint.toInt() <= 0x10FFFF
//    }
    /**
     * 正则判断emoji表情
     * @param input
     * @return
     */
    private fun isEmoji(input: String): Boolean {
        val p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff]" +
                "|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]")
        var m = p.matcher(input)
        return m.find()
    }

}
