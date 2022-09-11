package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.create

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.Host
import com.kursor.chroniclesofww2.domain.connection.LocalServer
import com.kursor.chroniclesofww2.game.CreateGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.game.create.CreateLocalGameViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateLocalGameFragment : CreateAbstractGameFragment() {


    override val actionToBattleChooseFragmentId =
        R.id.action_createLocalGameFragment_to_battleChooseFragment
    override val battleViewModel by koinNavGraphViewModel<BattleViewModel>(R.id.navigation_local_game)
    override val gameDataViewModel by viewModel<GameDataViewModel>()
    override val navigationGraphId = R.id.navigation_local_game

    val createLocalGameViewModel by viewModel<CreateLocalGameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLocalGameViewModel.statusLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateGameStatus.CREATED -> {
                    showMessageWaitingForConnections(
                        onCancel = {
                            createLocalGameViewModel.uncreateGame()
                        }
                    )
                }
                CreateGameStatus.REQUEST_FOR_ACCEPT -> {
                    showMessageConnectionRequest(
                        name = (arg as Host).name,
                        onAccept = {
                            createLocalGameViewModel.verdict(ACCEPTED)
                        },
                        onRefuse = {
                            createLocalGameViewModel.verdict(REJECTED)
                        }
                    )
                }
                CreateGameStatus.GAME_DATA_REQUEST -> {
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.GAME_DATA, gameDataJson)
                    startActivity(intent)
                }
                CreateGameStatus.TIMEOUT -> {
                    showReadyButton()
                    Toast.makeText(requireContext(), "Timeout", Toast.LENGTH_LONG).show()
                }
                CreateGameStatus.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Listening start error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }
    }

    override fun createGame() {
        createLocalGameViewModel.createGame(gameDataViewModel.dataContainer())
    }

    override fun checkConditionsForServerInit(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }


}