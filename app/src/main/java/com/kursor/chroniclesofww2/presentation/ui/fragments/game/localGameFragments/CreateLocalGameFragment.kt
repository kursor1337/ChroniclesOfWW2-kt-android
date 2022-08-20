package com.kursor.chroniclesofww2.presentation.ui.fragments.game.localGameFragments

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
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.CreateAbstractGameFragment
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
    val createLocalGameViewModel by viewModel<CreateLocalGameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isHostReady = true
        createLocalGameViewModel.localServer.listener = serverListener

        createLocalGameViewModel.stateLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateLocalGameViewModel.Status.CREATED -> {
                    buildMessageWaitingForConnections(
                        onPositiveClickListener = null,
                        onNegativeClickListener = { dialog, which ->
                            createLocalGameViewModel.uncreateGame()
                            binding.readyButton.isEnabled = true
                        },
                        onCancelListener = {
                            createLocalGameViewModel.uncreateGame()
                            binding.readyButton.isEnabled = true
                        }
                    )
                }
                CreateLocalGameViewModel.Status.CONNECTION_REQUEST -> {
                    buildMessageConnectionRequest(
                        host = arg as Host,
                        onPositiveClickListener = { dialog, which ->
                            createLocalGameViewModel.verdict(ACCEPTED)
                        },
                        onNegativeClickListener = { dialog, which ->
                            createLocalGameViewModel.verdict(REJECTED)
                        },
                        onCancelListener = {
                            createLocalGameViewModel.verdict(REJECTED)
                        }
                    )
                }
                CreateLocalGameViewModel.Status.GAME_DATA_REQUEST -> {
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.GAME_DATA, gameDataJson)
                    startActivity(intent)
                }
                else -> {}
            }
        }

    }

    override fun createGame() {
        createLocalGameViewModel.createGame(gameDataViewModel.createGameData() ?: return)
    }

    private val serverListener = object : LocalServer.Listener {
        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection
            createLocalGameViewModel.onConnectionInit()
        }

        override fun onListeningStartError(e: Exception) {
            Toast.makeText(activity, "Listening start error", Toast.LENGTH_LONG).show()
        }
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