package com.kursor.chroniclesofww2.view.menu.fragments.localGameFragments

import android.content.DialogInterface
import android.content.Intent
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.Const.connection.HOST
import com.kursor.chroniclesofww2.Const.connection.INVALID_JSON
import com.kursor.chroniclesofww2.Const.connection.REJECTED
import com.kursor.chroniclesofww2.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.Const.connection.REQUEST_SCENARIO_INFO
import com.kursor.chroniclesofww2.Const.game.SCENARIO
import com.kursor.chroniclesofww2.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.Connection.ReceiveListener
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Server
import com.kursor.chroniclesofww2.connection.local.NsdHelper.BroadcastListener
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.connection.local.NsdBroadcast
import com.kursor.chroniclesofww2.databinding.FragmentCreateHostBinding
import com.kursor.chroniclesofww2.model.Scenario
import com.kursor.chroniclesofww2.view.menu.activities.GameActivity
import com.kursor.chroniclesofww2.view.menu.activities.MainActivity
import com.kursor.chroniclesofww2.view.menu.fragments.SimpleDialogFragment

class CreateLocalHostFragment : Fragment() {

    private lateinit var binding: FragmentCreateHostBinding

    val SCENARIO_INFO = "SCENARIO_INFO"

    var currentDialog: DialogFragment? = null

    var chosenScenarioJson = ""
    lateinit var server: Server
    lateinit var connection: Connection
    private var isHostReady = false

    private val receiveListener: ReceiveListener = object : ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    currentDialog?.dismiss()
                    buildMessageConnectionRequest(connection.host)
                }
                REQUEST_SCENARIO_INFO -> {
                    Log.i("Server", "Client sent $REQUEST_SCENARIO_INFO")
                    connection.send(chosenScenarioJson)
                    val intent = Intent(activity as MainActivity, GameActivity::class.java)
                    intent.putExtra(CONNECTED_DEVICE, connection.host)
                        .putExtra(MULTIPLAYER_GAME_MODE, HOST)
                        .putExtra(SCENARIO, chosenScenarioJson)
                    Tools.currentConnection = connection

                    startActivity(intent)
                }
                CANCEL_CONNECTION -> {
                    Log.i("Server", CANCEL_CONNECTION)
                    connection.dispose()
                    Log.i("Server", "Sent invalid json")
                }
                INVALID_JSON -> Log.i("Server", "Sent invalid json")
            }
        }
    }

    private val serverListener: LocalServer.Listener = object : LocalServer.Listener {
        override fun onListeningStartError(e: Exception) {
            Toast.makeText(activity, R.string.error_start_listening, Toast.LENGTH_SHORT).show()
            binding.readyButton.isEnabled = true
        }

        override fun onConnected(connection: LocalConnection) {
            this@CreateLocalHostFragment.connection = connection
        }

        override fun onServerInfoObtained(hostName: String, port: Int) {
            nsdHelper.registerService(hostName, port)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            SCENARIO_INFO, this
        ) { key, bundle ->
            chosenScenarioJson = bundle.getString(SCENARIO) ?: ""
            if (chosenScenarioJson.isBlank()) return@setFragmentResultListener
            val scenario = Scenario.fromJson(chosenScenarioJson)
            binding.chosenScenarioTextView.text = scenario.name
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nsdHelper = NsdHelper(requireActivity(), broadcastListener)
        binding.chooseScenarioButton.setOnClickListener(View.OnClickListener {
            findNavController()
//            menuActivity.changeFragment(
//                JavaMissionFragment(),
//                true,
//                false
//            )
        })
        binding.readyButton.setOnClickListener(View.OnClickListener { v ->
            if (chosenScenarioJson.isBlank()) return@OnClickListener
            server = LocalServer(
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

    private fun buildMessageWaitingForConnections(): SimpleDialogFragment {
        val dialog: SimpleDialogFragment =
            SimpleDialogFragment.Builder(activity).setCancelable(false)
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    nsdHelper.unregisterService()
                    server.stopListening()
                    binding.readyButton.isEnabled = true
                }).setMessage("Waiting for Connections...")
                .setOnCancelListener(DialogInterface.OnCancelListener {
                    nsdHelper.unregisterService()
                    server.stopListening()
                    binding.readyButton.isEnabled = true
                })
                .build()
        dialog.show(parentFragmentManager, "Waiting for connections")
        return dialog
    }

    private fun buildMessageConnectionRequest(host: Host) {
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(activity)
            .setMessage(host.name + " wants to connect to this  device. Do you agree?")
            .setCancelable(false)
            .setNegativeButton("Refuse",
                DialogInterface.OnClickListener { dialog, which -> connection!!.send(REJECTED) })
            .setPositiveButton("Allow",
                DialogInterface.OnClickListener { dialog, which -> connection!!.send(ACCEPTED) })
            .setOnCancelListener(DialogInterface.OnCancelListener { connection!!.send(REJECTED) })
            .build()
        dialog.show(parentFragmentManager, "Waiting for Connected")
        Toast.makeText(activity, R.string.waiting_for_connected, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (server != null) {
            server!!.stopListening()
        }
        nsdHelper!!.shutdown()
    }


}