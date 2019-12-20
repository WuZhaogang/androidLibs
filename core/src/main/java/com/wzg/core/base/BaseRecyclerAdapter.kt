package com.wzg.core.base

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wzg.core.BaseApp
import com.wzg.core.R
import com.wzg.core.utils.GlideUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/2/1
 * 修改时间:  2018/2/1
 * 修改备注:  说明本次修改内容
 */
abstract class BaseRecyclerAdapter<T> constructor(layoutResId: Int, data: MutableList<T>) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data) {

    override fun bindToRecyclerView(recyclerView: RecyclerView?) {
        try {
            super.bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.lay_empty)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disPlayImage(imageView: ImageView, url: String) {
        GlideUtils.instance.disPlayDefaultCenterCrop(mContext, url, imageView)
    }

    fun disPlayImageSize(imageView: ImageView, url: String, width: Int, height: Int) {
        GlideUtils.instance.disPlayImageSize(mContext, url, imageView, width, height)
    }

    fun addDefaultHeader() {
        val header = LayoutInflater.from(BaseApp.getInstance()).inflate(R.layout.common_10dp_line, null)
        addHeaderView(header)
    }

    fun getDateString(timestamp: Long): String {
        //时间格式,HH是24小时制，hh是AM PM12小时制
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
//比如timestamp=1449210225945；
        val date_temp = java.lang.Long.valueOf(timestamp)
        val date_string = sdf.format(Date(date_temp))
        return date_string
//至于取10位或取13位，date_temp*1000L就是这种截取作用。如果是和服务器传值的，就和后台商量好就可以了
    }

}