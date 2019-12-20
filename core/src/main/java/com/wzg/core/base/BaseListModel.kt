package com.wzg.core.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 类描述:    列表的基类
 * 创建人:    wzg
 * 创建时间:  2018/3/15
 * 修改时间:  2018/3/15
 * 修改备注:  说明本次修改内容
 */
open class BaseListModel<T> : Serializable {
    var totalPages: Int = 0
    @SerializedName("last")
    var last: Boolean = false
    var totalElements: Int = 0
    var size: Int = 0
    var pageNumber: Int = 0
    var sort: Any? = null
    var first: Boolean = false
    var numberOfElements: Int = 0
    var content: ArrayList<T>? = null

}