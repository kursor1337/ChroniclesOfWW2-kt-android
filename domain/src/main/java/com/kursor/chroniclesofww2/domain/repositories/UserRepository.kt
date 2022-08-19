package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*

interface UserRepository {


    suspend fun login(loginReceiveDTO: LoginReceiveDTO): LoginResponseDTO

    suspend fun register(registerReceiveDTO: RegisterReceiveDTO): RegisterResponseDTO

    suspend fun getUserInfoList(): List<UserInfo>

    suspend fun getUserInfoByLogin(login: String): UserInfo?


}