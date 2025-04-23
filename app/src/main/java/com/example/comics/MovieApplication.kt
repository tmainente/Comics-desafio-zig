package com.example.comics

import android.app.Application
import com.example.comics.di.MovieModule
import com.example.comics.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MovieApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(NetworkModule.modules)
            modules(MovieModule.modules)
        }
    }
}