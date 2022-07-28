package com.kursor.chroniclesofww2.presentation.ui.fragments.localGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.LocalClient
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.JoinAbstractGameFragment
import org.koin.android.ext.android.inject


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    val settings by inject<Settings>()

    override val actionToPasswordDialogFragmentId: Int =
        R.id.action_joinLocalGameFragment_to_passwordDialogFragment
    override val clientInitErrorMessageResId: Int =
        R.string.client_init_error_message_local

    override fun initClient() {
        client = LocalClient(
            requireActivity(),
            settings.username,
            clientListener
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