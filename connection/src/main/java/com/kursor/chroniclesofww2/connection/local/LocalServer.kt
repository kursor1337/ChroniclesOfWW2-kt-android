package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.util.Log
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.Server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.lang.NullPointerException
import java.lang.Thread.sleep
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class LocalServer(
    activity: Activity,
    override val name: String,
    override val password: String? = null,
    override val listener: Server.Listener
) : Server {

    override val handler = Handler(activity.mainLooper)

    private val nsdBroadcast = NsdBroadcast(activity, object : NsdBroadcast.Listener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            handler.post {
                try {
                    listener.onStartedListening(Host(serviceInfo))
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    listener.onStartedListening()
                }
            }
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
            handler.post {
                listener.onFail()
            }
        }
    })

    private val serverSocket = ServerSocket(0) //TODO(dialog onBackPressed)

    private var waiting = false

    override fun startListening() {
        CoroutineScope(Dispatchers.IO).launch {
            waiting = true
            Log.i("Server", "Thread Started")
            val socket: Socket
            try {
                nsdBroadcast.registerService(name, serverSocket.localPort)
                Log.i("Server", "Listening for connections")
                socket = serverSocket.accept()
                Log.i("Server", "Connection accepted")
                val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                while (waiting) {
                    val name = input.readLine()
                    val connection = LocalConnection(
                        input,
                        output,
                        Host(name, socket.inetAddress, socket.port),
                        sendListener = null,
                        receiveListener = null,
                        handler.looper
                    ).apply {
                        shutdownListeners.add(Connection.ShutdownListener { socket.close() })
                    }
                    handler.post {
                        listener.onConnectionEstablished(connection)
                    }
                    Log.i("Server", "Server Shutdown")
                    waiting = false
                    break
                }

            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { listener.onListeningStartError(e) }

            }
        }

    }

    override fun stopListening() {
        Log.i("Server", "Stop Listening")
        waiting = false
        nsdBroadcast.unregisterService()
        serverSocket.close()
    }
}