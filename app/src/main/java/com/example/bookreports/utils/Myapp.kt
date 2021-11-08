package com.example.bookreports.utils

import android.app.Application
import com.example.bookreports.BuildConfig
import com.example.bookreports.home.BookViewModel
import com.example.bookreports.network.BookApi
import com.example.bookreports.registerinfo.AccountViewModel
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        setupKoin()
    }

    private fun setupKoin(){

        val viewModelModule = module {
            viewModel{ AccountViewModel() }
            viewModel { BookViewModel() }
        }

        val localModule = module {
            single { userInfo() }
        }

        startKoin {
            this@MyApp
            modules(
                viewModelModule,
                localModule
            )
        }
    }
}