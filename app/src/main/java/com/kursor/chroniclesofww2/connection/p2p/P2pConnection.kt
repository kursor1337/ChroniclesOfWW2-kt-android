package com.kursor.chroniclesofww2.connection.p2p

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.kursor.chroniclesofww2.connection.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.println
import java.io.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class P2pConnection(
    private val activity: Activity,
    input: BufferedReader,
    output: BufferedWriter,
    val host: Host,
    override val sendListener: Connection.SendListener,
    override val receiveListener: Connection.ReceiveListener
) : Connection {

    private val sender = Sender(output)
    private val receiver = Receiver(input)

    init {
        Log.i("Connection", "Init connection to ${host.name}; ${host.inetAddress}; ${host.port}")
        receiver.startReceiving()
    }

    override fun send(string: String) {
        sender.send(string)
    }

    override fun dispose() {
        Log.i("Connection", "Disposing")
        receiver.dispose()
        sender.stopSending()
        Log.i("Connection", "Disposed")
    }

    inner class Sender(private val output: BufferedWriter) {

        val messageQueue: BlockingQueue<String> = ArrayBlockingQueue(10)
        private var sending = true

        init {
            SenderThread().start()
        }

        fun send(string: String) {
            messageQueue.put(string)
        }

        private inner class SenderThread : Thread() {

            override fun run() {
                while (sending) {
                    try {
                        val msg = messageQueue.take()
                        sendMessage(msg)
                    } catch (ie: InterruptedException) {
                        Log.d("Sender", "Message sending loop interrupted, exiting")
                    }
                }
            }

            fun sendMessage(string: String) {
                try {
                    Log.e("Sender", "Connected, Sending: $string")
                    output.println(string)
                    output.flush()
                    activity.runOnUiThread { sendListener.onSendSuccess() }
                    Log.e("Sender", "Send Successful: $string")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i("Sender", "_____")
                    activity.runOnUiThread {
                        Toast.makeText(activity, "Message not sent", Toast.LENGTH_SHORT).show()
                        sendListener.onSendFailure(e)
                    }
                    e.printStackTrace()
                }
            }
        }

        fun stopSending() {
            Log.i("Sender", "Stopping")
            sending = false
        }
    }

    inner class Receiver(private val input: BufferedReader) {

        private val receiverThread = ReceiverThread()

        fun startReceiving() {
            receiverThread.start()
        }

        private var receiving = true

        private inner class ReceiverThread : Thread() {
            override fun run() {
                try {
                    while (receiving) {
                        sleep(50)
                        val string = input.readLine() ?: continue
                        Log.e("Receiver", "RECEIVED ==> $string")
                        activity.runOnUiThread { receiveListener.onReceive(string) }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun dispose() {
            Log.i("Receiver", "Disposing")
            try {
                receiving = false
                input.close()
                Log.i("Receiver", "Thread stopped")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}