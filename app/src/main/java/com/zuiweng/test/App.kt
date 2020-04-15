package com.zuiweng.test

import android.app.Application
import com.zuiweng.test.utils.initUtils

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        this.initUtils()
    }
}