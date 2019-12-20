package com.wzg.core.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 类描述:    单个model的基类
 * 创建人:    wzg
 * 创建时间:  2018/3/15
 * 修改时间:  2018/3/15
 * 修改备注:  说明本次修改内容
 */
open class BaseModel<T> : Serializable {
    var status: String? = ""

    var msg: String? = ""

    @SerializedName("object")
    var data: T? = null

}