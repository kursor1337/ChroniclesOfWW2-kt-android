package com.kursor.chroniclesofww2.domain.useCases.game

import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.LocalClient

class JoinLocalGameUseCase(
    val localClient: LocalClient
) {

    suspend operator fun invoke(host: Host) {
        localClient.connectTo(host)
    }

}