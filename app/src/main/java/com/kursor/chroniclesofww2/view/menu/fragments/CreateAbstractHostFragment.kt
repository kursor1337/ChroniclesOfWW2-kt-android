package com.kursor.chroniclesofww2.view.menu.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Server
import com.kursor.chroniclesofww2.connection.local.LocalConnection
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.databinding.FragmentCreateHostBinding
import com.kursor.chroniclesofww2.model.Scenario
import com.kursor.chroniclesofww2.view.menu.activities.GameActivity
import com.kursor.chroniclesofww2.view.menu.activities.MainActivity

abstract class CreateAbstractHostFragment : Fragment() {

    lateinit var binding: FragmentCreateHostBinding

    val SCENARIO_INFO = "SCENARIO_INFO"

    var currentDialog: DialogFragment? = null

    var chosenScenarioJson = ""
    lateinit var server: Server
    lateinit var connection: Connection
    private var isHostReady = false

    private val receiveListener: Connection.ReceiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                Const.connection.REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    currentDialog?.dismiss()
                    buildMessageConnectionRequest(connection.host)
                }
                Const.connection.REQUEST_SCENARIO_INFO -> {
                    Log.i("Server", "Client sent ${Const.connection.REQUEST_SCENARIO_INFO}")
                    connection.send(chosenScenarioJson)
                    val intent = Intent(activity as MainActivity, GameActivity::class.java)
                    intent.putExtra(Const.connection.CONNECTED_DEVICE, connection.host)
                        .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.SCENARIO, chosenScenarioJson)
                    Tools.currentConnection = connection
                    server.stopListening()
                    startActivity(intent)
                }
                Const.connection.CANCEL_CONNECTION -> {
                    Log.i("Server", Const.connection.CANCEL_CONNECTION)
                    connection.dispose()
                    Log.i("Server", "Sent invalid json")
                }
                Const.connection.INVALID_JSON -> Log.i("Server", "Sent invalid json")
            }
        }
    }

    private val serverListener = object : Listener

//    private val serverListener: LocalServer.Listener = object : LocalServer.Listener {
//        override fun onListeningStartError(e: Exception) {
//            Toast.makeText(activity, R.string.error_start_listening, Toast.LENGTH_SHORT).show()
//            binding.readyButton.isEnabled = true
//        }
//
//        override fun onConnected(connection: LocalConnection) {
//            this@CreateLocalHostFragment.connection = connection
//        }
//
//        override fun onServerInfoObtained(hostName: String, port: Int) {
//            nsdHelper.registerService(hostName, port)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            SCENARIO_INFO, this
        ) { key, bundle ->
            chosenScenarioJson = bundle.getString(Const.game.SCENARIO) ?: ""
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
                DialogInterface.OnClickListener { dialog, which -> connection!!.send(Const.connection.REJECTED) })
            .setPositiveButton("Allow",
                DialogInterface.OnClickListener { dialog, which -> connection!!.send(Const.connection.ACCEPTED) })
            .setOnCancelListener(DialogInterface.OnCancelListener { connection!!.send(Const.connection.REJECTED) })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}