package com.kursor.chroniclesofww2.presentation.ui.fragments.game.localGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.NsdLocalClient
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.JoinAbstractGameFragment


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId: Int =
        R.id.action_joinLocalGameFragment_to_passwordDialogFragment
    override val clientInitErrorMessageResId: Int =
        R.string.client_init_error_message_local

    override fun initClient() {
        localClient = NsdLocalClient(
            requireActivity(),
            settings.username,
            localClientListener
        )

        //(Tools.username, activity, sendListener, receiveListener, clientListener)
    }

    override fun checkConditionsForClientInit(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }


}