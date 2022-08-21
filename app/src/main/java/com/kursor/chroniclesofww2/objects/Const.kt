package com.kursor.chroniclesofww2.objects

object Const {

    object game {
        const val GAME_DATA = "game data"
        const val MISSION_INFO = "MISSION_INFO"
        const val BATTLE = "mission"
        const val MULTIPLAYER_GAME_MODE = "mode"
        const val BOARD_SIZE = 8
    }

    object connection {

        const val DEFAULT_PORT = 8080

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


        const val SERVER_URL = "10.0.2.2"
        const val PORT = 8080
        const val PROTOCOL = "http"
        const val FULL_SERVER_URL = "$PROTOCOL://$SERVER_URL:$PORT"
    }

}