package com.kursor.chroniclesofww2.view.menu.fragments.localGameFragments

import android.os.Bundle
import android.view.View
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.Server
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.view.menu.fragments.CreateAbstractHostFragment

class CreateLocalHostFragment : CreateAbstractHostFragment() {

    override var _server: Server? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.readyButton.setOnClickListener(View.OnClickListener { v ->
            if (chosenScenarioJson.isBlank()) return@OnClickListener
            _server = LocalServer(
                requireActivity(),
                Tools.username,
                binding.hostPasswordEditText.text.toString(),
                Connection.EMPTY_SEND_LISTENER,
                receiveListener,
                serverListener
            )
            server.startListening()
            v.isEnabled = false
            isHostReady = true
        })
    }


}