package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.features.ChangePasswordResponseDTO
import com.kursor.chroniclesofww2.features.UpdateUserInfoReceiveDTO
import com.kursor.chroniclesofww2.features.UpdateUserInfoResponseDTO

interface AccountRepository {

    var username: String

    var token: String?
    var login: String?
    var password: String?

    suspend fun changePassword(
        changePasswordReceiveDTO: ChangePasswordReceiveDTO
    ): ChangePasswordResponseDTO

    suspend fun updateUserInfo(
        updateUserInfoReceiveDTO: UpdateUserInfoReceiveDTO
    ): UpdateUserInfoResponseDTO

    fun refreshToken()

    fun auth()

}