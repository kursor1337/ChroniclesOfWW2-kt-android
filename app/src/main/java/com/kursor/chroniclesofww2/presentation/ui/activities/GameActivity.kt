package com.kursor.chroniclesofww2.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.ActivityGameBinding
import com.kursor.chroniclesofww2.model.controllers.Controller
import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.model.game.Reserve
import com.kursor.chroniclesofww2.model.game.board.Tile
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

abstract class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    protected lateinit var controller: Controller

    abstract fun initController(gameData: GameData, listener: Controller.Listener)

    protected val controllerListener = object : Controller.Listener {

        override fun onMotionMoveComplete(motionMove: MotionMove) {
            binding.boardView.hideLegalMoves()
            notifyEnemy(motionMove)
        }

        override fun onAddMoveComplete(addMove: AddMove) {
            binding.boardView.hideLegalMoves()
            notifyEnemy(addMove)
        }

        override fun onEnemyMoveComplete(move: Move) {
            Log.i("EventListener", "onEnemyMoveComplete")
            binding.boardView.hideLegalMoves()
        }

        override fun onMotionMoveCanceled(i: Int, j: Int) {
            binding.boardView.hideLegalMoves()
        }

        override fun onAddMoveCanceled() {
            binding.boardView.hideLegalMoves()
        }

        override fun onReserveClicked(reserve: Reserve, possibleMoves: List<AddMove>) {
            binding.boardView.showPossibleAddMoves(possibleMoves)
        }

        override fun onTileClicked(tile: Tile, possibleMoves: List<MotionMove>) {
            binding.boardView.showPossibleMotionMoves(possibleMoves)
        }

        override fun onGameEnd(meWon: Boolean) {
            buildAlertMessageEndOfTheGame(meWon)
        }

        override fun onStartingSecond() {
            TODO()
        }
    }

    abstract fun notifyEnemy(move: Move)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val gameData =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(GameData::class.java)
                .fromJson(intent.getStringExtra(Const.game.GAME_DATA)!!)!!

        initController(gameData, controllerListener)

        if (supportActionBar != null) supportActionBar!!.hide()

        binding.boardView.setOnTileViewClickListener { i, j, tileView ->
            controller.processTileClick(i, j)
        }

        binding.divisionResourcesMe.setOnReserveClickListener {
            controller.processReserveClick(
                it.reserve!!.type,
                binding.divisionResourcesMe.divisionResources?.playerName
                    ?: return@setOnReserveClickListener
            )
        }
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