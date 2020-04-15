package com.zuiweng.test.utils

import android.app.Application

lateinit var app :Application

fun Application.initUtils(){
    app = this
}
/**
 * dp转px
 */
fun dp2px(dpValue: Float): Int {
    val scale = app.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px 转化为 dp
 */
fun px2dp(pxValue: Float): Int {
    val scale = app.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 屏幕宽度
 */
fun screenWidth(): Int {
    return app.resources.displayMetrics.widthPixels
}