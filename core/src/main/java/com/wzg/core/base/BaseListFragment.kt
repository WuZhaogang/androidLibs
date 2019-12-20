package com.wzg.core.base

import androidx.recyclerview.widget.LinearLayoutManager
import com.wzg.core.model.ErrorModel
import com.wzg.core.uiKit.CustomRecyclerView
import com.wzg.core.uiKit.CustomRefreshLayout

/**
 * 类描述:    列表基类
 * 创建人:    wzg
 * 创建时间:  2018/5/31
 * 修改时间:  2018/5/31
 * 修改备注:  说明本次修改内容
 */
abstract class BaseListFragment<P : BasePresenter<*>, A : BaseRecyclerAdapter<*>> : BaseFragment<P>() {
    var layRefresh: CustomRefreshLayout? = null
    var recycleAdapter: A? = null
    var rlData: CustomRecyclerView? = null

    abstract fun createAdapter(): A?

    private fun initAdapter() {
        recycleAdapter = createAdapter()
    }

    protected fun initRefreshLayout(rlId: Int, refreshId: Int) {
        layRefresh = rootView?.findViewById(refreshId)
        rlData = rootView?.findViewById(rlId)
        rlData?.layoutManager = LinearLayoutManager(activity!!)
        rlData?.setNestedEmptyView(true)
        initAdapter()
        rlData?.adapter = recycleAdapter
        recycleAdapter?.isUseEmpty(true)
    }

    override fun onGetListSuccess() {
        layRefresh?.finishRefresh()
        recycleAdapter?.loadMoreComplete()
    }

    override fun onNoMoreData() {
        super.onNoMoreData()
        layRefresh?.finishRefresh()
        recycleAdapter?.loadMoreEnd()
    }

    override fun onCodeError(errorModel: ErrorModel?) {
        super.onCodeError(errorModel)
        layRefresh?.finishRefresh()
        recycleAdapter?.loadMoreFail()
    }


}