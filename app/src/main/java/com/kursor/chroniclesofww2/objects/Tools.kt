package com.kursor.chroniclesofww2.objects

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object Tools {

    var currentConnection: Connection? = null

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

    fun getSelfIpAddress(): String {
        var selfIp = ""
        try {
            val enumNetworkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = enumNetworkInterfaces.nextElement()
                val enumInetAddress: Enumeration<InetAddress> = networkInterface.inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress: InetAddress = enumInetAddress
                        .nextElement()
                    if (inetAddress.isSiteLocalAddress) {
                        selfIp = inetAddress.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            Log.e("GET_IP", "IP NOT FOUND")
        }
        return selfIp
    }

    fun validIP(ip: String?): Boolean {
        return if (ip == null || ip.length < 7 || ip.length > 15) false
        else try {
            var x = 0
            var y = ip.indexOf('.')
            if (y == -1 || ip[x] == '-' || ip.substring(x, y).toInt() > 255) return false
            x = ip.indexOf('.', ++y)
            if (x == -1 || ip[y] == '-' || ip.substring(y, x).toInt() > 255) return false
            y = ip.indexOf('.', ++x)
            !(y == -1 || ip[x] == '-' || ip.substring(x, y).toInt() > 255 || ip[++y] == '-' || ip.substring(y, ip.length).toInt() > 255 || ip[ip.length - 1] == '.')
        } catch (e: Exception) {
            false
        }
    }

}
