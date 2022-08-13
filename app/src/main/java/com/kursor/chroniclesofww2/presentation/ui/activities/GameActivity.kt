package com.kursor.chroniclesofww2.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.game.Model
import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.board.Tile
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.hudViews.BoardView
import com.kursor.chroniclesofww2.presentation.hudViews.DivisionResourcesView
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

abstract class GameActivity : AppCompatActivity() {

    //lateinit var binding: ActivityGameBinding

    protected lateinit var controller: Controller

    abstract fun initController(
        gameData: GameData,
        listener: Controller.Listener
    ): Pair<Model, Controller>

    protected val controllerListener = object : Controller.Listener {

        override fun onMotionMoveComplete(motionMove: MotionMove, turn: Int) {
            Log.i(TAG, "onMotionMoveComplete: start")
            boardView.hideLegalMoves()
            notifyEnemy(motionMove)
        }

        override fun onAddMoveComplete(addMove: AddMove, turn: Int) {
            Log.i(TAG, "onAddMoveComplete: ")
            boardView.hideLegalMoves()
            notifyEnemy(addMove)
        }

        override fun onEnemyMoveComplete(move: Move, turn: Int) {
            Log.i(TAG, "onEnemyMoveComplete: ")
            Log.i("EventListener", "onEnemyMoveComplete")
            boardView.hideLegalMoves()
        }

        override fun onMotionMoveCanceled(i: Int, j: Int) {
            Log.i(TAG, "onMotionMoveCanceled: ")
            boardView.hideLegalMoves()
        }

        override fun onAddMoveCanceled() {
            Log.i(TAG, "onAddMoveCanceled: ")
            boardView.hideLegalMoves()
        }

        override fun onReserveClicked(reserve: Reserve, possibleMoves: List<AddMove>) {
            Log.i(TAG, "onReserveClicked: ")
            Log.i(TAG, "onReserveClicked: number of possible moves = ${possibleMoves.size}")
            boardView.showPossibleAddMoves(possibleMoves)
        }

        override fun onTileClicked(tile: Tile, possibleMoves: List<MotionMove>) {
            Log.i(TAG, "onTileClicked: ")
            boardView.showPossibleMotionMoves(possibleMoves)
        }

        override fun onGameEnd(meWon: Boolean) {
            Log.i(TAG, "onGameEnd: ")
            buildAlertMessageEndOfTheGame(meWon)
        }

        override fun onStartingSecond() {
            TODO()
        }
    }

    abstract fun notifyEnemy(move: Move)

    lateinit var boardView: BoardView

    lateinit var divisionResourcesMe: DivisionResourcesView
    lateinit var divisionResourcesEnemy: DivisionResourcesView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        //binding = ActivityGameBinding.inflate(layoutInflater)

        boardView = findViewById(R.id.board_view)
        divisionResourcesMe = findViewById(R.id.division_resources_me)
        divisionResourcesEnemy = findViewById(R.id.division_resources_enemy)
        val gameData =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(GameData::class.java)
                .fromJson(intent.getStringExtra(Const.game.GAME_DATA)!!)!!

        val (model, control) = initController(gameData, controllerListener)
        controller = control
        boardView.board = model.board
        divisionResourcesMe.setup(model.me)
        divisionResourcesEnemy.setup(model.enemy)
        if (supportActionBar != null) supportActionBar!!.hide()

        boardView.setOnTileViewClickListener { i, j, tileView ->
            controller.processTileClick(i, j)
        }

        divisionResourcesMe.setOnReserveClickListener {
            controller.processReserveClick(
                it.reserve!!.type,
                divisionResourcesMe.divisionResources?.playerName
                    ?: return@setOnReserveClickListener
            )
        }
//        root.addView(Button(this).apply {
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            )
//            text = "Fuck"
//        })
    }

    fun showLoadingDialog(): SimpleDialogFragment {
        val simpleDialogFragment: SimpleDialogFragment = SimpleDialogFragment.Builder(this)
            .setCancelable(true)
            .setMessage("Loading...")
            .build()
        simpleDialogFragment.show(supportFragmentManager, "showLoadingDialog()")
        return simpleDialogFragment
    }

    fun buildAlertMessageEndOfTheGame(win: Boolean) {
        val result = if (win) getString(R.string.you_won)
        else getString(R.string.you_lose)
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(this)
            .setMessage(result).setCancelable(false)
            .setPositiveButton("Ок") { dialog, which -> goToMainScreen() }.build()
        dialog.show(supportFragmentManager, "")
    }

    fun goToMainScreen() {
        Log.i(TAG, "Going to main screen")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        System.gc()
        startActivity(intent)
    }

    companion object {
        const val TAG = "GameActivity"
    }

}