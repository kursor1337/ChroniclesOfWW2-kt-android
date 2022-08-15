package com.kursor.chroniclesofww2.di

import io.ktor.client.*
import org.koin.dsl.module

val connectionModule = module {

    single { HttpClient() }

}