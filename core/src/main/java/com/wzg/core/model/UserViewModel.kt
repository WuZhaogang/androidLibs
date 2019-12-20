package com.wzg.core.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/5/30
 * 修改时间:  2018/5/30
 * 修改备注:  说明本次修改内容
 */
class UserViewModel : ViewModel() {
    val multLiveData = MutableLiveData<UserModel>()

}
