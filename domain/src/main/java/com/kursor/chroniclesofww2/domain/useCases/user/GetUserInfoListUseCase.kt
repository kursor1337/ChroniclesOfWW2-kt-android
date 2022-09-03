package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserInfoListUseCase(val userRepository: UserRepository) {

    suspend operator fun invoke(): Result<List<UserInfo>> = runCatching {
        withContext(Dispatchers.IO) {
            userRepository.getUserInfoList()
        }
    }

}