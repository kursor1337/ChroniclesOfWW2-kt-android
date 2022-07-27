package com.kursor.chroniclesofww2.presentation.ui.fragments.localGameFragments

import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.LocalClient
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.JoinAbstractGameFragment
import org.koin.android.ext.android.inject


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    val settings by inject<Settings>()

    override val actionToPasswordDialogFragmentId: Int
        get() = R.id.action_joinLocalGameFragment_to_passwordDialogFragment

    override fun initClient() {
        client = LocalClient(
            requireActivity(),
            settings.username,
            clientListener
        )

        //(Tools.username, activity, sendListener, receiveListener, clientListener)
    }
}