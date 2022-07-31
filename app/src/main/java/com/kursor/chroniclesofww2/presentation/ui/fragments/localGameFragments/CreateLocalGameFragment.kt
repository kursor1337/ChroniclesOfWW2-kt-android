package com.kursor.chroniclesofww2.presentation.ui.fragments.localGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment
import org.koin.android.ext.android.inject

class CreateLocalGameFragment : CreateAbstractGameFragment() {


    override val actionToBattleChooseFragmentId =
        R.id.action_createLocalGameFragment_to_battleChooseFragment

    override fun initServer() {
        if (gameDataJson.isBlank()) return
        server = LocalServer(
            requireActivity(),
            settings.username,
            binding.hostPasswordEditText.text.toString(),
            serverListener
        )
        server.startListening()
        isHostReady = true
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