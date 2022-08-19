package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.viewModels.features.LoginViewModel
import com.kursor.chroniclesofww2.viewModels.features.RegisterViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleListViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        LoginViewModel(loginUseCase = get())
    }

    viewModel {
        RegisterViewModel(registerUseCase = get())
    }

    viewModel {
        GameDataViewModel()
    }

    viewModel {
        BattleViewModel()
    }

    viewModel {
        BattleListViewModel(
            loadStandardBattleListUseCase = get(),
            loadLocalCustomBattleListUseCase = get(),
            loadRemoteCustomBattleListUseCase = get()
        )
    }

}