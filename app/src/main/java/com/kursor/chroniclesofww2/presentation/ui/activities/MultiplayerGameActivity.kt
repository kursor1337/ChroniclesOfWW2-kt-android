package com.kursor.chroniclesofww2.presentation.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.viewModels.game.session.GameSessionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MultiplayerGameActivity : GameActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameSessionViewModel by viewModel<GameSessionViewModel> { parametersOf(gameData) }

        boardView.init(gameSessionViewModel.model.board, gameData.meInitiator)
        divisionResourcesMe.setup(gameSessionViewModel.model.me)
        divisionResourcesEnemy.setup(gameSessionViewModel.model.enemy)

        boardView.setOnTileViewClickListener { i, j, tileView ->
            gameSessionViewModel.controller.processTileClick(i, j)
        }

        divisionResourcesMe.setOnReserveClickListener {
            gameSessionViewModel.controller.processReserveClick(
                it.reserve!!.type,
                it.reserve?.playerName
                    ?: return@setOnReserveClickListener
            )
        }

        gameSessionViewModel.lastMoveLiveData.observe(this) { move ->
            boardView.showLastMove(move)
        }
        gameSessionViewModel.legalMovesLiveData.observe(this) { possibleMoves ->
            if (possibleMoves.isEmpty()) {
                boardView.hideLegalMoves()
                return@observe
            }
            if (possibleMoves[0].type == Move.Type.ADD) {
                boardView.showPossibleAddMoves(possibleMoves.map { it as AddMove })
            } else boardView.showPossibleMotionMoves(possibleMoves.map { it as MotionMove })
        }

        gameSessionViewModel.gameEndLiveData.observe(this) { winner ->
            buildAlertMessageEndOfTheGame(winner.name == gameSessionViewModel.model.me.name)
        }
        gameSessionViewModel.systemMessagesLiveData.observe(this) { string ->
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val TAG = "GameActivity"
    }

}