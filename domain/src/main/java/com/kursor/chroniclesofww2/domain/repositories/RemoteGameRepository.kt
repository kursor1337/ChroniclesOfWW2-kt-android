package com.kursor.chroniclesofww2.domain.repositories

import com.kursor.chroniclesofww2.features.*

interface RemoteGameRepository {

    suspend fun getWaitingGamesList(token: String): List<WaitingGameInfoDTO>

}