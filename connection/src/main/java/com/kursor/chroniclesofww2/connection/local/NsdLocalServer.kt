package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class NsdLocalServer(
    context: Context,
    override var listener: LocalServer.Listener? = null
) : LocalServer {


    private val nsdBroadcast = NsdBroadcast(context, object : NsdBroadcast.Listener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            listener?.onStartedListening()
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
            listener?.onFail()
        }
    })

    private val serverSocket = ServerSocket(0) //TODO(dialog onBackPressed)

    private var waiting = false

    override suspend fun startListening(name: String) {
        withContext(Dispatchers.IO) {
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
                    val connectedName = input.readLine()
                    val connection = LocalConnection(
                        input,
                        output,
                        Host(connectedName, socket.inetAddress, socket.port),
                        sendListener = null,
                        ioDispatcher = Dispatchers.IO
                    ).apply {
                        shutdownListeners.add(Connection.ShutdownListener { socket.close() })
                    }
                    withContext(Dispatchers.Main) {
                        listener?.onConnectionEstablished(connection)
                    }
                    Log.i("Server", "Server Shutdown")
                    waiting = false
                    break
                }

            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { listener?.onListeningStartError(e) }

            }
        }

    }

    override suspend fun stopListening() {
        Log.i("Server", "Stop Listening")
        waiting = false
        nsdBroadcast.unregisterService()
        serverSocket.close()
    }
}