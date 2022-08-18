package com.kursor.chroniclesofww2.presentation.ui.fragments.game.localGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.NsdLocalServer
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.CreateAbstractGameFragment
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel

class CreateLocalGameFragment : CreateAbstractGameFragment() {


    override val actionToBattleChooseFragmentId =
        R.id.action_createLocalGameFragment_to_battleChooseFragment
    override val battleViewModel by navGraphViewModels<BattleViewModel>(R.id.navigation_local_game)
    override val gameDataViewModel by viewModels<GameDataViewModel>()

    override fun initServer() {
        if (gameDataJson.isBlank()) return
        localServer = NsdLocalServer(
            requireActivity(),
            settings.username,
            binding.hostPasswordEditText.text.toString(),
            serverListener
        )
        localServer.startListening()
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