package com.kursor.chroniclesofww2.view.menu.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.interfaces.Client
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import java.util.*

abstract class JoinAbstractGameFragment : Fragment() {

    private lateinit var binding: FragmentJoinGameBinding

    var isAccepted = false
    abstract var _client: Client?

    val client: Client get() = _client!!
    var hostListView: ListView? = null
    var hostMap: MutableMap<String, Host> = TreeMap<String, Host>()
    var hostAdapter: HostsAdapter? = null
    var host: Host? = null
    var statusTextView: TextView? = null
    var clientNameEditText: EditText? = null

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
        clientNameEditText = view.findViewById(R.id.client_name_edittext)
        nsdHelper = NsdHelper(menuActivity, nsdListener)
        nsdHelper.startDiscovery()
        hostListView = view.findViewById(R.id.list_view)
        statusTextView = view.findViewById(R.id.status_text_view)
        statusTextView.setText(R.string.finding)
        hostAdapter = HostsAdapter(menuActivity, ArrayList<Host>())
        hostListView.setAdapter(hostAdapter)
        hostListView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val string = clientNameEditText.getText().toString()
            if (string.length > 0) {
                client = Client(string, menuActivity, sendListener, receiveListener, clientListener)
                host = hostAdapter.getItem(position)
                client.connectTo(host)
            } else {
                Toast.makeText(menuActivity, R.string.fill_the_gaps, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuActivity = context as MenuActivity
    }

    fun disposeNetworkStuff() {
        nsdHelper.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeNetworkStuff()
    }

    private val nsdListener: NsdHelper.DiscoveryListener = object : DiscoveryListener() {
        fun onResolveFailed(errorCode: Int) {
            Toast.makeText(menuActivity, R.string.resolve_failed, Toast.LENGTH_SHORT).show()
        }

        fun onDiscoveryFailed(errorCode: Int) {
            Toast.makeText(menuActivity, R.string.discovery_failed, Toast.LENGTH_SHORT).show()
        }

        fun onHostFound(host: Host) {
            statusTextView.setText(R.string.found)
            hostMap[host.getName()] = host
            hostAdapter.clear()
            hostAdapter.addAll(hostMap.values)
            hostAdapter.notifyDataSetChanged()
        }

        fun onHostLost(name: String) {
            hostMap.remove(name)
            hostAdapter.clear()
            hostAdapter.addAll(hostMap.values)
            hostAdapter.notifyDataSetChanged()
            hostAdapter.notifyDataSetChanged()
            if (hostAdapter.getCount() == 0) {
                statusTextView.setText(R.string.not_found)
            }
        }
    }

    private val sendListener: SendListener = object : SendListener() {
        fun onSendSuccess() {}
        fun onSendFailure(e: Exception) {}
    }

    private val receiveListener: ReceiveListener = object : ReceiveListener() {
        fun onReceive(string: String) {
            when (string) {
                ACCEPTED -> {
                    Log.i("Client", ACCEPTED)
                    isAccepted = true
                    connection.send(REQUEST_MISSION_INFO)
                    Log.i("Client", REQUEST_MISSION_INFO)
                    buildMessageWaitingForAccepted()
                }
                REJECTED -> Toast.makeText(
                    menuActivity,
                    R.string.connection_refused,
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    Log.i("Client", "Default branch")
                    if (isAccepted) {
                        if (Mission.fromJson(string) == null) {
                            Log.i("Client", "Invalid Json")
                            connection.send(INVALID_JSON)
                            return
                        }
                        val intent = Intent(menuActivity, GameActivity::class.java)
                        intent.putExtra(CONNECTED_DEVICE, host)
                            .putExtra(MULTIPLAYER_GAME_MODE, CLIENT)
                            .putExtra(MISSION, string)
                        Const.connection.setCurrentConnection(connection)
                        nsdHelper.stopDiscovery()
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private val clientListener: Client.Listener = object : Listener() {
        fun onException(e: Exception) {
            Toast.makeText(menuActivity, R.string.error_connectiong, Toast.LENGTH_SHORT).show()
        }

        fun onConnectionEstablished(c: Connection) {
            connection = c
            connection.send(REQUEST_FOR_ACCEPT)
            Log.i("Client", REQUEST_FOR_ACCEPT)
        }
    }

    fun buildMessageWaitingForAccepted() {
        val dialog: SimpleDialogFragment = Builder(menuActivity)
            .setMessage(R.string.waiting_for_accepted)
            .setNegativeButton(
                R.string.cancel_request_for_accepted,
                DialogInterface.OnClickListener { dialog, which ->
                    connection.send(CANCEL_CONNECTION)
                    dialog.dismiss()
                }).build()
        dialog.show(parentFragmentManager, "WaitingForAccepted")
    }

}