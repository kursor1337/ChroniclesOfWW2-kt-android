package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.viewModels.HostDiscoveryViewModel
import com.kursor.chroniclesofww2.viewModels.SettingsViewModel
import com.kursor.chroniclesofww2.viewModels.game.session.GameSessionViewModel
import com.kursor.chroniclesofww2.viewModels.features.LoginViewModel
import com.kursor.chroniclesofww2.viewModels.features.RegisterViewModel
import com.kursor.chroniclesofww2.viewModels.game.create.CreateLocalGameViewModel
import com.kursor.chroniclesofww2.viewModels.game.create.CreateRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.game.join.JoinLocalGameViewModel
import com.kursor.chroniclesofww2.viewModels.game.join.JoinRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.game.session.SingleHostGameViewModel
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

    viewModel { parameters ->
        GameSessionViewModel(Tools.currentConnection!!, parameters.get())
    }

    viewModel { parameters ->
        SingleHostGameViewModel(parameters.get())
    }

    viewModel {
        CreateLocalGameViewModel(
            localServer = get(),
            accountRepository = get()
        )
    }

    viewModel {
        HostDiscoveryViewModel(localClient = get())
    }

    viewModel {
        JoinLocalGameViewModel(localClient = get())
    }

    viewModel {
        CreateRemoteGameViewModel(
            accountRepository = get(),
            httpClient = get()
        )
    }

    viewModel {
        JoinRemoteGameViewModel(
            accountRepository = get(),
            httpClient = get(),
            loadRemoteGameListUseCase = get()
        )
    }

    viewModel {
        SettingsViewModel(
            accountRepository = get(),
            checkIsSignedInUseCase = get(),
            changeUsernameUseCase = get(),
            logoutUseCase = get()
        )
    }
}