package com.kursor.chroniclesofww2.di

import com.kursor.chroniclesofww2.domain.useCases.user.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        LoginUseCase(
            accountRepository = get<Settings>(),
            userRepository = get()
        )
    }

    factory {
        RegisterUseCase(
            accountRepository = get<Settings>(),
            userRepository = get()
        )
    }

    factory {
        ChangeUsernameUseCase(
            userRepository = get(),
            accountRepository1 = get<Settings>(),
            accountRepository = get()
        )
    }

    factory {
        ChangePasswordUseCase(
            userRepository = get(),
            accountRepository = get()
        )
    }

    factory {
        LogoutUseCase(accountRepository = get<Settings>())
    }

    factory {
        GetUserInfoListUseCase(userRepository = get())
    }


}