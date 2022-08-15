package com.kursor.chroniclesofww2.presentation.ui.activities

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.controllers.TwoHostsController
import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Tools

class MultiplayerGameActivity : GameActivity() {


    private val connection = Tools.currentConnection!!

    override fun initController(
        gameData: GameData,
        listener: Controller.Listener
    ): Pair<Model, Controller> {
        val model = Model(gameData)
        return model to TwoHostsController(model, listener)
    }

    override fun notifyEnemy(move: Move) {
        connection.send(move.encodeToString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        connection.receiveListener = receiveListener
//
//        binding.boardView.setOnTileViewClickListener { i, j, tileView ->
//            controller.processTileClick(i, j)
//        }

//        binding.divisionResourcesMe.setOnReserveClickListener {
//            controller.processReserveClick(
//                it.reserve!!.type,
//                binding.divisionResourcesMe.divisionResources!!.playerName
//            )
//        }
    }

    private val receiveListener: Connection.ReceiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            if (string == CANCEL_CONNECTION) {
                Toast.makeText(this@MultiplayerGameActivity, "Disconnected", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return
            }
            (controller as TwoHostsController).processSimplifiedEnemyMove(
                Move.decodeFromStringToSimplified(string)
            )
        }

        override fun onDisconnected() {
            TODO("Not yet implemented")
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