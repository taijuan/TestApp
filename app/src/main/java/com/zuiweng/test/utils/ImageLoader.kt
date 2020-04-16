package com.zuiweng.test.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.collection.LruCache
import io.zhuozhuo.remotetestlib.DataCenter
import io.zhuozhuo.remotetestlib.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private val lruCache: LruCache<String, Bitmap> by lazy {
    val cacheSize = Runtime.getRuntime().maxMemory() / 8
    object : LruCache<String, Bitmap>(cacheSize.toInt()) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount
        }
    }
}

fun Message.loadImage(callback: (Bitmap, String) -> Unit) {
//    val max = screenWidth() * 3 / 4
//    val dw = imageWidth * 1f / max
//    val dh = imageHeight * 1f / max
//    var x = if (dw > dh) dw else dh
//    x = if (x > 1f) x else 1f
//    val bmx = Bitmap.createBitmap(
//        this.imageWidth / x.toInt(),
//        this.imageHeight / x.toInt(),
//        Bitmap.Config.RGB_565
//    )
//    callback.invoke(bmx, this@loadImage.content)
//    GlobalScope.launch(Dispatchers.Main) {
//        var bm = lruCache.get(this@loadImage.content)
//        if (bm != null) {
//            callback.invoke(bm, this@loadImage.content)
//        } else {
//            val data = async(Dispatchers.IO) {
//                DataCenter.loadImage__NotMainThread(this@loadImage.content)
//            }.await()
//            bm = BitmapFactory.decodeByteArray(data, 0, data.size, BitmapFactory.Options().apply {
//                this.inSampleSize = x.toInt()
//            })
//            if (bm != null) {
//                lruCache.put(this@loadImage.content, bm)
//                callback.invoke(bm, this@loadImage.content)
//            }
//        }
//    }

    val max = screenWidth() - dp2px(8f) - dp2px(8f)
    val dw = imageWidth * 1f / max
    val dh = imageHeight * 1f / max
    var x = if (dw > dh) dw else dh
    x = if (x > 1f) x else 1f
    val bmx = Bitmap.createBitmap(
        (this.imageWidth / x).toInt(),
        (this.imageHeight / x).toInt(),
        Bitmap.Config.RGB_565
    )
    callback.invoke(bmx, this@loadImage.content)
    GlobalScope.launch(Dispatchers.Main) {
        var bm = lruCache.get(this@loadImage.content)
        if (bm != null) {
            callback.invoke(bm, this@loadImage.content)
        } else {
            val data = async(Dispatchers.IO) {
                DataCenter.loadImage__NotMainThread(this@loadImage.content)
            }.await()
            var bmm = BitmapFactory.decodeByteArray(data, 0, data.size, null)
            val mx = Matrix().apply {
                this.postScale(1 / x, 1 / x)
            }
            bm = Bitmap.createBitmap(bmm, 0, 0, bmm.width, bmm.height, mx, false)
            if (bmm.isRecycled) {
                bmm.recycle()
                bmm = null
            }
            if (bm != null) {
                lruCache.put(this@loadImage.content, bm)
                callback.invoke(bm, this@loadImage.content)
            }
        }
    }
}

fun clearLruCache() {
    if (lruCache.size() > 0) {
        lruCache.evictAll()
    }
}