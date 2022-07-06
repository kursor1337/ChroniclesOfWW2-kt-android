package com.kursor.chroniclesofww2.connection.local

import android.app.Activity
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.kursor.chroniclesofww2.connection.interfaces.SERVICE_NAME
import com.kursor.chroniclesofww2.connection.interfaces.SERVICE_TYPE

class NsdBroadcast(
    val nsdManager: NsdManager,
    val broadcastListener: Listener,
) {

    constructor(activity: Activity, broadcastListener: Listener) : this(
        activity.getSystemService(Context.NSD_SERVICE) as NsdManager,
        broadcastListener
    )

    interface Listener {
        fun onServiceRegistered(serviceInfo: NsdServiceInfo)
        fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int)
    }

    private var serviceName = SERVICE_NAME

    private val mNsdRegistrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            Log.i(
                TAG, "onServiceRegistered: ${serviceInfo.port}; " +
                        "${serviceInfo.serviceName}; ${serviceInfo.serviceType} "
            )
            serviceName = serviceInfo.serviceName
            broadcastListener.onServiceRegistered(serviceInfo)
        }

        override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
            broadcastListener.onRegistrationFailed(arg0, arg1)
        }

        override fun onServiceUnregistered(arg0: NsdServiceInfo) {}
        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {}
    }


    fun registerService(name: String, localPort: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            port = localPort
            serviceName = "$serviceName.$name"
            serviceType = SERVICE_TYPE
            Log.i(TAG, "Service registered: $port; $serviceName; $serviceType ")
        }
        nsdManager.registerService(
            serviceInfo,
            NsdManager.PROTOCOL_DNS_SD,
            mNsdRegistrationListener
        )
    }

    fun unregisterService() {
        Log.i(TAG, "Service unregistered")
        serviceName = SERVICE_NAME
        try {
            nsdManager.unregisterService(mNsdRegistrationListener)
        } catch (e: Exception) {
            Log.i(TAG, e.message!!)
        }
    }

    companion object {
        const val TAG = "NsdBroadcast"
    }
}