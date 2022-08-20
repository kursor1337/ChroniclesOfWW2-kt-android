package com.kursor.chroniclesofww2.connection

import android.net.nsd.NsdServiceInfo
import android.os.Parcel
import android.os.Parcelable
import com.kursor.chroniclesofww2.domain.connection.Host
import java.net.InetAddress
import java.net.UnknownHostException

data class HostImpl(
    override val name: String,
    override val inetAddress: InetAddress,
    override val port: Int
) : Parcelable, Host {

    constructor(nsdServiceInfo: NsdServiceInfo) : this(
        formatName(nsdServiceInfo.serviceName),
        nsdServiceInfo.host,
        nsdServiceInfo.port
    )

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: DUMMY,
        getInetAddressFrom(parcel) ?: LOOPBACK_ADDRESS,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(inetAddress.address.size)
        parcel.writeByteArray(inetAddress.address)
        parcel.writeInt(port)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HostImpl> {
        const val DUMMY = "dummy"
        val LOOPBACK_ADDRESS: InetAddress = InetAddress.getLoopbackAddress()
        const val FILTER_TEXT = ""
        const val JSON_NAME = "name"
        const val JSON_FILTER_TEXT = "filterText"

        override fun createFromParcel(parcel: Parcel): HostImpl {
            return HostImpl(parcel)
        }

        override fun newArray(size: Int): Array<HostImpl?> {
            return arrayOfNulls(size)
        }

        fun getInetAddressFrom(parcel: Parcel): InetAddress? {
            val addressBytes = ByteArray(parcel.readInt())
            parcel.readByteArray(addressBytes)
            return try {
                InetAddress.getByAddress(addressBytes)
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                null
            }
        }

        fun formatName(serviceName: String): String {
            return serviceName.substring(serviceName.indexOf('.') + 1)
        }

    }
}