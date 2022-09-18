package com.kursor.chroniclesofww2.connection.remote

import android.util.Log
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.features.GameSessionDTO
import com.kursor.chroniclesofww2.features.GameSessionMessageType
import com.kursor.chroniclesofww2.features.Routes
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class RemoteConnection(
    val fullUrl: String,
    val httpClient: HttpClient,
    val dispatcher: CoroutineDispatcher
) : Connection {


    val TAG = "RemoteConnection $fullUrl"

    override var sendListener: Connection.SendListener? = null
    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var webSocketSession: WebSocketSession

    suspend fun init(token: String) = withContext(Dispatchers.IO) {
        Log.d(TAG, "init: initiating connection to the path $fullUrl")
        webSocketSession = httpClient.webSocketSession {
            url(fullUrl)
            bearerAuth(token = token)
        }

    }

    override suspend fun send(string: String) = withContext(Dispatchers.IO) {
        Log.d(TAG, "sending: $string")
        webSocketSession.send(string)
    }

    override fun shutdown() {
        coroutineScope.launch {
            webSocketSession.close()
        }
    }

    override fun observeIncoming(): Flow<String> = webSocketSession.incoming
        .receiveAsFlow()
        .map { frame ->
            Log.d(TAG, "$frame")
            if (frame is Frame.Close) shutdownListeners.forEach { listener ->
                listener.onConnectionDisposed()
                return@map Frame.Text(
                    Json.encodeToString(
                        GameSessionDTO(
                            type = GameSessionMessageType.DISCONNECT,
                            message = frame.readReason()?.message
                                ?: GameSessionMessageType.DISCONNECT.toString()
                        )
                    )
                )
            }
            frame
        }.filter { it is Frame.Text }
        .map {
            (it as Frame.Text).readText()
        }.flowOn(Dispatchers.IO)



}