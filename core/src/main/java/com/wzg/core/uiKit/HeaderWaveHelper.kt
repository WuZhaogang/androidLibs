package com.wzg.core.uiKit

/**
 * Created by yuanshuobin on 16/8/26.
 */

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

import java.util.ArrayList

class HeaderWaveHelper(private val mHeaderWaveView: HeaderWaveView, behindWaveColor: Int, frontWaveColor: Int) {
    private var mAnimatorSet: AnimatorSet? = null
    private var mAmplitudeAnim: ObjectAnimator? = null
    private var amplitudeChangeAnim: ObjectAnimator? = null
    private var mHasCancel = false
    private var mHasStart = false
    private var mIsFirst = false
    //水位高低 waterLevelRatio
    private var mDefaultWaterLevelRatioF = 1.0f
    private var mDefaultWaterLevelRatioT = 1.0f
    //波浪大小振幅 amplitudeRatio
    private var mDefaultAmplitudeRatioF = 0.1f
    private var mDefaultAmplitudeRatioT = 0.06f
    //floatView 旋转调整角
    private var mDefaultFloatViewRotation = 85f

    fun setDefaultWaterLevelRatio(defaultWaterLevelRatioF: Float, defaultWaterLevelRatioT: Float) {
        mDefaultWaterLevelRatioF = defaultWaterLevelRatioF
        mDefaultWaterLevelRatioT = defaultWaterLevelRatioT
    }

    fun setDefaultAmplitudeRatio(defaultAmplitudeRatioF: Float, defaultAmplitudeRatioT: Float) {
        mDefaultAmplitudeRatioF = defaultAmplitudeRatioF
        mDefaultAmplitudeRatioT = defaultAmplitudeRatioT
    }

    fun setDefaultFloatViewRotation(defaultFloatViewRotation: Float) {
        mDefaultFloatViewRotation = defaultFloatViewRotation
    }

    init {
        mHeaderWaveView.setWaveColor(behindWaveColor, frontWaveColor)
        initAnimation()
    }

    private fun initAnimation() {
        val animators = ArrayList<Animator>()
        val waterLevelAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "waterLevelRatio", mDefaultWaterLevelRatioF, mDefaultWaterLevelRatioT)
        waterLevelAnim.duration = 10000
        waterLevelAnim.interpolator = DecelerateInterpolator()


        // amplitude animation.
        // wave grows big then grows small, repeatedly
        mAmplitudeAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "amplitudeRatio", mDefaultAmplitudeRatioF, mDefaultAmplitudeRatioT)
        mAmplitudeAnim?.repeatCount = ValueAnimator.INFINITE
        mAmplitudeAnim?.repeatMode = ValueAnimator.REVERSE
        mAmplitudeAnim?.duration = 5000
        mAmplitudeAnim?.interpolator = LinearInterpolator()


        // horizontal animation.
        // wave waves infinitely.
        val waveShiftAnim = ObjectAnimator.ofFloat(
                mHeaderWaveView, "waveShiftRatio", 0f, 1f)
        waveShiftAnim.repeatCount = ValueAnimator.INFINITE
        waveShiftAnim.duration = 10000
        waveShiftAnim.interpolator = LinearInterpolator()


        //        waveShiftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //            @Override
        //            public void onAnimationUpdate(ValueAnimator valueAnimator) {
        //                //获取sin函数的height，更新mFloatView
        //                float value = mHeaderWaveView.getSinHeight() - mFloatView.getMeasuredHeight();
        //                mFloatView.setRotation(value + mDefaultFloatViewRotation);
        //                mFloatView.setTranslationY(value);
        //                mFloatView.invalidate();
        //            }
        //        });

        animators.add(waveShiftAnim)
        mAmplitudeAnim?.let { animators.add(it) }
        animators.add(waterLevelAnim)
        mAnimatorSet = AnimatorSet()
        mAnimatorSet?.playTogether(animators)

    }

    fun start() {
        mHeaderWaveView.isShowWave = true
        if (mAnimatorSet != null && !mHasStart) {
            mHasStart = true
            mHasCancel = false
            if (amplitudeChangeAnim != null) {
                amplitudeChangeAnim = ObjectAnimator.ofFloat(
                        mHeaderWaveView, "amplitudeRatio", 0.00001f, mDefaultAmplitudeRatioF)
                amplitudeChangeAnim?.duration = 1000
                amplitudeChangeAnim?.start()
            }

            if (!mIsFirst) {
                mAnimatorSet?.start()
                mIsFirst = true
            }


        }
    }

    fun cancel() {
        if (mAnimatorSet != null && !mHasCancel) {
            mHasCancel = true
            mHasStart = false
            mAmplitudeAnim?.cancel()
            amplitudeChangeAnim = ObjectAnimator.ofFloat(
                    mHeaderWaveView, "amplitudeRatio", mDefaultAmplitudeRatioT, 0.00001f)
            amplitudeChangeAnim?.duration = 1000
            amplitudeChangeAnim?.start()

        }
    }
}