package com.kursor.chroniclesofww2

import android.app.Application
import com.kursor.chroniclesofww2.di.appModule
import com.kursor.chroniclesofww2.di.connectionModule
import com.kursor.chroniclesofww2.di.dataModule
import com.kursor.chroniclesofww2.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, appModule, domainModule, connectionModule)
        }
    }


}