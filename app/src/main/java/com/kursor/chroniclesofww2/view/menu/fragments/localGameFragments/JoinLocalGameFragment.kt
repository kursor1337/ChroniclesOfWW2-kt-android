package com.kursor.chroniclesofww2.view.menu.fragments.localGameFragments

import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.local.LocalClient
import com.kursor.chroniclesofww2.view.menu.fragments.abstractGameFragment.JoinAbstractGameFragment


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    override fun initClient() {
        client = LocalClient(
            requireActivity(),
            Tools.username,
            Connection.EMPTY_SEND_LISTENER,
            receiveListener,
            clientListener
        )

        //(Tools.username, activity, sendListener, receiveListener, clientListener)
    }
}