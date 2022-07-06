package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.data.repositories.CustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.StandardBattleRepository
import org.koin.dsl.module

val dataModule = module {
    single {
        CustomBattleRepository(context = get())
    }
    single {
        StandardBattleRepository(context = get())
    }
}