package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Server
import java.io.*
import java.lang.Thread.sleep
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class LocalServer(
    val activity: Activity,
    override val name: String,
    override val password: String? = null,
    override val sendListener: Connection.SendListener? = null,
    override val receiveListener: Connection.ReceiveListener? = null,
    override val listener: Server.Listener
) : Server {

    private val nsdBroadcast = NsdBroadcast(activity, object : NsdBroadcast.Listener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            listener.onRegistered(Host(serviceInfo))
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
            listener.onFail()
        }
    })

    private val serverSocket = ServerSocket(0) //TODO(dialog onBackPressed)

    private var waiting = false

    override fun startListening() {
        waiting = true
        Thread {
            Log.i("Server", "Thread Started")
            var socket: Socket? = null
            try {
                nsdBroadcast.registerService(name, serverSocket.localPort)
                Log.i("Server", "Listening for connections")
                socket = serverSocket.accept()
                Log.i("Server", "Connection accepted")
                val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                while (waiting) {
                    sleep(50)
                    val name = input.readLine()
                    if (name == null) {
                        Log.i("Server", "Client info not yet obtained")
                        continue
                    }
                    val connection = LocalConnection(
                        input,
                        output,
                        Host(name, socket.inetAddress, socket.port),
                        sendListener,
                        receiveListener
                    )
                    listener.onConnectionEstablished(connection)
                    Log.i("Server", "Server Shutdown")
                    waiting = false
                    break
                }

            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                listener.onListeningStartError(e)
            } /* finally {
                socket?.close()
            }
            */
        }.start()
    }

    override fun stopListening() {
        Log.i("Server", "Stop Listening")
        waiting = false
        nsdBroadcast.unregisterService()
        serverSocket.close()
    }
}