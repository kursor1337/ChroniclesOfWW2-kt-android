package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository

class LogoutUseCase(val accountRepository: AccountRepository) {

    operator fun invoke() {
        accountRepository.token = null
        accountRepository.login = null
        accountRepository.password = null
    }

}