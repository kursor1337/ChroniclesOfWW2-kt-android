package com.kursor.chroniclesofww2.domain.useCases.user

import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.tools.RequestResult
import com.kursor.chroniclesofww2.domain.tools.tryRequest
import com.kursor.chroniclesofww2.features.DeleteUserReceiveDTO
import com.kursor.chroniclesofww2.features.DeleteUserResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteAccountUseCase(
    val accountRepository: AccountRepository
) {

    suspend operator fun invoke(
        login: String,
        password: String
    ): RequestResult<DeleteUserResponseDTO> =
        tryRequest {
            withContext(Dispatchers.IO) {
                accountRepository.deleteAccount(
                    DeleteUserReceiveDTO(
                        login = login,
                        password = password
                    )
                )
            }
        }

}