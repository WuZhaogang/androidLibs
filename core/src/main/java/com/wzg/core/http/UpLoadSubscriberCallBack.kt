package com.wzg.core.http


import com.orhanobut.logger.Logger
import com.wzg.core.base.BaseView
import com.wzg.core.base.UpLoadFileModel
import com.wzg.core.model.ErrorModel
import io.reactivex.observers.DisposableObserver

/**
 * 类描述:    描述该类的功能
 * 创建人:    wzg
 * 创建时间:  2017/11/28
 * 修改备注:  说明本次修改内容
 */
public abstract class UpLoadSubscriberCallBack<T>(baseView: BaseView) : DisposableObserver<T>() {

    private val SUCCESS = "SUCCESS"
    private var baseView: BaseView? = baseView

    override fun onNext(response: T) {
        baseView?.hideLoading()
        val baseModel = response as UpLoadFileModel
        onSuccess(response)
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        Logger.e(e.message!!)
        baseView?.hideLoading()
        val errorModel = ErrorModel(NET_ERROR_MSG)
        onError(errorModel)
        if (baseView != null) {
            baseView?.onCodeError(errorModel)
        }
    }

    protected abstract fun onSuccess(response: T?)

    protected abstract fun onError(errorModel: ErrorModel)

    companion object {
        var NET_ERROR_MSG = "网络异常,请稍后再试..."
    }


}
