package com.releaseit.ktorsse.android

import android.app.Application
import com.releaseit.ktorsse.initDependencyInjection
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDependencyInjection {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
        }
    }
}
