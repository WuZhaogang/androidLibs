package com.wzg.core.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/1/31
 * 修改时间:  2018/1/31
 * 修改备注:  说明本次修改内容
 */
abstract class BaseLazyFragment : Fragment() {
    // 控件是否初始化完成
    // 我们在控件初始化完成之后再进行数据的加载，否则对控件进行操作的时候会遇到空指针异常
    protected var isViewCreated: Boolean = false

    // 是否加载过数据
    // 我们判断未曾加载过数据的话再进行获取，否则每次对用户可见时都会执行懒加载的方法
    protected var isLoadCompleted: Boolean = false


    // 该方法只有在ViewPager与Fragment结合使用的时候才会执行
    // 该方法在onCreateView之前调用
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isViewCreated && !isLoadCompleted) {
            // 只有在对用户可见、控件初始化完成并且未曾加载过数据的情况下才进行懒加载
            lazyLoad()
            isLoadCompleted = true
        }
    }

    // ViewPager的第一个Fragment默认执行setUserVisibleHint(fasle)方法
    // 所以在activity创建完成后要让第一页也加载数据
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {
            // 此处不需要判断isViewCreated，因为这个方法在onCreateView方法之后执行
            lazyLoad()
            isLoadCompleted = true
        }
    }

    // 懒加载,强制子类重写
    protected abstract fun lazyLoad()
}
