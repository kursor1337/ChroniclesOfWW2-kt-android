package com.kursor.chroniclesofww2.view.menu.fragments.localGameFragments

import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Client
import com.kursor.chroniclesofww2.connection.local.LocalClient
import com.kursor.chroniclesofww2.view.menu.fragments.JoinAbstractGameFragment


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    override var _client: Client? = null

    override fun initClient() {
        _client = LocalClient(
            requireActivity(),
            Tools.username,
            sendListener,
            receiveListener,
            clientListener
        )

        //(Tools.username, activity, sendListener, receiveListener, clientListener)
    }
}