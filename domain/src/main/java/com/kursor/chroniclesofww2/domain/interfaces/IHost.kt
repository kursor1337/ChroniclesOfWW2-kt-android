package com.kursor.chroniclesofww2.domain.interfaces

import java.net.InetAddress

interface IHost {
    val name: String
    val inetAddress: InetAddress
    val port: Int
}