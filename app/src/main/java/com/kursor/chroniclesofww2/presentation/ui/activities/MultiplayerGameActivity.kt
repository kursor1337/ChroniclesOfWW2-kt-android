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
import com.kursor.chroniclesofww2.viewModels.GameSessionViewModel
import io.ktor.http.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MultiplayerGameActivity : GameActivity() {


    private val connection = Tools.currentConnection!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_game)

        val gameSessionViewModel by viewModel<GameSessionViewModel> { parametersOf(gameData) }
    }

    companion object {
        const val TAG = "GameActivity"
    }

}