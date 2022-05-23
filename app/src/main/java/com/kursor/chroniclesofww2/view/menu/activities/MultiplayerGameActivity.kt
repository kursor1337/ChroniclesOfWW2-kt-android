package com.kursor.chroniclesofww2.view.menu.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.Toast
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.view.menu.gameUiViews.TileView

class MultiplayerGameActivity : GameActivity() {

    private val PICKING_MOVE = 42
    private val MOTION_MOVE = 558
    private val ADDING_MOVE = 407
    private val SETTING_MOVE = 363
    private val TAG = "GameActivity"

    private val receiveListener: Connection.ReceiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            if (string == CANCEL_CONNECTION) {
                Toast.makeText(this@MultiplayerGameActivity, "Disconnected", Toast.LENGTH_SHORT).show()
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

    private var previousMoveType = 0
    private var mode: String? = null

    protected val previous = 0
    protected val current = 1

    protected var selectedTileView: TileView? = null
    protected var chosenControl: HUD.Control? = null

    //tile[0] - previous, tile[1] - current

    //tile[0] - previous, tile[1] - current
    var layout1: LinearLayout? = null
    var layout2: LinearLayout? = null
    var hud: HUD? = null
    var mission: Mission? = null
    var enemy: Player? = null
    var me: Player? = null
    var engine: Engine? = null
    var boardLayout: BoardLayout? = null

    var eventListener: EventListener = object : EventListener() {
        override fun onMyMoveComplete(move: Move) {
            Log.i("EventListener", "onMyMoveComplete")
            connection.send(move.toCode())
            disableScreen()
            boardLayout.hideLegalMoves()
        }

        override fun onEnemyMoveComplete(move: Move?) {
            Log.i("EventListener", "onEnemyMoveComplete")
            enableScreen()
        }
    }

    protected fun disableScreen() {
        boardLayout.setEnabled(false)
        hud.getMyInterface().disable()
    }

    protected fun enableScreen() {
        boardLayout.setEnabled(true)
        hud.getMyInterface().enable()
    }

    protected fun getMission(): Mission? {
        return Mission.fromJson(intent.getStringExtra(MISSION))
    }

    fun goToMainScreen() {
        Log.i(TAG, "Going to main screen")
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        System.gc()
        startActivity(intent)
    }

    fun isInverted(): Boolean {
        return mode == CLIENT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        if (supportActionBar != null) supportActionBar!!.hide()

        //SimpleDialogFragment loadingDialog = showLoadingDialog();
        initializeMultiPlayerFeatures()
        initializeGameFeatures(isInverted())
        val sqr = findViewById<TableLayout>(R.id.table)
        boardLayout = BoardLayout(sqr, this, engine.getBoard())
        layout1 = findViewById(R.id.hud1)
        layout2 = findViewById(R.id.hud2)
        boardLayout.initializeBoardLayout(isInverted())
        boardLayout.setClickListeners(getTileClickListener())
        if (isInverted()) {
            disableScreen()
        }
        //loadingDialog.dismiss();
        boardLayout.hideLegalMoves()
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
        val result: String
        result = if (win) {
            getString(R.string.you_won)
        } else {
            getString(R.string.you_lose)
        }
        val dialog: SimpleDialogFragment = Builder(this)
            .setMessage(result).setCancelable(false)
            .setPositiveButton("Ок",
                DialogInterface.OnClickListener { dialog, which -> goToMainScreen() }).build()
        dialog.show(supportFragmentManager, "")
    }

    fun getEnemy(): Player? {
        return enemy
    }

    fun getMe(): Player? {
        return me
    }

    //Event listener helps to receive info from engine
    //Abstract coz single, server and client game activities need to behave differently

    //Event listener helps to receive info from engine
    //Abstract coz single, server and client game activities need to behave differently
    /**
     * abstract class to receive info from the engine
     */
    abstract class EventListener {
        fun onGameEnd(win: Boolean) {
            buildAlertMessageEndOfTheGame(win)
        }

        //it's for redrawing the board
        abstract fun onMyMoveComplete(move: Move?)
        abstract fun onEnemyMoveComplete(move: Move?)
        fun showText(s: String?) {
            Toast.makeText(this@GameActivity, s, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun processReceivedMove(move: Move?) {
        Log.i(TAG, "Process received move")
        engine.handleEnemyMove(move)
    }

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