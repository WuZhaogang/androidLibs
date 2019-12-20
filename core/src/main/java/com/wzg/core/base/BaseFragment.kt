package com.wzg.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.wzg.core.Constant
import com.wzg.core.model.ErrorModel
import com.wzg.core.model.LoginResponseModel
import com.wzg.core.model.UserModel
import com.wzg.core.utils.SharedPreferencesUtil

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/2/1
 * 修改时间:  2018/2/1
 * 修改备注:  说明本次修改内容
 */
abstract class BaseFragment<P : BasePresenter<*>> : BaseLazyFragment(), BaseView {
    var rootView: View? = null

    protected var mPresenter: P? = null

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): P?

    protected var mContext: Context? = null

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = layoutInflater.inflate(getLayoutId(), container, false)
            isViewCreated = true
            initListener()
        } else {
            var viewGroup: ViewGroup? = rootView?.parent as ViewGroup?
            viewGroup?.removeView(rootView)
        }
        mPresenter = createPresenter()
        return rootView
    }

    protected fun initListener() {}

    protected fun getResString(id: Int): String {
        return resources.getString(id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    protected fun getResColor(id: Int): Int {
        return ContextCompat.getColor(mContext!!, id)
    }

    override fun showLoading() {
        (activity as BaseActivity<*>).showLoading()
    }

    override fun hideLoading() {
        (activity as BaseActivity<*>).hideLoading()
    }

    override fun showMsg(msg: String?) {
        (activity as BaseActivity<*>).showMsg(msg)
    }

    override fun onCodeError(errorModel: ErrorModel?) {
        (activity as BaseActivity<*>).onCodeError(errorModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
    }


    /**
     * 封装提示类Dialog
     */
    public fun showAlertDialog(
        title: String,
        content: String,
        positiveCallBack: MaterialDialog.SingleButtonCallback
    ) {
        (activity as BaseActivity<*>).showAlertDialog(title, content, positiveCallBack)
    }

    public fun showAlertDialog(
        content: String,
        positiveCallBack: MaterialDialog.SingleButtonCallback
    ) {
        showAlertDialog("提示", content, positiveCallBack)
    }

    /**
     * 保存用户信息
     */
    fun saveUserModel(userModel: UserModel) {
        SharedPreferencesUtil.putPreferences(Constant.SP_USER_KEY, userModel)
    }

    /**
     * 获取用户信息
     */
    fun getUserModel(): LoginResponseModel? {
        return (activity as BaseActivity<*>).getUserModel()
    }

    /**
     * 清除用户数据
     */
    fun clearUserModel() {
        SharedPreferencesUtil.putPreferences(Constant.SP_USER_KEY, null)
    }

    override fun onGetListSuccess() {
    }

    override fun onNoMoreData() {
    }

    override fun onAccountExpired(errorModel: ErrorModel) {

    }
}