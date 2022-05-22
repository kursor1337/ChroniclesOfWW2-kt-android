package com.kursor.chroniclesofww2.view.menu.fragments.abstractGameFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kursor.chroniclesofww2.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.Const.connection.CLIENT
import com.kursor.chroniclesofww2.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.Const.connection.HOST_IS_WITH_PASSWORD
import com.kursor.chroniclesofww2.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.Const.connection.REJECTED
import com.kursor.chroniclesofww2.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.Const.connection.REQUEST_SCENARIO_INFO
import com.kursor.chroniclesofww2.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.Const.game.SCENARIO
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.HostAdapter
import com.kursor.chroniclesofww2.connection.interfaces.Client
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import com.kursor.chroniclesofww2.view.menu.activities.GameActivity
import com.kursor.chroniclesofww2.view.menu.fragments.SimpleDialogFragment
import com.phelat.navigationresult.BundleFragment

abstract class JoinAbstractGameFragment : BundleFragment() {

    private lateinit var binding: FragmentJoinGameBinding

    var isAccepted = false

    protected lateinit var client: Client

    val hostList = mutableListOf<Host>()
    lateinit var hostAdapter: HostAdapter
    lateinit var host: Host

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

        initClient()

        client.startDiscovery()
        hostAdapter = HostAdapter(requireActivity(), hostList).apply {
            setOnItemClickListener { view, position, host ->
                client.connectTo(host)
            }
        }
        binding.hostsRecyclerView.adapter = hostAdapter
    }

    abstract fun initClient()

    override fun onDestroy() {
        super.onDestroy()
        client.stopDiscovery()
    }

    protected val receiveListener: Connection.ReceiveListener =
        Connection.ReceiveListener { string ->
            when (string) {
                ACCEPTED -> {
                    Log.i("Client", ACCEPTED)
                    isAccepted = true
                    Tools.currentConnection!!.send(REQUEST_SCENARIO_INFO)
                    Log.i("Client", REQUEST_SCENARIO_INFO)
                    buildMessageWaitingForAccepted()
                }
                REJECTED -> Toast.makeText(
                    activity,
                    R.string.connection_refused,
                    Toast.LENGTH_SHORT
                ).show()
                HOST_IS_WITH_PASSWORD -> {
                    navigate(
                        R.id.action_joinLocalGameFragment_to_passwordDialogFragment,
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
                            .putExtra(SCENARIO, string)
                        client.stopDiscovery()
                        startActivity(intent)
                    }
                }
            }
        }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) = when (requestCode) {
        PASSWORD_REQUEST_ID -> {
            val password = bundle.getString(PASSWORD)!!
            Tools.currentConnection!!.send("$PASSWORD$password")
        }
        else -> {}
    }


    protected val clientListener: Client.Listener = object : Client.Listener {
        override fun onException(e: Exception) {
            Toast.makeText(activity, R.string.error_connectiong, Toast.LENGTH_SHORT).show()
        }

        override fun onHostDiscovered(host: Host) {
            hostList.add(host)
            hostAdapter.notifyItemInserted(hostList.lastIndex)
        }

        override fun onHostLost(host: Host) {
            val index = hostList.indexOf(host)
            hostList.removeAt(index)
            hostAdapter.notifyItemRemoved(index)
        }

        override fun onFail(errorCode: Int) {
            TODO("Not yet implemented")
        }

        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection
            Tools.currentConnection!!.send(REQUEST_FOR_ACCEPT)
            Log.i("Client", REQUEST_FOR_ACCEPT)
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