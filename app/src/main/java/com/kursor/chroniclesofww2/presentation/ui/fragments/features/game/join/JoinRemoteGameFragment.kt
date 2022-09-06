package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.join

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.adapters.WaitingGameAdapter
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.game.join.JoinRemoteGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinRemoteGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId =
        R.id.action_joinRemoteGameFragment_to_passwordDialogFragment

    override val clientInitErrorMessageResId: Int
        get() = TODO("Not yet implemented")

    val joinRemoteGameViewModel by viewModel<JoinRemoteGameViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        joinRemoteGameViewModel.stateLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                JoinRemoteGameViewModel.Status.ACCEPTED -> {}
                JoinRemoteGameViewModel.Status.REJECTED -> {
                    Toast.makeText(requireContext(), "Rejected", Toast.LENGTH_LONG).show()
                }
                JoinRemoteGameViewModel.Status.GAME_DATA_OBTAINED -> {
                    val gameDataJson = arg as String
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.CLIENT)
                        .putExtra(Const.game.BATTLE, gameDataJson)
                    startActivity(intent)
                }
                JoinRemoteGameViewModel.Status.GAME_LIST_OBTAINED -> {
                    val list = arg as List<WaitingGameInfoDTO>
                    binding.gamesRecyclerView.adapter = WaitingGameAdapter(requireActivity(), list)
                }
            }
        }

    }


    override fun checkConditionsForGame(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    override fun obtainGamesList() {
        joinRemoteGameViewModel.obtainGameList()
    }
}