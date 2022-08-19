package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.domain.useCases.battle.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.LoadRemoteCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.LoadStandardBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.user.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        LoginUseCase(
            accountRepository = get(),
            userRepository = get()
        )
    }

    factory {
        RegisterUseCase(
            accountRepository = get(),
            userRepository = get()
        )
    }

    factory {
        ChangeUsernameUseCase(
            accountRepository = get()
        )
    }

    factory {
        ChangePasswordUseCase(
            accountRepository = get()
        )
    }

    factory {
        LogoutUseCase(accountRepository = get())
    }

    factory {
        GetUserInfoListUseCase(userRepository = get())
    }

    factory {
        LoadStandardBattleListUseCase(standardBattleRepository = get())
    }

    factory {
        LoadLocalCustomBattleListUseCase(battleRepository = get())
    }

    factory {
        LoadRemoteCustomBattleListUseCase(remoteCustomBattleRepository = get())
    }


}