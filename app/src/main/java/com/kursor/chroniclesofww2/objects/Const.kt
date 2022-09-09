package com.kursor.chroniclesofww2.objects

import com.kursor.chroniclesofww2.features.GameFeaturesMessages

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

        /** Constants for socket communication */
        const val REQUEST_FOR_ACCEPT = GameFeaturesMessages.REQUEST_FOR_ACCEPT
        const val REQUEST_GAME_DATA = GameFeaturesMessages.REQUEST_GAME_DATA
        const val REJECTED = GameFeaturesMessages.REJECTED
        const val ACCEPTED = GameFeaturesMessages.ACCEPTED
        const val CANCEL_CONNECTION = GameFeaturesMessages.CANCEL_CONNECTION
        const val NOT_RECEIVED = GameFeaturesMessages.NOT_RECEIVED
        const val INVALID_JSON = GameFeaturesMessages.INVALID_JSON

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


        const val SERVER_URL = "192.168.31.197"
        const val PORT = 8080
        const val HTTP_SERVER_URL = "http://$SERVER_URL:$PORT"
        const val WEBSOCKET_SERVER_URL = "ws://$SERVER_URL:$PORT"
    }

}