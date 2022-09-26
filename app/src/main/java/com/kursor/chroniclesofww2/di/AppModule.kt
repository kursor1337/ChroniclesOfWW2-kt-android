package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.viewModels.HostDiscoveryViewModel
import com.kursor.chroniclesofww2.viewModels.SettingsViewModel
import com.kursor.chroniclesofww2.viewModels.features.battle.CreateNewBattleViewModel
import com.kursor.chroniclesofww2.viewModels.features.battle.BattlesManagementViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.session.GameSessionViewModel
import com.kursor.chroniclesofww2.viewModels.features.user.LoginViewModel
import com.kursor.chroniclesofww2.viewModels.features.user.RegisterViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.create.CreateLocalGameViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.create.CreateRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.join.JoinLocalGameViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.join.JoinRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.features.game.session.SingleHostGameViewModel
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
            logoutUseCase = get(),
            getAccountInfoUseCase = get()
        )
    }

    viewModel {
        CreateNewBattleViewModel(
            saveLocalCustomBattleUseCase = get()
        )
    }

    viewModel {
        BattlesManagementViewModel(
            loadLocalCustomBattleListUseCase = get(),
            deleteLocalCustomBattleUseCase = get(),
            loadPublishedBattleUseCase = get(),
            publishBattleUseCase = get(),
            unpublishBattleUseCase = get()
        )
    }
}