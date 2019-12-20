package com.wzg.core.base

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wzg.core.R
import com.wzg.core.model.ErrorModel


/**
 * @author wzg
 * @createTime 2017/3/13
 * @describe 描述
 * @note 备注
 */

abstract class BaseDialogFragment<T : BasePresenter<*,*>> : DialogFragment(), BaseView {
    var rootView: View? = null

    protected var mPresenter: T? = null

    abstract fun getContentViewId(savedInstanceState: Bundle?): Int

    abstract fun initView()


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog)
        mPresenter = createPresenter()
        isCancelable = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getContentViewId(savedInstanceState), container)
        initView()
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    protected fun hideSoftKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity!!.window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity!!.currentFocus != null)
                inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * toast
     *
     * @param msg
     */
    override fun showMsg(msg: String?) {
        //        if (toast == null) {
        //            toast = Toast.makeText(App.sApplicationContext, msg, Toast.LENGTH_SHORT);
        //        }
        //        toast.setText(msg);
        //        toast.show();
        (activity as BaseActivity<*>).showMsg(msg)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    /**
     * 错误处理
     *
     * @param errorModel
     */
    override fun onCodeError(errorModel: ErrorModel?) {
        showMsg(errorModel!!.message)
    }

    fun show(manager: FragmentManager) {
        try {
            super.show(manager, this.javaClass.simpleName)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        dialog?.window!!.setLayout(dm.widthPixels, dialog?.window!!.attributes.height)
    }

}
