package com.wzg.core

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.wzg.core.utils.SharedPreferencesUtil


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/2/1
 * 修改时间:  2018/2/1
 * 修改备注:  说明本次修改内容
 */
open class BaseApp : MultiDexApplication() {
    companion object {
        private var mInstance: BaseApp? = null//上下文

        fun getInstance(): Context? {
            if (mInstance == null) {
                mInstance = BaseApp()
            }
            return mInstance
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        SharedPreferencesUtil.init(this)
    }
}