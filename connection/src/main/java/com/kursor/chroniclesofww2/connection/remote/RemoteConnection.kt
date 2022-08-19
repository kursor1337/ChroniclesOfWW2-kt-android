package com.kursor.chroniclesofww2.connection.remote

import com.kursor.chroniclesofww2.connection.interfaces.Connection
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class RemoteConnection(
    baseUrl: String,
    path: String,
    val httpClient: HttpClient,
    dispatcher: CoroutineDispatcher
) : Connection {

    val fullUrl = "ws://$baseUrl/$path"

    override var sendListener: Connection.SendListener? = null
    override val shutdownListeners = mutableListOf<Connection.ShutdownListener>()

    val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var webSocketSession: WebSocketSession

    fun init() {
        coroutineScope.launch {
            webSocketSession = httpClient.webSocketSession(fullUrl)
        }
    }

    override fun send(string: String) {
        coroutineScope.launch {
            webSocketSession.send(string)
        }
    }

    override fun shutdown() {
        coroutineScope.launch {
            webSocketSession.close()
        }
    }

    override fun observeIncoming(): Flow<String> = webSocketSession.incoming
        .receiveAsFlow()
        .filter { it is Frame.Text }
        .map { (it as Frame.Text).readText() }

}