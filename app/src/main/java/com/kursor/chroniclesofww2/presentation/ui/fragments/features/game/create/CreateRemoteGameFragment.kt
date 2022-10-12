package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.create

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.features.GameFeaturesMessages
import com.kursor.chroniclesofww2.game.CreateGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.features.game.create.CreateRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateRemoteGameFragment : CreateAbstractGameFragment() {
    override val actionToBattleChooseFragmentId =
        R.id.action_createRemoteGameFragment_to_battleChooseFragment3

    override val battleViewModel by koinNavGraphViewModel<BattleViewModel>(R.id.navigation_remote_game)
    override val gameDataViewModel by viewModel<GameDataViewModel>()
    override val navigationGraphId = R.id.navigation_remote_game

    val createRemoteGameViewModel by viewModel<CreateRemoteGameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createRemoteGameViewModel.statusLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateGameStatus.CREATED -> {
                    val gameId = (arg as Int).toString()
                    showMessageWaitingForConnections(
                        argText = "GameId: $gameId",
                        onCancel = {
                            createRemoteGameViewModel.cancelConnection()
                        }
                    )
                }
                CreateGameStatus.TIMEOUT -> {
                    Log.d("CreateRemoteGameFragment", "Timeout")
                    Toast.makeText(
                        requireContext(),
                        R.string.timeout,
                        Toast.LENGTH_LONG
                    ).show()
                    showReadyButton()
                }
                CreateGameStatus.REQUEST_FOR_ACCEPT -> {
                    showMessageConnectionRequest(
                        arg as String,
                        onAccept = {
                            createRemoteGameViewModel.verdict(GameFeaturesMessages.ACCEPTED)
                        },
                        onRefuse = {
                            createRemoteGameViewModel.verdict(GameFeaturesMessages.REJECTED)
                        }
                    )
                }
                CreateGameStatus.GAME_DATA_OBTAINED -> {
                    startActivity(
                        Intent(
                            activity,
                            MultiplayerGameActivity::class.java
                        ).apply {
                            putExtra(Const.game.GAME_DATA, arg as String)
                        }
                    )
                }
                CreateGameStatus.UNCREATED -> {}
                CreateGameStatus.GAME_DATA_REQUEST -> {}
                CreateGameStatus.CANCEL_CONNECTION -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.connection_cancelled,
                        Toast.LENGTH_LONG
                    ).show()
                }
                CreateGameStatus.GAME_START -> {}
                CreateGameStatus.UNAUTHORIZED -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.unauthorized,
                        Toast.LENGTH_LONG
                    ).show()
                }
                CreateGameStatus.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun createGame() {
        createRemoteGameViewModel.createGame(gameDataViewModel.dataContainer())
    }

    override fun onStop() {
        super.onStop()
        Log.d("CreateRemoteGameFragment", "onStop: ")
    }

    override fun checkConditionsForServerInit(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}