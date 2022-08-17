package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.interfaces.IUserRepository
import com.kursor.chroniclesofww2.features.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserInfoListUseCase(val userRepository: IUserRepository) {

    suspend operator fun invoke(): List<UserInfo> {
        return withContext(Dispatchers.IO) {
            userRepository.getUserInfoList()
        }
    }

}