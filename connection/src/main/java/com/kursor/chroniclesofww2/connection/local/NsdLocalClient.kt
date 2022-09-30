package com.kursor.chroniclesofww2.connection.local

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.domain.connection.LocalClient.Listener
import com.kursor.chroniclesofww2.domain.connection.println
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.UnknownHostException

class NsdLocalClient(
    context: Context,
    looper: Looper,
    override var listener: Listener? = null
) : LocalClient {

    val handler = Handler(looper)

    override val discoveryListeners = mutableListOf<LocalClient.DiscoveryListener>()

    override val availableHosts = mutableListOf<Host>()

    var name = ""

    private val nsdDiscovery = NsdDiscovery(context, object : NsdDiscovery.Listener {
        override fun onHostFound(host: Host) {
            handler.post {
                availableHosts.add(host)
                discoveryListeners.forEach { it.onHostDiscovered(host) }
            }
        }

        override fun onHostLost(host: Host) {
            handler.post {
                availableHosts.remove(host)
                discoveryListeners.forEach { it.onHostLost(host) }
            }
        }

        override fun onDiscoveryFailed(errorCode: Int) {
            handler.post {
                listener?.onFail(errorCode)
            }
        }

        override fun onResolveFailed(errorCode: Int) {
            handler.post {
                listener?.onFail(errorCode)
            }
        }
    })

    override suspend fun startDiscovery() {
        nsdDiscovery.startDiscovery()
    }

    override suspend fun stopDiscovery() {
        nsdDiscovery.stopDiscovery()
    }

    override suspend fun connectTo(host: Host) {
        withContext(Dispatchers.IO) {
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
                        ioDispatcher = Dispatchers.IO
                    ).apply {
                        shutdownListeners.add(Connection.ShutdownListener { socket.close() })
                    }
                Log.i("Client", "Connection established")
                withContext(Dispatchers.Main) {
                    listener?.onConnectionEstablished(connection)
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