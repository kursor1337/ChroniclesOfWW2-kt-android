package com.kursor.chroniclesofww2.connection.web

import android.app.Activity
import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.LocalClient

class WebKtorClient(
    activity: Activity,

    ) : LocalClient {
    override val availableHosts: MutableList<Host>
        get() = TODO("Not yet implemented")
    override val handler: Handler
        get() = TODO("Not yet implemented")
    override val listener: LocalClient.Listener
        get() = TODO("Not yet implemented")
    override val discoveryListeners: MutableList<LocalClient.DiscoveryListener>
        get() = TODO("Not yet implemented")

    override fun startDiscovery() {
        TODO("Not yet implemented")
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun connectTo(host: Host, password: String?) {
        TODO("Not yet implemented")
    }
}