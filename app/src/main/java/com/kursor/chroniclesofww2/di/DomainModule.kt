package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.domain.useCases.auth.*
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.DeleteLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.EditLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudLocal.SaveLocalCustomBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote.EditPublishedBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote.PublishBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.crudRemote.UnpublishBattleUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadLocalCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadMyRemoteBattlesListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadRemoteCustomBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.battle.load.LoadStandardBattleListUseCase
import com.kursor.chroniclesofww2.domain.useCases.game.LoadRemoteGameListUseCase
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
        LoadRemoteCustomBattleListUseCase(
            remoteCustomBattleRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        LoadMyRemoteBattlesListUseCase(
            remoteCustomBattleRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        LoadRemoteGameListUseCase(
            gameRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        RefreshTokenUseCase(accountRepository = get())
    }

    factory {
        AuthUseCase(accountRepository = get())
    }

    factory {
        CheckCredentialsUseCase(accountRepository = get())
    }

    factory {
        CheckTokenUseCase(accountRepository = get())
    }

    factory {
        CheckIsSignedInUseCase(accountRepository = get())
    }

    factory {
        DeleteLocalCustomBattleUseCase(localCustomBattleRepository = get())
    }

    factory {
        EditLocalCustomBattleUseCase(localCustomBattleRepository = get())
    }

    factory {
        SaveLocalCustomBattleUseCase(localCustomBattleRepository = get())
    }

    factory {
        EditPublishedBattleUseCase(
            remoteCustomBattleRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        PublishBattleUseCase(
            remoteCustomBattleRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        UnpublishBattleUseCase(
            remoteCustomBattleRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        GetAccountInfoUseCase(accountRepository = get())
    }

}