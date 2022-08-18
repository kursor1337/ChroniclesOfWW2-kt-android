package com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Const.connection.CLIENT
import com.kursor.chroniclesofww2.objects.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.objects.Const.connection.HOST_IS_WITH_PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_GAME_DATA
import com.kursor.chroniclesofww2.objects.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.objects.Const.game.BATTLE
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.adapters.HostAdapter
import com.kursor.chroniclesofww2.connection.interfaces.Client
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import com.kursor.chroniclesofww2.domain.interfaces.AccountRepository
import com.kursor.chroniclesofww2.presentation.ui.activities.GameActivity
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

abstract class JoinAbstractGameFragment : BundleFragment() {


    private lateinit var binding: FragmentJoinGameBinding

    var isAccepted = false

    protected lateinit var client: Client

    val hostList = mutableListOf<Host>()
    lateinit var hostAdapter: HostAdapter
    lateinit var host: Host


    abstract val actionToPasswordDialogFragmentId: Int
    abstract val clientInitErrorMessageResId: Int


    val settings by inject<AccountRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clientInitErrorTextView.setText(clientInitErrorMessageResId)

        tryToInitClient()

        binding.retryTextView.setOnClickListener { tryToInitClient() }

        hostAdapter = HostAdapter(requireActivity(), hostList).apply {
            setOnItemClickListener { view, position, host ->
                client.connectTo(host)
            }
        }
        binding.hostsRecyclerView.adapter = hostAdapter
    }

    abstract fun initClient()

    abstract fun checkConditionsForClientInit(): Boolean

    fun tryToInitClient() {
        if (this::client.isInitialized) {
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
        client.discoveryListeners.add(clientDiscoveryListener)
        client.startDiscovery()
        showList()
    }

    fun showError() {
        binding.notConnectedLayout.visibility = View.VISIBLE
        binding.hostsRecyclerView.visibility = View.GONE
    }

    fun showList() {
        binding.notConnectedLayout.visibility = View.GONE
        binding.hostsRecyclerView.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::client.isInitialized) client.stopDiscovery()
    }

    protected val receiveListener: Connection.ReceiveListener =
        object : Connection.ReceiveListener {
            override fun onReceive(string: String) {
                when (string) {
                    ACCEPTED -> {
                        Log.i("Client", ACCEPTED)
                        isAccepted = true
                        Tools.currentConnection!!.send(REQUEST_GAME_DATA)
                        Log.i("Client", REQUEST_GAME_DATA)
                        buildMessageWaitingForAccepted()
                    }
                    REJECTED -> Toast.makeText(
                        activity,
                        R.string.connection_refused,
                        Toast.LENGTH_SHORT
                    ).show()
                    HOST_IS_WITH_PASSWORD -> {
                        navigate(
                            actionToPasswordDialogFragmentId,
                            PASSWORD_REQUEST_ID
                        )
                    }
                    else -> {
                        Log.i("Client", "Default branch")
                        if (isAccepted) {
//                            if (Scenario.fromJson(string) == null) {
//                                Log.i("Client", "Invalid Json")
//                                Tools.currentConnection!!.send(INVALID_JSON)
//                                return
//                            }
                            val intent = Intent(activity, GameActivity::class.java)
                            intent.putExtra(CONNECTED_DEVICE, host)
                                .putExtra(MULTIPLAYER_GAME_MODE, CLIENT)
                                .putExtra(BATTLE, string)
                            client.stopDiscovery()
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onDisconnected() {
                TODO("Not yet implemented")
            }
        }


    override fun onFragmentResult(requestCode: Int, bundle: Bundle) = when (requestCode) {
        PASSWORD_REQUEST_ID -> {
            val password = bundle.getString(PASSWORD)!!
            Tools.currentConnection!!.send("$PASSWORD$password")
        }
        else -> {
        }
    }


    protected val clientListener: Client.Listener = object : Client.Listener {
        override fun onException(e: Exception) {
            Toast.makeText(activity, R.string.error_connectiong, Toast.LENGTH_SHORT).show()
        }

        override fun onFail(errorCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection.apply {
                this.receiveListener = this@JoinAbstractGameFragment.receiveListener
            }
            Tools.currentConnection!!.send(REQUEST_FOR_ACCEPT)
            Log.i("Client", REQUEST_FOR_ACCEPT)
        }
    }

    private val clientDiscoveryListener = object : Client.DiscoveryListener {
        override fun onHostDiscovered(host: Host) {
            hostList.add(host)
            hostAdapter.notifyItemInserted(hostList.lastIndex)
        }

        override fun onHostLost(host: Host) {
            val index = hostList.indexOf(host)
            hostList.removeAt(index)
            hostAdapter.notifyItemRemoved(index)
        }
    }


    private fun buildMessageWaitingForAccepted() {
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(activity)
            .setMessage(R.string.waiting_for_accepted)
            .setNegativeButton(
                R.string.cancel_request_for_accepted
            ) { dialog, which ->
                Tools.currentConnection!!.send(CANCEL_CONNECTION)
                dialog.dismiss()
            }.build()
        dialog.show(parentFragmentManager, "WaitingForAccepted")
    }

    companion object {
        const val PASSWORD_REQUEST_ID = 101
    }

}