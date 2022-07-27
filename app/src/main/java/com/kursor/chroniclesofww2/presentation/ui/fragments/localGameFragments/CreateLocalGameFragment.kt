package com.kursor.chroniclesofww2.presentation.ui.fragments.localGameFragments

import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment
import org.koin.android.ext.android.inject

class CreateLocalGameFragment : CreateAbstractGameFragment() {

    val settings by inject<Settings>()

    override val actionToBattleChooseFragmentId =
        R.id.action_createLocalGameFragment_to_battleChooseFragment

    override fun initServer() {
        if (chosenScenarioJson.isBlank()) return
        server = LocalServer(
            requireActivity(),
            settings.username,
            binding.hostPasswordEditText.text.toString(),
            serverListener
        )
        server.startListening()
        isHostReady = true
    }


}