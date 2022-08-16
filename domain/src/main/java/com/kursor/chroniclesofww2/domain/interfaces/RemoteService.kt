package com.kursor.chroniclesofww2.domain.interfaces

import com.kursor.chroniclesofww2.features.LoginReceiveDTO

interface RemoteService {

    suspend fun login(loginReceiveDTO: LoginReceiveDTO)

}