package com.nda.simpletodo

import android.app.Application

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        UtilsManager.init(applicationContext)
    }
}