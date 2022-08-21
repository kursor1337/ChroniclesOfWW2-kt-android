package com.kursor.chroniclesofww2.presentation.ui.fragments.game.webGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import com.kursor.chroniclesofww2.R
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


        createRemoteGameViewModel.stateLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateRemoteGameViewModel.Status.CREATED -> {
                    buildMessageWaitingForConnections(
                        onPositiveClickListener = null,
                        onNegativeClickListener = { dialog, which ->

                        },
                        onCancelListener = {

                        }
                    )
                }
                CreateRemoteGameViewModel.Status.CONNECTED -> {
                    bui
                }
                CreateRemoteGameViewModel.Status.TIMEOUT -> {

                }
            }
        }

    }


    override fun createGame() {
        val battle = battleViewModel.battleLiveData.value ?: return
        val gameData = gameDataViewModel.createGameData() ?: return
        createRemoteGameViewModel.createGame(battle, gameData.boardWidth, gameData.boardHeight)
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