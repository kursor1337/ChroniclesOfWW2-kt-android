package com.kursor.chroniclesofww2.domain.interfaces

import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.model.serializable.Battle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UserRepository {


    suspend fun login(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO

    suspend fun register(registerReceiveDTO: RegisterReceiveDTO): RegisterResponseDTO

    suspend fun getUserInfoList(): List<UserInfo>

    suspend fun getUserInfoByLogin(login: String): UserInfo?


}