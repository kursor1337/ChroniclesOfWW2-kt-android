package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.viewModels.features.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Settings(settingsRepository = get())
    }

    viewModel {
        LoginViewModel(loginUseCase = get())
    }

}