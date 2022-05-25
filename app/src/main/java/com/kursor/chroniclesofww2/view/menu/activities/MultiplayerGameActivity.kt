package com.kursor.chroniclesofww2.view.menu.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.Engine
import com.kursor.chroniclesofww2.model.GameData
import com.kursor.chroniclesofww2.model.board.Move
import com.kursor.chroniclesofww2.view.menu.fragments.SimpleDialogFragment

class MultiplayerGameActivity : GameActivity() {

    private val connection = Tools.currentConnection!!

    override val engineListener = object : Engine.Listener {
        override fun onMyMoveComplete(move: Move) {
            Log.i("EventListener", "onMyMoveComplete")
            connection.send(move.encodeToString())
            disableScreen()
        }

        override fun onEnemyMoveComplete(move: Move) {
            Log.i("EventListener", "onEnemyMoveComplete")
            enableScreen()
        }

        override fun onGameEnd(meWon: Boolean) {
            buildAlertMessageEndOfTheGame(meWon)
        }

        override fun onStartingSecond() {
            disableScreen()
        }

        override fun onMyMotionMoveCanceled(i: Int, j: Int) {
            TODO("Not yet implemented")
        }

        override fun onMyAddMoveCanceled() {
            TODO("Not yet implemented")
        }
    }

    lateinit var engine: Engine

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

//        boardLayout = BoardLayout(sqr, this, engine.getBoard())
//        layout1 = findViewById(R.id.hud1)
//        layout2 = findViewById(R.id.hud2)
//        boardLayout.initializeBoardLayout(isInverted())
//        boardLayout.setClickListeners(getTileClickListener())
//        if (isInverted()) {
//            disableScreen()
//        }
//        //loadingDialog.dismiss();
//        boardLayout.hideLegalMoves()
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


//        View.OnClickListener { v ->
//            if (chosenControl != null && selectedTileView != null) {
//                chosenControl.addDivision(selectedTileView.getDivision())
//                boardLayout.hideLegalMoves()
//            }
//            selectedTileView = null
//            chosenControl = hud.getControlByButtonId(v.id)
//            Log.i(TAG, "HUD click, type = " + chosenControl.type)
//            val row: Int
//            row = if (isInverted()) 0 else 7
//            boardLayout.showLegalMoves(row)
//        }


    fun showLoadingDialog(): SimpleDialogFragment {
        val simpleDialogFragment: SimpleDialogFragment = SimpleDialogFragment.Builder(this)
            .setCancelable(true)
            .setMessage("Loading...")
            .build()
        simpleDialogFragment.show(supportFragmentManager, "showLoadingDialog()")
        return simpleDialogFragment
    }

    //init of hud and engine
    //engine and hud are separated now, so gameActivity needs
    //to be the mediator(посредник)
//    protected fun initializeGameFeatures(invert: Boolean) {
//        mission = getMission()
//        if (invert) {
//            mission.invertPlayers()
//        }
//        enemy = mission.getEnemyPlayer()
//        me = mission.getMePlayer()
//        hud = HUD(enemy, me, this)
//        hud.setControlListeners(uiClickListener)
//        engine = Engine(this)
//    }

    fun buildAlertMessageEndOfTheGame(win: Boolean) {
        val result = if (win) getString(R.string.you_won)
        else getString(R.string.you_lose)
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(this)
            .setMessage(result).setCancelable(false)
            .setPositiveButton("Ок") { dialog, which -> goToMainScreen() }.build()
        dialog.show(supportFragmentManager, "")
    }

    //Event listener helps to receive info from engine
    //Abstract coz single, server and client game activities need to behave differently

    //Event listener helps to receive info from engine
    //Abstract coz single, server and client game activities need to behave differently
    /**
     * abstract class to receive info from the engine
     */
//    abstract class EventListener {
//        fun onGameEnd(win: Boolean) {
//            buildAlertMessageEndOfTheGame(win)
//        }
//
//        //it's for redrawing the board
//        abstract fun onMyMoveComplete(move: Move?)
//        abstract fun onEnemyMoveComplete(move: Move?)
//        fun showText(s: String?) {
//            Toast.makeText(this@GameActivity, s, Toast.LENGTH_SHORT).show()
//        }
//    }

    /*
    Some engine stuff that I decided to move into activity coz idk
     */

    /*
    Some engine stuff that I decided to move into activity coz idk
     */
//    protected fun isMotionMoveNow(tileV: TileView): Boolean {
//        if (selectedTileView == null || !selectedTileView.isOccupied()) return false
//        if (selectedTileView === tileV) return false
//        return if (selectedTileView.isOccupied()) {
//            if (tileV.getDivision() == null) {
//                true
//            } else !engine.myOwnership(tileV.getDivision())
//        } else false
//    }
//
//    protected fun isAddMoveNow(tileV: TileView): Boolean {
//        if (tileV.isOccupied()) return false
//        if (!(tileV.getRow() === 7 && !isInverted()) && !(tileV.getRow() === 0 && isInverted())) {
//            Log.i("GameActivity", "isAddMove" + tileV.getCoordinate())
//            return false
//        }
//        return chosenControl != null && selectedTileView == null
//    }
//
//    protected fun isPickingClickNow(tileV: TileView): Boolean {
//        if (!tileV.isOccupied()) return false
//        if (tileV.getDivision().getKeeper() !== me) return false
//        if (tileV === selectedTileView) {
//            boardLayout.hideLegalMoves()
//            selectedTileView = null
//            return false
//        }
//        if (selectedTileView != null && selectedTileView.getDivision().getKeeper() === me) {
//            boardLayout.hideLegalMoves()
//            selectedTileView = tileV
//            return true
//        }
//        return chosenControl == null
//    }
//
//    protected fun motionMove(tileV: TileView) {
//        Log.i(TAG, "Motion move")
//        val move = Move(selectedTileView.getTile(), tileV.getTile())
//        engine.handleMyMove(move)
//        selectedTileView = null
//        previousMoveType = MOTION_MOVE
//    }
//
//    protected fun addMove(tileV: TileView) {
//        Log.i(TAG, "Add move")
//        val move = Move(chosenControl, tileV.getTile())
//        engine.handleMyMove(move)
//        previousMoveType = ADDING_MOVE
//        chosenControl = null
//    }
//
//    protected fun pickingClick(tileV: TileView) {
//        if (chosenControl != null) chosenControl = null
//        if (!tileV.isOccupied()) return
//        selectedTileView = tileV
//        boardLayout.showLegalMoves(selectedTileView.getDivision())
//        Log.i(TAG, "Picking move")
//    }

    override fun onDestroy() {
        super.onDestroy()
        connection.send(CANCEL_CONNECTION)
        connection.dispose()
        Tools.currentConnection = null
    }

    companion object {
        const val TAG = "GameActivity"
    }

}