package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.join

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.adapters.WaitingGameAdapter
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import com.kursor.chroniclesofww2.game.JoinGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.game.join.JoinRemoteGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinRemoteGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId =
        R.id.action_joinRemoteGameFragment_to_passwordDialogFragment

    override val clientInitErrorMessageResId: Int
        get() = R.string.client_init_error_message_remote

    private val joinRemoteGameViewModel by viewModel<JoinRemoteGameViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        joinRemoteGameViewModel.stateLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                JoinGameStatus.ACCEPTED -> {}
                JoinGameStatus.REJECTED -> {
                    Toast.makeText(requireContext(), "Rejected", Toast.LENGTH_LONG).show()
                }
                JoinGameStatus.GAME_DATA_OBTAINED -> {
                    val gameDataJson = arg as String
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.CLIENT)
                        .putExtra(Const.game.BATTLE, gameDataJson)
                    startActivity(intent)
                }
                JoinGameStatus.GAME_LIST_OBTAINED -> {}
                JoinGameStatus.UNAUTHORIZED -> {
                    Toast.makeText(
                        requireContext(),
                        "Login first (in Settings)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        joinRemoteGameViewModel.waitingGamesListLiveData.observe(viewLifecycleOwner) { waitingGamesList ->
            binding.gamesRecyclerView.adapter =
                WaitingGameAdapter(requireActivity(), waitingGamesList).apply {
                    setOnItemClickListener { view, position, waitingGameInfoDTO ->
                        joinRemoteGameViewModel.joinGame(waitingGameInfoDTO.id)
                    }
                }
        }

        binding.findGameByIdEditText.doOnTextChanged { text, start, before, count ->
            joinRemoteGameViewModel.search(text.toString())
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
        if (accountRepository.token != null) {
            joinRemoteGameViewModel.obtainGameList()
        } else {
            Toast.makeText(requireContext(), "Login first (in Settings)", Toast.LENGTH_LONG).show()
        }

    }
}