package com.kursor.chroniclesofww2.presentation.ui.activities

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.Engine
import com.kursor.chroniclesofww2.model.GameData
import com.kursor.chroniclesofww2.model.board.moves.Move

class MultiplayerGameActivity : GameActivity() {

    private val connection = Tools.currentConnection!!

    override fun notifyEnemy(move: Move) {
        connection.send(move.encodeToString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        val gameData = Tools.GSON.fromJson(
            intent.getStringExtra(Const.game.GAME_DATA),
            GameData::class.java
        )!!

        connection.receiveListener = receiveListener

        if (supportActionBar != null) supportActionBar!!.hide()
        engine = Engine(gameData, engineListener).apply {
            bindViews(
                binding.boardView,
                binding.divisionResourcesMe,
                binding.divisionResourcesEnemy
            )
        }

        binding.boardView.setOnTileViewClickListener { i, j, tileView ->
            engine.processTileClick(i, j)
        }

        binding.divisionResourcesMe.setOnReserveClickListener {
            engine.processReserveClick(it.reserve!!.type)
        }
    }

    private val receiveListener: Connection.ReceiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            if (string == CANCEL_CONNECTION) {
                Toast.makeText(this@MultiplayerGameActivity, "Disconnected", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return
            }
            engine.handleEnemyMove(
                Move.decodeFromStringToSimplified(string).returnToFullState(engine)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.send(CANCEL_CONNECTION)
        connection.shutdown()
        Tools.currentConnection = null
    }

    companion object {
        const val TAG = "GameActivity"
    }

}