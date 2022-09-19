package com.kursor.chroniclesofww2.presentation.ui.activities

import android.os.Bundle
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.viewModels.features.game.session.SingleHostGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SinglePlayerGameActivity : GameActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val singleHostGameViewModel by viewModel<SingleHostGameViewModel> { parametersOf(gameData) }

        boardView.init(singleHostGameViewModel.model.board, true)
        divisionResourcesMe.setup(singleHostGameViewModel.model.me)
        divisionResourcesEnemy.setup(singleHostGameViewModel.model.enemy)


        boardView.setOnTileViewClickListener { i, j, tileView ->
            singleHostGameViewModel.controller.processTileClick(i, j)
        }

        divisionResourcesMe.setOnReserveClickListener {
            singleHostGameViewModel.controller.processReserveClick(
                it.reserve!!.type,
                divisionResourcesMe.divisionResources?.playerName
                    ?: return@setOnReserveClickListener
            )
        }

        divisionResourcesEnemy.setOnReserveClickListener {
            singleHostGameViewModel.controller.processReserveClick(
                it.reserve!!.type,
                it.reserve?.playerName
                    ?: return@setOnReserveClickListener
            )
        }

        singleHostGameViewModel.lastMoveLiveData.observe(this) { move ->
            boardView.showLastMove(move)
        }
        singleHostGameViewModel.legalMovesLiveData.observe(this) { possibleMoves ->
            if (possibleMoves.isEmpty()) {
                boardView.hideLegalMoves()
                return@observe
            }
            if (possibleMoves[0].type == Move.Type.ADD) {
                boardView.showPossibleAddMoves(possibleMoves.map { it as AddMove })
            } else boardView.showPossibleMotionMoves(possibleMoves.map { it as MotionMove })
        }

        singleHostGameViewModel.gameEndLiveData.observe(this) { winner ->
            buildAlertMessageEndOfTheGame(winner.name == singleHostGameViewModel.model.me.name)
        }
    }
}