package com.kursor.chroniclesofww2.di

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

}