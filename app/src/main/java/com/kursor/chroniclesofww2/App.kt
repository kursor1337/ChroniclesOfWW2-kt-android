package com.kursor.chroniclesofww2

import android.app.Application
import com.kursor.chroniclesofww2.di.appModule
import com.kursor.chroniclesofww2.di.connectionModule
import com.kursor.chroniclesofww2.di.dataModule
import com.kursor.chroniclesofww2.di.domainModule
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, appModule, domainModule, connectionModule)
        }
        val accountRepository by inject<AccountRepository>()
        accountRepository.refreshTokenInIntervals()
    }
}