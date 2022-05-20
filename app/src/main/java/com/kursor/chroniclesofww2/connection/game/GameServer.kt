package com.kursor.chroniclesofww2.connection.game

import android.app.Activity
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Server

abstract class GameServer(
    val activity: Activity,
    val host: Host
) : Server {



}