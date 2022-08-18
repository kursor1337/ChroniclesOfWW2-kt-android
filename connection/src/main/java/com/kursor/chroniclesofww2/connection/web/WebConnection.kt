package com.kursor.chroniclesofww2.connection.web

import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Connection


class WebConnection() : Connection {
    override var sendListener: Connection.SendListener?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var receiveListeners: MutableList<Connection.ReceiveListener>
        get() = TODO("Not yet implemented")
        set(value) {}
    override val shutdownListeners: MutableList<Connection.ShutdownListener>
        get() = TODO("Not yet implemented")


    override fun send(string: String) {
        TODO("Not yet implemented")
    }

    override fun shutdown() {
        TODO("Not yet implemented")
    }
}