package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.UserRepository
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.LeaderboardInfoReceiveDTO

class ObtainLeaderBoardUseCase(
    val userRepository: UserRepository
) {

    suspend operator fun invoke() = tryRequest {
        userRepository.getLeaderboard(
            LeaderboardInfoReceiveDTO(
                quantity = -1
            )
        )
    }

}