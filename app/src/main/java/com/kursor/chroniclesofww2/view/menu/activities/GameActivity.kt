package com.kursor.chroniclesofww2.view.menu.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.databinding.ActivityGameBinding
import com.kursor.chroniclesofww2.model.Engine
import com.kursor.chroniclesofww2.model.GameData
import com.kursor.chroniclesofww2.model.board.Move
import com.kursor.chroniclesofww2.model.board.Tile
import com.kursor.chroniclesofww2.view.menu.fragments.SimpleDialogFragment

abstract class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    val engineListener = object : Engine.Listener {
        override fun onMyMoveComplete(move: Move) {
            notifyEnemy(move)
            binding.boardView.hideLegalMoves()
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
            binding.boardView.hideLegalMoves()
        }

        override fun onMyAddMoveCanceled() {
            binding.boardView.hideLegalMoves()
        }

        override fun onTileClicked(tile: Tile) {
            binding.boardView.calculateAndShowPossibleMoves(
                tile.row,
                tile.column,
                tile.division!!.playerName
            )
        }
    }

    abstract fun notifyEnemy(move: Move)

    lateinit var engine: Engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val gameData = Tools.GSON.fromJson(
            intent.getStringExtra(Const.game.GAME_DATA),
            GameData::class.java
        )!!

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

    protected fun disableScreen() {
        binding.boardView.isEnabled = false
        binding.divisionResourcesMe.isEnabled = false
    }

    protected fun enableScreen() {
        binding.boardView.isEnabled = true
        binding.divisionResourcesMe.isEnabled = true
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