package com.wzg.core.http


import com.wzg.core.base.BaseListModel
import com.wzg.core.base.BaseView
import com.wzg.core.model.ErrorModel
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException


/**
 * 类描述:    描述该类的功能
 * 创建人:    wzg
 * 创建时间:  2017/11/28
 * 修改备注:  说明本次修改内容
 */
abstract class SubscriberCallBack<T>(baseView: BaseView) : DisposableObserver<T>() {

    private val SUCCESS = "SUCCESS"
    private var baseView: BaseView? = baseView

    override fun onNext(response: T) {
        baseView?.hideLoading()
        try {
            val baseListModel = response as BaseListModel<Any>
            if (baseListModel.last) {
                baseView?.onNoMoreData()
            } else {
                baseView?.onGetListSuccess()
            }
            onSuccess(response)
        } catch (e: ClassCastException) {
//            e.printStackTrace()
            onSuccess(response)
        }
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        baseView?.hideLoading()
        e.printStackTrace()
        val errorModel = ErrorModel(NET_ERROR_MSG)

        if (e is HttpException) {
            val httpException = e
            val code = httpException.code()
            var msg = NET_ERROR_MSG
            errorModel.message = msg
            if (code == 403) {
                msg = "身份认证过期,请重新登录"
                errorModel.message = msg
                baseView?.onGetListSuccess()
                baseView?.onAccountExpired(errorModel)
            } else if (code == 400 || code == 500) {
                val body = httpException.response()?.errorBody()?.string()
                if (body != null) {
                    val tempErrorModel = ApiRetrofit.getInstance().gson.fromJson(body, ErrorModel::class.java) as ErrorModel
                    if (tempErrorModel.message != null) {
                        errorModel.message = tempErrorModel.message
                    }
                }
            }
            onError(errorModel)
        }
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
