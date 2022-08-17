package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.data.repositories.battle.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.battle.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.battle.StandardBattleRepository
import com.kursor.chroniclesofww2.data.repositories.settings.SharedPrefSettingsRepository
import com.kursor.chroniclesofww2.data.repositories.user.UserRepositoryImpl
import com.kursor.chroniclesofww2.domain.interfaces.SettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.UserRepository
import com.kursor.chroniclesofww2.objects.Const
import org.koin.dsl.module

val dataModule = module {
    single {
        LocalCustomBattleRepository(context = get())
    }

    single {
        StandardBattleRepository(context = get())
    }

    single {
        RemoteCustomBattleRepository(
            serverUrl = Const.connection.FULL_SERVER_URL,
            httpClient = get()
        )
    }

    single<SettingsRepository> {
        SharedPrefSettingsRepository(context = get())
    }

    single {
        BattleManager(
            listOf(
                get<StandardBattleRepository>(),
                get<LocalCustomBattleRepository>()
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