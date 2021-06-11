package com.example.bookreports.utils

import android.app.Application
import com.example.bookreports.BuildConfig
import com.example.bookreports.network.BookApi
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import timber.log.Timber

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        val builder :Picasso.Builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this,Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        built.apply {
            setIndicatorsEnabled(true)
            isLoggingEnabled
        }
        Picasso.setSingletonInstance(built)

    }
}