package com.wzg.core.uiKit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

/**
 * Created by yuanshuobin on 16/8/26.
 */
class HeaderWaveView : View {


    // if true, the shader will display the wave
    var isShowWave: Boolean = false

    // shader containing repeated waves
    private var mWaveShader: BitmapShader? = null
    // shader matrix
    private var mShaderMatrix: Matrix? = null
    // paint to draw wave
    private var mViewPaint: Paint? = null

    private var mDefaultAmplitude: Float = 0.toFloat()
    private var mDefaultWaterLevel: Float = 0.toFloat()
    private var mDefaultWaveLength: Float = 0.toFloat()
    private var mDefaultAngularFrequency: Double = 0.toDouble()

    /**
     * Set vertical size of wave according to `amplitudeRatio`
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     * <br></br>Ratio of amplitude to height of WaveView.
     */
    var amplitudeRatio = DEFAULT_AMPLITUDE_RATIO
        set(amplitudeRatio) {
            if (this.amplitudeRatio != amplitudeRatio) {
                field = amplitudeRatio
                invalidate()
            }
        }
    private val mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO
    /**
     * Set water level according to `waterLevelRatio`.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     * <br></br>Ratio of water level to WaveView height.
     */
    var waterLevelRatio = DEFAULT_WATER_LEVEL_RATIO
        set(waterLevelRatio) {
            if (this.waterLevelRatio != waterLevelRatio) {
                field = waterLevelRatio
                invalidate()
            }
        }
    /**
     * Shift the wave horizontally according to `waveShiftRatio`.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     * <br></br>Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    var waveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO
        set(waveShiftRatio) {
            if (this.waveShiftRatio != waveShiftRatio) {
                field = waveShiftRatio
                invalidate()
            }
        }

    private var mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR
    private var mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR

    //sin函数的height
    val sinHeight: Float
        get() = (height * waterLevelRatio + (amplitudeRatio / DEFAULT_AMPLITUDE_RATIO).toDouble() * mDefaultAmplitude.toDouble() * Math.sin(waveShiftRatio.toDouble() * width.toDouble() * mDefaultAngularFrequency)).toFloat()


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mShaderMatrix = Matrix()
        mViewPaint = Paint()
        mViewPaint!!.isAntiAlias = true
    }


    fun setWaveColor(behindWaveColor: Int, frontWaveColor: Int) {
        mBehindWaveColor = behindWaveColor
        mFrontWaveColor = frontWaveColor

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createShader()
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private fun createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO.toDouble() / width.toDouble()
        mDefaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO
        mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO
        mDefaultWaveLength = width.toFloat()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val wavePaint = Paint()
        wavePaint.strokeWidth = 2f
        wavePaint.isAntiAlias = true
        wavePaint.color = mFrontWaveColor

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        val endX = width + 1
        val endY = height + 1

        val waveY = FloatArray(endX)

        wavePaint.color = mBehindWaveColor
        for (beginX in 0 until endX) {
            val wx = beginX * mDefaultAngularFrequency
            val beginY = (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx)).toFloat()
            canvas.drawLine(beginX.toFloat(), beginY, beginX.toFloat(), -1f, wavePaint)
            waveY[beginX] = beginY
        }

        wavePaint.color = mFrontWaveColor
        val wave2Shift = (mDefaultWaveLength / 4).toInt()
        for (beginX in 0 until endX) {
            canvas.drawLine(beginX.toFloat(), waveY[(beginX + wave2Shift) % endX], beginX.toFloat(), -1f, wavePaint)
        }

        // use the bitamp to create the shader
        mWaveShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        mViewPaint!!.shader = mWaveShader

    }

    override fun onDraw(canvas: Canvas) {
        // modify paint shader according to mShowWave state
        if (isShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint!!.shader == null) {
                mViewPaint!!.shader = mWaveShader
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix!!.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    amplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0f,
                    mDefaultWaterLevel)
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix!!.postTranslate(
                    waveShiftRatio * width,
                    (DEFAULT_WATER_LEVEL_RATIO - (1f - waterLevelRatio)) * height)
            // assign matrix to invalidate the shader
            mWaveShader!!.setLocalMatrix(mShaderMatrix)

            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mViewPaint!!)


        } else {
            mViewPaint!!.shader = null
        }
    }

    companion object {
        private val DEFAULT_AMPLITUDE_RATIO = 0.05f
        private val DEFAULT_WATER_LEVEL_RATIO = 0.1f
        private val DEFAULT_WAVE_LENGTH_RATIO = 1.0f
        private val DEFAULT_WAVE_SHIFT_RATIO = 0.0f

        val DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#40b7d28d")
        val DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#80b7d28d")
    }
}
