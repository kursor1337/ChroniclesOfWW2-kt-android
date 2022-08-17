package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.domain.useCases.user.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        LoginUseCase(userRepository = get())
    }

    factory {
        RegisterUseCase(userRepository = get())
    }

    factory {
        ChangeUsernameUseCase(
            userRepository = get(),
            tokenHandler = get<Settings>(),
            settingsRepository = get()
        )
    }

    factory {
        ChangePasswordUseCase(
            userRepository = get(),
            settingsRepository = get()
        )
    }

    factory {
        LogoutUseCase(tokenHandler = get<Settings>())
    }

    factory {
        GetUserInfoListUseCase(userRepository = get())
    }


}