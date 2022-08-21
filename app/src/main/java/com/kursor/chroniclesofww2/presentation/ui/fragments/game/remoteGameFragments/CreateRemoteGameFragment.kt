package com.kursor.chroniclesofww2.presentation.ui.fragments.game.remoteGameFragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.features.GameFeaturesMessages
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.CreateAbstractGameFragment
import com.kursor.chroniclesofww2.viewModels.game.create.CreateRemoteGameViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateRemoteGameFragment : CreateAbstractGameFragment() {
    override val actionToBattleChooseFragmentId =
        R.id.action_createRemoteGameFragment_to_battleChooseFragment3

    override val battleViewModel by koinNavGraphViewModel<BattleViewModel>(R.id.navigation_remote_game)
    override val gameDataViewModel by viewModel<GameDataViewModel>()
    val createRemoteGameViewModel by viewModel<CreateRemoteGameViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRemoteGameViewModel.stateLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateRemoteGameViewModel.Status.CREATED -> {
                    buildMessageWaitingForConnections(
                        onPositiveClickListener = null,
                        onNegativeClickListener = { dialog, which ->
                            createRemoteGameViewModel.cancelConnection()
                        },
                        onCancelListener = {
                            createRemoteGameViewModel.cancelConnection()
                        }
                    )
                }
                CreateRemoteGameViewModel.Status.CONNECTED -> {}
                CreateRemoteGameViewModel.Status.TIMEOUT -> {
                    Toast.makeText(requireContext(), "Timeout", Toast.LENGTH_LONG).show()
                }
                CreateRemoteGameViewModel.Status.REQUEST_FOR_ACCEPT -> {
                    buildMessageConnectionRequest(
                        arg as String,
                        onPositiveClickListener = { dialog, which ->
                            createRemoteGameViewModel.verdict(GameFeaturesMessages.ACCEPTED)
                            val gameDataJson = arg
                            val intent = Intent(activity, MultiplayerGameActivity::class.java)
                            intent.putExtra(
                                Const.game.MULTIPLAYER_GAME_MODE,
                                Const.connection.CLIENT
                            )
                                .putExtra(Const.game.BATTLE, gameDataJson)
                            startActivity(intent)
                        },
                        onNegativeClickListener = { dialog, which ->
                            createRemoteGameViewModel.verdict(GameFeaturesMessages.REJECTED)
                        },
                        onCancelListener = {
                            createRemoteGameViewModel.verdict(GameFeaturesMessages.REJECTED)
                        }
                    )
                }
            }
        }

    }


    override fun createGame() {
        createRemoteGameViewModel.createGame(gameDataViewModel)
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