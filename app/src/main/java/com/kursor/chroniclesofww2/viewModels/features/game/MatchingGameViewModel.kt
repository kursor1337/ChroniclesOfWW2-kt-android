package com.kursor.chroniclesofww2.viewModels.features.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursor.chroniclesofww2.connection.remote.RemoteConnection
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.features.*
import com.kursor.chroniclesofww2.game.MatchGameStatus
import com.kursor.chroniclesofww2.objects.Const
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MatchingGameViewModel(
    val httpClient: HttpClient,
    val accountRepository: AccountRepository
) : ViewModel() {

    private val _statusLiveData = MutableLiveData<Pair<MatchGameStatus, Any?>>()
    val statusLiveData: LiveData<Pair<MatchGameStatus, Any?>> get() = _statusLiveData

    private val _messagesLiveData = MutableLiveData<String>()
    val messagesLiveData: LiveData<String> get() = _messagesLiveData

    val connection = RemoteConnection(
        fullUrl = Routes.Game.MATCH.absolutePath(Const.connection.WEBSOCKET_SERVER_URL),
        httpClient = httpClient,
        dispatcher = Dispatchers.IO
    )

    fun startMatching() {
        val token = accountRepository.token
        if (token == null) {
            _statusLiveData.value = MatchGameStatus.UNAUTHORIZED to null
            return
        }
        viewModelScope.launch {
            connection.init(token)
        }
    }

    fun stopMatching() {
        viewModelScope.launch {
            connection.send(GameFeaturesMessages.CANCEL_CONNECTION)
            connection.shutdown()
        }
    }

    fun onConnectionInit() {
        viewModelScope.launch {
            connection.observeIncoming().collect { string ->
                val matchingGameMessageDTO = Json.decodeFromString<MatchingGameMessageDTO>(string)
                when (matchingGameMessageDTO.type) {
                    MatchingGameMessageType.MESSAGE -> {
                        _messagesLiveData.postValue(matchingGameMessageDTO.message)
                    }
                    MatchingGameMessageType.GAME_DATA -> {
                        _statusLiveData.postValue(MatchGameStatus.GAME_DATA_OBTAINED to matchingGameMessageDTO.message)
                    }
                    MatchingGameMessageType.TIMEOUT -> {
                        _statusLiveData.postValue(MatchGameStatus.TIMEOUT to null)
                    }
                    MatchingGameMessageType.ACCEPT -> {

                    }
                    MatchingGameMessageType.REJECT -> {
                        _statusLiveData.postValue(MatchGameStatus.REJECT to null)
                    }
                    MatchingGameMessageType.INIT -> {
                        _statusLiveData.postValue(
                            MatchGameStatus.FOUND to Json.decodeFromString<MatchingUserInfoDTO>(
                                matchingGameMessageDTO.message
                            )
                        )
                    }
                }
            }
        }
    }

}