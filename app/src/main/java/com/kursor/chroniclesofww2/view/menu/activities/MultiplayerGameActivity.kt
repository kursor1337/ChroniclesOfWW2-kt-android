package com.kursor.chroniclesofww2.view.menu.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.Const.connection.CLIENT
import com.kursor.chroniclesofww2.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.model.Engine
import com.kursor.chroniclesofww2.model.GameData
import com.kursor.chroniclesofww2.model.board.AddMove
import com.kursor.chroniclesofww2.model.board.Move
import com.kursor.chroniclesofww2.view.menu.fragments.SimpleDialogFragment
import com.kursor.chroniclesofww2.view.menu.hudViews.TileView

class MultiplayerGameActivity : GameActivity() {

    private val PICKING_MOVE = 42
    private val MOTION_MOVE = 558
    private val ADDING_MOVE = 407
    private val SETTING_MOVE = 363
    private val TAG = "GameActivity"


    private var previousMoveType = 0
    private var mode: String? = null

    //tile[0] - previous, tile[1] - current
    protected val previous = 0
    protected val current = 1

    private val connection = Tools.currentConnection!!

    var clickedTileView: TileView? = null

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        val gameData = Tools.GSON.fromJson(
            intent.getStringExtra(Const.game.GAME_DATA),
            GameData::class.java
        )!!
        if (supportActionBar != null) supportActionBar!!.hide()
        val engine = Engine(gameData, engineListener).apply {
            bindViews(
                binding.boardView,
                binding.divisionResourcesMe,
                binding.divisionResourcesEnemy
            )
        }
        //SimpleDialogFragment loadingDialog = showLoadingDialog();
        initializeMultiPlayerFeatures()
        initializeGameFeatures(isInverted())
        binding.boardView.setOnTileClickListener { i, j, tileView, tile ->
            if (tile.isEmpty && clickedTileView == null) return@setOnTileClickListener
            if (tile.isEmpty && binding.divisionResourcesMe.clickedReserveView != null) {
                engine.handleMyMove(
                    AddMove(
                        binding.divisionResourcesMe.clickedReserveView!!.reserve!!, tile
                    )
                )
            }
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

    fun isInverted(): Boolean {
        return mode == CLIENT
    }


    private val receiveListener: Connection.ReceiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            if (string == CANCEL_CONNECTION) {
                Toast.makeText(this@MultiplayerGameActivity, "Disconnected", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return
            }
            processReceivedMove(Move.decodeString(string, engine.getBoard(), hud))
        }
    }

    var tileClickListener =
        View.OnClickListener { view ->
            val tileView = boardLayout.getTileViewByCoordinate(view.id)
            Log.i(TAG, "" + view.id)
            Log.i(TAG, tileView.getTile().toString() + "")
            if (isMotionMoveNow(tileView)) {
                motionMove(tileView)
            } else if (isAddMoveNow(tileView)) {
                addMove(tileView)
            } else if (isPickingClickNow(tileView)) {
                pickingClick(tileView)
            }
        }

    var uiClickListener =
        View.OnClickListener { v ->
            if (chosenControl != null && selectedTileView != null) {
                chosenControl.addDivision(selectedTileView.getDivision())
                boardLayout.hideLegalMoves()
            }
            selectedTileView = null
            chosenControl = hud.getControlByButtonId(v.id)
            Log.i(TAG, "HUD click, type = " + chosenControl.type)
            val row: Int
            row = if (isInverted()) 0 else 7
            boardLayout.showLegalMoves(row)
        }


    fun showLoadingDialog(): SimpleDialogFragment? {
        val simpleDialogFragment: SimpleDialogFragment = Builder(this)
            .setCancelable(true)
            .setMessage("Loading...")
            .build()
        simpleDialogFragment.show(supportFragmentManager, "showLoadingDialog()")
        return simpleDialogFragment
    }

    //init of hud and engine
    //engine and hud are separated now, so gameActivity needs
    //to be the mediator(посредник)
    protected fun initializeGameFeatures(invert: Boolean) {
        mission = getMission()
        if (invert) {
            mission.invertPlayers()
        }
        enemy = mission.getEnemyPlayer()
        me = mission.getMePlayer()
        hud = HUD(enemy, me, this)
        hud.setControlListeners(uiClickListener)
        engine = Engine(this)
    }

    private fun initializeMultiPlayerFeatures() {
        mode = intent.getStringExtra(MULTIPLAYER_GAME_MODE)
        host = intent.getParcelableExtra(CONNECTED_DEVICE)
        if (host != null) {
            connection = Const.connection.getCurrentConnection()
            if (connection == null) {
                Toast.makeText(this, "Some problems", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                connection.setMReceiveListener(receiveListener)
            }
        } else {
            Toast.makeText(this, "Host == null", Toast.LENGTH_SHORT).show()
        }
        Log.i(TAG, "Vse Norm")
    }

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
    protected fun isMotionMoveNow(tileV: TileView): Boolean {
        if (selectedTileView == null || !selectedTileView.isOccupied()) return false
        if (selectedTileView === tileV) return false
        return if (selectedTileView.isOccupied()) {
            if (tileV.getDivision() == null) {
                true
            } else !engine.myOwnership(tileV.getDivision())
        } else false
    }

    protected fun isAddMoveNow(tileV: TileView): Boolean {
        if (tileV.isOccupied()) return false
        if (!(tileV.getRow() === 7 && !isInverted()) && !(tileV.getRow() === 0 && isInverted())) {
            Log.i("GameActivity", "isAddMove" + tileV.getCoordinate())
            return false
        }
        return chosenControl != null && selectedTileView == null
    }

    protected fun isPickingClickNow(tileV: TileView): Boolean {
        if (!tileV.isOccupied()) return false
        if (tileV.getDivision().getKeeper() !== me) return false
        if (tileV === selectedTileView) {
            boardLayout.hideLegalMoves()
            selectedTileView = null
            return false
        }
        if (selectedTileView != null && selectedTileView.getDivision().getKeeper() === me) {
            boardLayout.hideLegalMoves()
            selectedTileView = tileV
            return true
        }
        return chosenControl == null
    }

    protected fun motionMove(tileV: TileView) {
        Log.i(TAG, "Motion move")
        val move = Move(selectedTileView.getTile(), tileV.getTile())
        engine.handleMyMove(move)
        selectedTileView = null
        previousMoveType = MOTION_MOVE
    }

    protected fun addMove(tileV: TileView) {
        Log.i(TAG, "Add move")
        val move = Move(chosenControl, tileV.getTile())
        engine.handleMyMove(move)
        previousMoveType = ADDING_MOVE
        chosenControl = null
    }

    protected fun pickingClick(tileV: TileView) {
        if (chosenControl != null) chosenControl = null
        if (!tileV.isOccupied()) return
        selectedTileView = tileV
        boardLayout.showLegalMoves(selectedTileView.getDivision())
        Log.i(TAG, "Picking move")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connection != null) {
            connection.send(Const.connection.CANCEL_CONNECTION)
            connection.dispose()
        }
        if (Const.connection.getCurrentConnection() != null) {
            Log.i(TAG, "Disposing global connection")
            Const.connection.getCurrentConnection().dispose()
        }
        Const.connection.setCurrentConnection(null)
    }

}