package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.data.repositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.StandardBattleRepository
import org.koin.dsl.module

val dataModule = module {
    single {
        LocalCustomBattleRepository(context = get())
    }
    single {
        StandardBattleRepository(context = get())
    }
}