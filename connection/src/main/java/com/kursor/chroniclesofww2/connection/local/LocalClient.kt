package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.os.Handler
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Client
import com.kursor.chroniclesofww2.connection.interfaces.Client.Listener
import com.kursor.chroniclesofww2.connection.interfaces.println
import java.io.*
import java.net.Socket
import java.net.UnknownHostException

class LocalClient(
    activity: Activity,
    private val name: String,
    private val sendListener: Connection.SendListener,
    private val receiveListener: Connection.ReceiveListener,
    private val listener: Listener
) : Client {

    override val availableHosts = mutableListOf<Host>()

    override val handler = Handler(activity.mainLooper)

    val nsdDiscovery = NsdDiscovery(activity, object : NsdDiscovery.Listener {
        override fun onHostFound(host: Host) {
            handler.post {
                availableHosts.add(host)
                listener.onHostDiscovered(host)
            }
        }

        override fun onHostLost(host: Host) {
            handler.post {
                availableHosts.remove(host)
                listener.onHostLost(host)
            }

        }

        override fun onDiscoveryFailed(errorCode: Int) {
            handler.post {
                listener.onFail(errorCode)
            }
        }

        override fun onResolveFailed(errorCode: Int) {
            handler.post {
                listener.onFail(errorCode)
            }

        }

    })

    override fun startDiscovery() {
        nsdDiscovery.startDiscovery()
    }

    override fun stopDiscovery() {
        nsdDiscovery.stopDiscovery()
    }

    override fun connectTo(host: Host, password: String?) {
        val inetAddress = host.inetAddress
        val port = host.port
        val name = host.name
        Thread {
            var socket: Socket? = null
            try {
                Log.i("Client", "Before Connection")
                socket = Socket(inetAddress, port)
                Log.i("Client", "After Connection")
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                output.println(this.name)
                output.flush()
                Log.i("Client", "Sent Client Info")
                val connection =
                    LocalConnection(
                        input,
                        output,
                        host,
                        sendListener,
                        receiveListener,
                        handler.looper
                    )
                Log.i("Client", "Connection established")
                handler.post {
                    listener.onConnectionEstablished(connection)
                }
                Log.i("Client", "Client shutdown")
            } catch (e: UnknownHostException) {
                Log.e("Client", "UnknownHostException")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Client", e::class.java.name)
                Log.e("Client", "Client Error")
            } finally {
                if (socket != null) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        Log.e("Client", "Could Not Close Client")
                        e.printStackTrace()
                    }
                }
            }


        }.start()
    }
}