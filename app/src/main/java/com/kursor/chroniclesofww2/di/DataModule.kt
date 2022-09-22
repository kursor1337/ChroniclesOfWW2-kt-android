package com.kursor.chroniclesofww2.di

import androidx.room.Room
import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.data.repositories.battle.LocalCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.battle.RemoteCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.battle.StandardBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.account.AccountRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.database.Database
import com.kursor.chroniclesofww2.data.repositories.game.RemoteGameRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.user.UserRepositoryImpl
import com.kursor.chroniclesofww2.domain.repositories.*
import com.kursor.chroniclesofww2.objects.Const
import org.koin.dsl.module

val dataModule = module {

    single<Database> {
        Room.databaseBuilder(
            get(),
            Database::class.java,
            "ChroniclesOfWW2-kt-android-db"
        ).build()
    }

    single {
        get<Database>().battleDao()
    }

    single<LocalCustomBattleRepository> {
        LocalCustomBattleRepositoryImpl(
            context = get(),
            battleDao = get()
        )
    }

    single<StandardBattleRepository> {
        StandardBattleRepositoryImpl(context = get())
    }

    single<RemoteCustomBattleRepository> {
        RemoteCustomBattleRepositoryImpl(
            serverUrl = Const.connection.HTTP_SERVER_URL,
            httpClient = get()
        )
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            context = get(),
            httpClient = get(),
            serverUrl = Const.connection.HTTP_SERVER_URL
        )
    }

    single {
        BattleManager(
            listOf(
                get<StandardBattleRepository>(),
                get<LocalCustomBattleRepository>(),
                get<RemoteCustomBattleRepository>()
            )
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            serverUrl = Const.connection.HTTP_SERVER_URL,
            httpClient = get()
        )
    }

    single<RemoteGameRepository> {
        RemoteGameRepositoryImpl(
            httpClient = get(),
            serverUrl = Const.connection.HTTP_SERVER_URL
        )
    }

}