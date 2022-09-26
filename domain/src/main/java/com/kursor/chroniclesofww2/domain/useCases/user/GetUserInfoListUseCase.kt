package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserInfoListUseCase(val userRepository: UserRepository) {

    suspend operator fun invoke(): RequestResult<List<UserInfo>> = tryRequest {
        userRepository.getUserInfoList()
    }
}