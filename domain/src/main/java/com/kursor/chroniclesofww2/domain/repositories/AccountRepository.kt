package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*
import kotlinx.coroutines.Job

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

    suspend fun deleteAccount(deleteUserReceiveDTO: DeleteUserReceiveDTO): DeleteUserResponseDTO

    suspend fun refreshToken()

    suspend fun auth()

    suspend fun checkCredentials(): Boolean

    suspend fun checkToken(): Boolean

    suspend fun isSignedIn(): Boolean

    suspend fun startTokenExpireTimer(millis: Long)
    fun refreshTokenInIntervals()
}