package com.kursor.chroniclesofww2

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.kursor.chroniclesofww2.di.appModule
import com.kursor.chroniclesofww2.di.connectionModule
import com.kursor.chroniclesofww2.di.dataModule
import com.kursor.chroniclesofww2.di.domainModule
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.auth.RefreshTokenUseCase
import com.kursor.chroniclesofww2.features.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val refreshTokenUseCase by inject<RefreshTokenUseCase>()
        CoroutineScope(Dispatchers.IO).launch {
            refreshTokenUseCase().onFailure {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@App, "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}