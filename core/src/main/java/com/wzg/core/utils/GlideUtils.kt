package com.wzg.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION
import com.bumptech.glide.request.RequestOptions
import com.wzg.core.R
import java.io.File
import java.security.MessageDigest


/**
 * 类描述:
 * 创建人:    wzg
 * 创建时间:  2018/3/16
 * 修改时间:  2018/3/16
 * 修改备注:  说明本次修改内容
 */
class GlideUtils {
    companion object {
        val instance = GlideUtils()
    }

    /**
     * 默认glide，不做任何处理，glide 从字符串中加载图片（网络地址或者本地地址）
     */
    fun disPlayDefaultCenterCrop(context: Context, url: String, view: ImageView) {
        Glide.with(context).load(url).apply(getOptions()).into(view)
    }

    fun disPlayDefaultFitXY(context: Context, url: String, view: ImageView) {
        view.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(context).load(url).into(view)
    }

    /**
     * 默认glide，不做任何处理，加载资源图片
     */
    fun disPlayDefault(context: Context, id: Int, view: ImageView) {
        Glide.with(context).load(id).into(view)
    }

    /**
     * 默认glide，不做任何处理，加载资源图片
     */
    fun disPlayDefault(context: Context, url: String, view: ImageView) {
        val requestOptions = getOptions().centerInside()
        Glide.with(context).load(url)
                .apply(requestOptions)
                .into(view)
    }

    /**
     * glide 从文件中加载图片
     */
    fun disPlayFileCenterCrop(context: Context, file: File, view: ImageView) {
        var requestOptions = getOptions()
        Glide.with(context).load(file)
                .apply(requestOptions)
                .into(view)
    }

    /**
     * glide 从URI中加载图片
     */
    fun disPlayUri(context: Context, uri: Uri, view: ImageView, defaultId: Int) {
        var requestOptions = getOptions().placeholder(defaultId)
        Glide.with(context).load(uri)
                .apply(requestOptions)
                .into(view)
    }

    /**
     * glide 通过指定的大小从字符串中加载图片（网络地址或者本地地址）
     */
    fun disPlayImageSize(context: Context, url: String, view: ImageView, width: Int, height: Int) {
        var requestOptions = getOptions().override(width, height)
        Glide.with(context).load(url)
                .apply(requestOptions)
                .into(view)
    }

    private fun getOptions(): RequestOptions {
        return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_empty)
                .dontAnimate()
                .centerCrop()
                .priority(Priority.NORMAL)
    }

    //截取视频的某一帧
    fun loadVideoScreenshot(context: Context, uri: String, imageView: ImageView, frameTimeMicros: Long) {
        // 这里的时间是以微秒为单位
        val requestOptions = RequestOptions.frameOf(frameTimeMicros)
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
        requestOptions.transform(object : BitmapTransformation() {
            override fun updateDiskCacheKey(messageDigest: MessageDigest) {
                try {
                    messageDigest.update((context.packageName + "RotateTransform").toByteArray(charset("utf-8")))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
                return toTransform
            }

        })
        Glide.with(context).load(uri).apply(requestOptions).into(imageView)
    }
}