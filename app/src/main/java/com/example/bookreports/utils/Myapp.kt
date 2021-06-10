package com.example.bookreports.utils

import android.app.Application
import com.example.bookreports.BuildConfig
import timber.log.Timber

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }


    }
}