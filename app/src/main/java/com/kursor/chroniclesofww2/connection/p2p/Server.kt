package com.kursor.chroniclesofww2.connection.p2p

import android.app.Activity
import android.util.Log
import com.kursor.chroniclesofww2.connection.Connection
import com.kursor.chroniclesofww2.connection.Host
import java.io.*
import java.lang.Thread.sleep
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class Server(
    private val name: String,
    private val activity: Activity,
    private val sendListener: Connection.SendListener,
    private val receiveListener: Connection.ReceiveListener,
    private val serverListener: Listener
) {

    private val serverSocket = ServerSocket(0) //TODO(dialog onBackPressed)


    interface Listener {
        fun onConnected(connection: P2pConnection)
        fun onServerInfoObtained(hostName: String, port: Int)
        fun onListeningStartError(e: Exception)
    }

    private var waiting = false

    fun startListening() {
        waiting = true
        Thread {
            Log.i("Server", "Thread Started")
            var socket: Socket? = null
            try {
                activity.runOnUiThread {
                    serverListener.onServerInfoObtained(
                        name,
                        serverSocket.localPort
                    )
                }
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
                    val connection = P2pConnection(
                        activity,
                        input,
                        output,
                        Host(name, socket.inetAddress, socket.port),
                        sendListener,
                        receiveListener
                    )
                    activity.runOnUiThread { serverListener.onConnected(connection) }
                    Log.i("Server", "Server Shutdown")
                    waiting = false
                    break
                }

            } catch (e: SocketException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                activity.runOnUiThread { serverListener.onListeningStartError(e) }
            } /* finally {
                socket?.close()
            }
            */
        }.start()
    }

    fun stopListening() {
        Log.i("Server", "Stop Listening")
        waiting = false
        serverSocket.close()
    }
}