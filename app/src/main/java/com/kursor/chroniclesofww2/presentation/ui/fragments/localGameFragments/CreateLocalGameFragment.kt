package com.kursor.chroniclesofww2.presentation.ui.fragments.localGameFragments

import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment

class CreateLocalGameFragment : CreateAbstractGameFragment() {

    override fun initServer() {
        if (chosenScenarioJson.isBlank()) return
        server = LocalServer(
            requireActivity(),
            Tools.username,
            binding.hostPasswordEditText.text.toString(),
            serverListener
        )
        server.startListening()
        isHostReady = true
    }


}