package com.kursor.chroniclesofww2

import android.util.Log
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object Const {

    object game {
        const val MISSION_INFO = "MISSION_INFO"
        const val SCENARIO = "mission"
        const val MULTIPLAYER_GAME_MODE = "mode"
        const val BOARD_SIZE = 8
    }

    object connection {

        const val DEFAULT_PORT = 1337

        /** Constants for socket communication */
        const val REQUEST_FOR_ACCEPT = "request_for_accept"
        const val REQUEST_SCENARIO_INFO = "request_mission_info"
        const val REJECTED = "rejected"
        const val ACCEPTED = "accepted"
        const val CANCEL_CONNECTION = "cancel_connection"
        const val NOT_RECEIVED = "not received"
        const val INVALID_JSON = "invalid json"

        /** Constants for Activity */
        const val CONNECTED_DEVICE = "connected_device"
        const val HOST = "host"

        const val CLIENT = "Client"
        const val SERVER = "Server"

        const val LOCAL = "local"
        const val WEB = "web"
        const val MULTIPLAYER_TYPE = "multiplayer type"
        const val HOST_IS_WITH_PASSWORD = "host is with password"
        const val PASSWORD = "password"
        const val USERNAME = "username"
    }

}