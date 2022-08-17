package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.StandardBattleRepository
import com.kursor.chroniclesofww2.data.repositories.settingsRepositories.SharedPrefSettingsRepository
import com.kursor.chroniclesofww2.domain.interfaces.ISettingsRepository
import org.koin.dsl.module

val dataModule = module {
    single {
        LocalCustomBattleRepository(context = get())
    }

    single {
        StandardBattleRepository(context = get())
    }

    single<ISettingsRepository> {
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

}