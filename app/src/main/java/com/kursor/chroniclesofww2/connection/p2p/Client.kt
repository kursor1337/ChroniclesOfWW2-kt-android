package com.kursor.chroniclesofww2.connection.p2p

import android.app.Activity
import android.util.Log
import com.kursor.chroniclesofww2.connection.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.println
import java.io.*
import java.net.Socket
import java.net.UnknownHostException

class Client(
    private val name: String,
    private val activity: Activity,
    private val sendListener: Connection.SendListener,
    private val receiveListener: Connection.ReceiveListener,
    private val listener: Listener
) {

    interface Listener {
        fun onConnectionEstablished(connection: P2pConnection)
        fun onException(e: Exception)
    }

    fun connectTo(host: Host) {
        val inetAddress = host.inetAddress
        val port = host.port
        val name = host.name
        Thread {
            var socket: Socket? = null
            try {
                Log.i("Client", "Before Connection")
                socket = Socket(inetAddress, port)
                Log.i("Client", "After Connection")
                Log.i("Client", "Sent Client Info")
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                output.println(name)
                output.flush()
                val connection =
                    P2pConnection(activity, input, output, host, sendListener, receiveListener)
                Log.i("Client", "Connection established")
                activity.runOnUiThread { listener.onConnectionEstablished(connection) }
                Log.i("Client", "Client shutdown")
            } catch (e: UnknownHostException) {
                Log.e("Client", "UnknownHostException")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Client", e::class.java.name)
                Log.e("Client", "Client Error")
            }
            /*
            finally {
                if (socket != null) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        Log.e("Client", "Could Not Close Client")
                        e.printStackTrace()
                    }
                }
            }

             */
        }.start()
    }

}