package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.TokenHandler

class LogoutUseCase(val tokenHandler: TokenHandler) {

    operator fun invoke() {
        tokenHandler.token = null
    }

}