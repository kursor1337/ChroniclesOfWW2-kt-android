package com.kursor.chroniclesofww2.connection.web

import android.os.Handler
import com.kursor.chroniclesofww2.connection.interfaces.LocalServer

class WebKtorServer() : LocalServer {
    override val name: String
        get() = TODO("Not yet implemented")
    override val password: String?
        get() = TODO("Not yet implemented")
    override val listener: LocalServer.Listener
        get() = TODO("Not yet implemented")
    override val handler: Handler
        get() = TODO("Not yet implemented")

    override fun startListening() {
        TODO("Not yet implemented")
    }

    override fun stopListening() {
        TODO("Not yet implemented")
    }
}