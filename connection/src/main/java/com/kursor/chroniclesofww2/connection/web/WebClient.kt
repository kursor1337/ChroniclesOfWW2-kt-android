package com.kursor.chroniclesofww2.connection.web

import android.app.Activity
import android.os.Handler
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Client

class WebClient(
    activity: Activity,

    ) : Client {
    override val availableHosts: MutableList<Host>
        get() = TODO("Not yet implemented")
    override val handler: Handler
        get() = TODO("Not yet implemented")
    override val listener: Client.Listener
        get() = TODO("Not yet implemented")
    override val discoveryListeners: MutableList<Client.DiscoveryListener>
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