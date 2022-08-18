package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.os.Handler
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.LocalClient
import com.kursor.chroniclesofww2.connection.interfaces.LocalClient.Listener
import com.kursor.chroniclesofww2.connection.interfaces.println
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.Socket
import java.net.UnknownHostException

class NsdLocalClient(
    activity: Activity,
    private val name: String,
    override val listener: Listener
) : LocalClient {

    override val discoveryListeners = mutableListOf<LocalClient.DiscoveryListener>()

    override val availableHosts = mutableListOf<Host>()

    private val nsdDiscovery = NsdDiscovery(activity, object : NsdDiscovery.Listener {
        override fun onHostFound(host: Host) {
            availableHosts.add(host)
            discoveryListeners.forEach { it.onHostDiscovered(host) }
        }

        override fun onHostLost(host: Host) {
            availableHosts.remove(host)
            discoveryListeners.forEach { it.onHostLost(host) }
        }

        override fun onDiscoveryFailed(errorCode: Int) {
            listener.onFail(errorCode)
        }

        override fun onResolveFailed(errorCode: Int) {
            listener.onFail(errorCode)
        }
    })

    override fun startDiscovery() {
        nsdDiscovery.startDiscovery()
    }

    override fun stopDiscovery() {
        nsdDiscovery.stopDiscovery()
    }

    override fun connectTo(host: Host, password: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val inetAddress = host.inetAddress
            val port = host.port
            val name = host.name
            var socket: Socket? = null
            try {
                Log.i("Client", "Before Connection")
                socket = Socket(inetAddress, port)
                Log.i("Client", "After Connection")
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                output.println(this@NsdLocalClient.name)
                output.flush()
                Log.i("Client", "Sent Client Info")
                val connection =
                    LocalConnection(
                        input,
                        output,
                        host,
                        sendListener = null,
                        Dispatchers.IO
                    ).apply {
                        shutdownListeners.add(Connection.ShutdownListener { socket.close() })
                    }
                Log.i("Client", "Connection established")
                withContext(Dispatchers.Main) {
                    listener.onConnectionEstablished(connection)
                }
                Log.i("Client", "Client shutdown")
            } catch (e: UnknownHostException) {
                Log.e("Client", "UnknownHostException")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Client", e::class.java.name)
                Log.e("Client", "Client Error")
            }
        }
    }
}