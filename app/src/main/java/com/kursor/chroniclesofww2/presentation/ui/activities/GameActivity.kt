package com.kursor.chroniclesofww2.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.presentation.hudViews.BoardView
import com.kursor.chroniclesofww2.presentation.hudViews.DivisionResourcesView
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment

abstract class GameActivity : AppCompatActivity() {

    //lateinit var binding: ActivityGameBinding

    private lateinit var analytics: FirebaseAnalytics

    lateinit var boardView: BoardView
    lateinit var divisionResourcesMe: DivisionResourcesView
    lateinit var divisionResourcesEnemy: DivisionResourcesView

    lateinit var gameData: GameData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)
        analytics = Firebase.analytics
        //binding = ActivityGameBinding.inflate(layoutInflater)
        if (supportActionBar != null) supportActionBar!!.hide()
        boardView = findViewById(R.id.board_view)
        divisionResourcesMe = findViewById(R.id.division_resources_me)
        divisionResourcesEnemy = findViewById(R.id.division_resources_enemy)
        gameData = Moshi.GAMEDATA_ADAPTER
            .fromJson(intent.getStringExtra(Const.game.GAME_DATA)!!)!!

//        boardView.board = model.board
//        divisionResourcesMe.setup(model.me)
//        divisionResourcesEnemy.setup(model.enemy)


//        boardView.setOnTileViewClickListener { i, j, tileView ->
//            controller.processTileClick(i, j)
//        }
//
//        divisionResourcesMe.setOnReserveClickListener {
//            controller.processReserveClick(
//                it.reserve!!.type,
//                divisionResourcesMe.divisionResources?.playerName
//                    ?: return@setOnReserveClickListener
//            )
//        }
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
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        System.gc()
        startActivity(intent)
    }

    companion object {
        const val TAG = "GameActivity"
    }

}