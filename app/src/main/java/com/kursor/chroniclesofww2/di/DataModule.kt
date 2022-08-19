package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.data.repositories.battle.LocalCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.battle.RemoteCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.battle.StandardBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.account.AccountRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.user.UserRepositoryImpl
import com.kursor.chroniclesofww2.domain.interfaces.AccountRepository
import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.objects.Const
import org.koin.dsl.module

val dataModule = module {
    single {
        LocalCustomBattleRepositoryImpl(context = get())
    }

    single {
        StandardBattleRepositoryImpl(context = get())
    }

    single {
        RemoteCustomBattleRepositoryImpl(
            serverUrl = Const.connection.FULL_SERVER_URL,
            httpClient = get(),
            accountRepository = get()
        )
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            context = get(),
            httpClient = get(),
            serverUrl = Const.connection.FULL_SERVER_URL
        )
    }

    single {
        BattleManager(
            listOf(
                get<StandardBattleRepositoryImpl>(),
                get<LocalCustomBattleRepositoryImpl>()
            )
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            serverUrl = Const.connection.FULL_SERVER_URL,
            httpClient = get()
        )
    }

}