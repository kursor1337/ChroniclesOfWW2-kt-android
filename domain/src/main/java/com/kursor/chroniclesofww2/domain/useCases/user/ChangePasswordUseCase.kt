package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.ChangePasswordReceiveDTO
import com.kursor.chroniclesofww2.features.ChangePasswordResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePasswordUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(
        changePasswordReceiveDTO: ChangePasswordReceiveDTO
    ): RequestResult<ChangePasswordResponseDTO> = tryRequest {
        accountRepository.password = changePasswordReceiveDTO.newPassword
        accountRepository.changePassword(changePasswordReceiveDTO)
    }

}