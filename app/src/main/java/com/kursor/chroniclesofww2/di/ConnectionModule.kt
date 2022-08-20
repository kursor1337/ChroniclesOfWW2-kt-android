package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.connection.local.NsdLocalClient
import com.kursor.chroniclesofww2.connection.local.NsdLocalServer
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*

import org.koin.dsl.module

val connectionModule = module {

    single {
        HttpClient(CIO) {
            install(WebSockets)

            install(ContentNegotiation) {
                json()
            }
        }
    }

    single<LocalClient> {
        NsdLocalClient(context = get())
    }

    single<LocalServer> {
        NsdLocalServer(context = get())
    }

}