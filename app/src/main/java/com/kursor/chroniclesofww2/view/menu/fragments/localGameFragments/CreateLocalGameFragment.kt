package com.kursor.chroniclesofww2.view.menu.fragments.localGameFragments

import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.Server
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.view.menu.fragments.abstractGameFragment.CreateAbstractGameFragment

class CreateLocalGameFragment : CreateAbstractGameFragment() {

    override fun initServer() {
        if (chosenScenarioJson.isBlank()) return
        server = LocalServer(
            requireActivity(),
            Tools.username,
            binding.hostPasswordEditText.text.toString(),
            Connection.EMPTY_SEND_LISTENER,
            receiveListener,
            serverListener
        )
        server.startListening()
        isHostReady = true
    }


}