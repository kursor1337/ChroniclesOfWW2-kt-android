package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.Settings
import org.koin.dsl.module

val appModule = module {

    single {
        Settings(settingsRepository = get())
    }

}