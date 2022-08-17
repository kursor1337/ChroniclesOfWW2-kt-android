package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.ITokenHandler

class LogoutUseCase(val tokenHandler: ITokenHandler) {

    operator fun invoke() {
        tokenHandler.token = null
    }

}