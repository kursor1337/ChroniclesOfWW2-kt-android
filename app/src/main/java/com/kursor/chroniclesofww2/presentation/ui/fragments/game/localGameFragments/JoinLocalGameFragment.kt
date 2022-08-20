package com.kursor.chroniclesofww2.presentation.ui.fragments.game.localGameFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.local.NsdLocalClient
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.adapters.HostAdapter
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.JoinAbstractGameFragment
import com.kursor.chroniclesofww2.viewModels.HostDiscoveryViewModel
import com.kursor.chroniclesofww2.viewModels.game.JoinLocalGameViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class JoinLocalGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId: Int =
        R.id.action_joinLocalGameFragment_to_passwordDialogFragment
    override val clientInitErrorMessageResId: Int =
        R.string.client_init_error_message_local

    private lateinit var binding: FragmentJoinGameBinding

    var isAccepted = false

    protected lateinit var localClient: LocalClient

    val hostList = mutableListOf<Host>()
    lateinit var hostAdapter: HostAdapter
    lateinit var host: Host
    val joinLocalGameViewModel by viewModel<JoinLocalGameViewModel>()
    val hostDiscoveryViewModel by viewModel<HostDiscoveryViewModel>()

    val settings by inject<AccountRepository>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clientInitErrorTextView.setText(clientInitErrorMessageResId)



        hostAdapter = HostAdapter(requireActivity(), hostList).apply {
            setOnItemClickListener { view, position, host ->
                joinLocalGameViewModel.connectTo(host)
            }
        }
        binding.gamesRecyclerView.adapter = hostAdapter
    }

    override fun initClient() {
        localClient = NsdLocalClient(
            requireActivity(),
            settings.username,
            localClientListener
        )

        //(Tools.username, activity, sendListener, receiveListener, clientListener)
    }

    protected val localClientListener: LocalClient.Listener = object : LocalClient.Listener {
        override fun onException(e: Exception) {
            Toast.makeText(activity, R.string.error_connectiong, Toast.LENGTH_SHORT).show()
        }

        override fun onFail(errorCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection
            Log.i("Client", Const.connection.REQUEST_FOR_ACCEPT)
        }
    }

    override fun checkConditionsForClientInit(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    override fun tryToInitClient() {
        if (this::localClient.isInitialized) {
            Toast.makeText(requireContext(), "Client already initialized", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (!checkConditionsForClientInit()) {
            Toast.makeText(requireContext(), clientInitErrorMessageResId, Toast.LENGTH_LONG)
                .show()
            showError()
            return
        }
        initClient()
        showList()
    }


}