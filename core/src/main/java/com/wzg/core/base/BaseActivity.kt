package com.wzg.core.base

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.jaeger.library.StatusBarUtil
import com.wzg.core.BaseApp
import com.wzg.core.Constant.Companion.SP_USER_KEY
import com.wzg.core.R
import com.wzg.core.model.ErrorModel
import com.wzg.core.model.LoginResponseModel
import com.wzg.core.toolBar.ToolBarOptions
import com.wzg.core.uiKit.StatusBarUtils
import com.wzg.core.utils.SharedPreferencesUtil
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/1/31
 * 修改时间:  2018/1/31
 * 修改备注:  说明本次修改内容
 */
abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity(), BaseView, EasyPermissions.PermissionCallbacks {

    protected var mPresenter: P? = null

    protected var mMaterialDialog: MaterialDialog? = null

    protected var loadingView: View? = null

    protected var toolbar: Toolbar? = null

    private var loadingDialog: ProgressDialog? = null

    companion object {
        protected var mToast: Toast? = null
        const val RC_PERMISSION_CODE: Int = 100
    }

    /**
     * 公用的Toast
     */
    private fun showToast(msg: String?) {
        if (mToast != null) {
            mToast?.setText(msg)
            mToast?.duration = Toast.LENGTH_SHORT
        } else {
            mToast = Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_SHORT)
        }
        mToast?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId(savedInstanceState))
        setStatusBarWhite()
        mPresenter = createPresenter()
        initView()
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): P?

    @LayoutRes
    abstract fun getLayoutId(savedInstanceState: Bundle?): Int

    abstract fun initView()

    /**
     * 公用设置ToolBar
     */
    fun setToolBar(options: ToolBarOptions) {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        if (options.titleId != 0) {
            val textView: TextView = toolbar?.findViewById(R.id.tvTitle) as TextView
            textView.setText(options.titleId)

            if (options.titleTextColor != 0) {
                textView.setTextColor(options.titleTextColor)
            }
        }
        if (options.bgColor != 0) {
            toolbar?.setBackgroundColor(resources.getColor(options.bgColor))
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            val textView: TextView = toolbar?.findViewById(R.id.tvTitle) as TextView
            textView.text = options.titleString
            if (options.titleTextColor != 0) {
                textView.setTextColor(options.titleTextColor)
            }
        }
        if (!TextUtils.isEmpty(options.titleRightString)) {
            val textView: TextView = toolbar?.findViewById(R.id.tvRight) as TextView
            textView.text = options.titleRightString
            textView.visibility = View.VISIBLE
            if (options.rightTextColor != 0) {
                textView.setTextColor(options.rightTextColor)
            }
        }
        if (options.logoId != 0) {
            toolbar?.setLogo(options.logoId)
        }
        if (options.isNeedNavigate) {
            val imageView: ImageView = toolbar?.findViewById(R.id.imgBack) as ImageView
            imageView.setImageResource(options.navigateId)
            imageView.setOnClickListener(View.OnClickListener {
                hindInput()
                onBackPressed()
            })
        } else {
            val imageView = toolbar?.findViewById(R.id.imgBack) as ImageView
            imageView.visibility = View.GONE
        }
        setSupportActionBar(toolbar)
    }

    /**
     * 隐藏键盘
     */
    protected fun hindInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 设置状态栏颜色
     */
    protected fun setStatusBarWhite() {
        val type = StatusBarUtils.StatusBarLightMode(this)
        if (type == 0) {
            StatusBarUtil.setColor(this, getResColor(R.color.c2e313d), 100)
            StatusBarUtil.setTranslucentForImageView(this, null)
        } else {
            StatusBarUtil.setColor(this, getResColor(R.color.c2e313d), 0)
            StatusBarUtils.StatusBarDarkMode(this, type)
        }
    }

    /**
     * 显示加载框
     */
    override fun showLoading() {
        if (loadingDialog != null && loadingDialog?.isShowing!!) {
            return
        }
        if (loadingView == null) {
            loadingView = LayoutInflater.from(this).inflate(R.layout.lay_loading, null)
        }
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this, R.style.loadingDialog)
        }
        loadingDialog?.setCanceledOnTouchOutside(false)
        loadingDialog?.show()
        loadingDialog?.setContentView(loadingView!!)
    }

    /**
     * 加载消失
     */
    override fun hideLoading() {
        loadingDialog?.dismiss()
    }

    /**
     * 封装提示类Dialog
     */
    public fun showAlertDialog(
        title: String,
        content: String,
        positiveCallBack: MaterialDialog.SingleButtonCallback
    ) {
        mMaterialDialog = MaterialDialog.Builder(this)
            .title(title)
            .content(content)
            .positiveText(getResString(R.string.confirm))
            .negativeText(getResString(R.string.cancel))
            .onPositive(positiveCallBack)
            .build()
        mMaterialDialog?.show()
    }

    public fun showAlertDialog(
        content: String,
        positiveCallBack: MaterialDialog.SingleButtonCallback
    ) {
        showAlertDialog("提示", content, positiveCallBack)
    }

    /**
     * 强制弹框
     */
    fun showAlertDialogNoCancel(
        content: String,
        positiveCallBack: MaterialDialog.SingleButtonCallback
    ) {
        mMaterialDialog = MaterialDialog.Builder(this)
            .title("提示")
            .content(content)
            .cancelable(false)
            .positiveText(getResString(R.string.confirm))
            .onPositive(positiveCallBack)
            .build()
        mMaterialDialog?.show()
    }

    override fun showMsg(msg: String?) {
        showToast(msg)
    }

    override fun onCodeError(errorModel: ErrorModel?) {
        showToast(errorModel?.message)
    }

    protected fun getResString(@StringRes id: Int): String {
        return resources.getString(id)
    }

    protected fun getResColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(this, id)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoading()
        mPresenter?.detachView()
    }

    /**
     * 保存用户信息
     */
    fun saveUserModel(userModel: LoginResponseModel) {
        SharedPreferencesUtil.putPreferences(SP_USER_KEY, userModel)
    }

    /**
     * 获取用户信息
     */
    fun getUserModel(): LoginResponseModel? {
        return SharedPreferencesUtil.getPreferences(SP_USER_KEY)
    }

    /**
     * 清除用户数据
     */
    fun clearUserModel() {
        SharedPreferencesUtil.putPreferences(SP_USER_KEY, null)
    }


    override fun onGetListSuccess() {
    }

    override fun onNoMoreData() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        // Some permissions have been granted
        // ...
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        // Some permissions have been denied
        // ...
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    //当身份过期
    override fun onAccountExpired(errorModel: ErrorModel) {
        showMsg(errorModel.message)
    }
}