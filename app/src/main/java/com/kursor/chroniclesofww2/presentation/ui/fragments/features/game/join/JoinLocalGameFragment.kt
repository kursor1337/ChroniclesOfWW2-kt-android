package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.join

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.adapters.HostAdapter
import com.kursor.chroniclesofww2.game.JoinGameStatus
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.HostDiscoveryViewModel
import com.kursor.chroniclesofww2.viewModels.RecyclerViewViewModelObserver
import com.kursor.chroniclesofww2.viewModels.features.game.join.JoinLocalGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId: Int =
        R.id.action_joinLocalGameFragment_to_passwordDialogFragment
    override val clientInitErrorMessageResId: Int =
        R.string.client_init_error_message_local

    lateinit var hostAdapter: HostAdapter
    val joinLocalGameViewModel by viewModel<JoinLocalGameViewModel>()
    val hostDiscoveryViewModel by viewModel<HostDiscoveryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hostAdapter = HostAdapter(requireActivity(), hostDiscoveryViewModel.hostList).apply {
            setOnItemClickListener { view, position, host ->
                joinLocalGameViewModel.connectTo(host)
            }
        }

        binding.gamesRecyclerView.adapter = hostAdapter

        hostDiscoveryViewModel.observer = object : RecyclerViewViewModelObserver {
            override fun itemInserted(index: Int) {
                hostAdapter.notifyItemInserted(index)
            }

            override fun itemRemoved(index: Int) {
                hostAdapter.notifyItemRemoved(index)
            }
        }

        joinLocalGameViewModel.statusLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                JoinGameStatus.ACCEPTED -> {
                    buildMessageWaitingForAccepted(
                        onPositiveButtonClickListener = null,
                        onNegativeButtonClickListener = { dialog, which ->
                            joinLocalGameViewModel.cancelConnection()
                        },
                        onCancelListener = {
                            joinLocalGameViewModel.cancelConnection()
                        }
                    )
                }
                JoinGameStatus.REJECTED -> {
                    Toast.makeText(
                        activity,
                        R.string.connection_refused,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                JoinGameStatus.GAME_DATA_OBTAINED -> {
                    val gameDataJson = arg as String
                    hostDiscoveryViewModel.stopDiscovery()
                    startActivity(Intent(activity, MultiplayerGameActivity::class.java).apply {
                        putExtra(Const.game.GAME_DATA, gameDataJson)
                    })
                }
                JoinGameStatus.UNAUTHORIZED -> {
                    Toast.makeText(
                        requireContext(),
                        "Login first (in Settings)",
                        Toast.LENGTH_LONG
                    ).show()
                }
                JoinGameStatus.ERROR -> {
                    Toast.makeText(activity, R.string.error_connectiong, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        obtainGamesList()
    }

    override fun checkConditionsForGame(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    override fun obtainGamesList() {
        if (!checkConditionsForGame()) {
            Toast.makeText(requireContext(), clientInitErrorMessageResId, Toast.LENGTH_LONG)
                .show()
            showError()
            return
        }
        hostDiscoveryViewModel.startDiscovery()
        showList()
    }
}