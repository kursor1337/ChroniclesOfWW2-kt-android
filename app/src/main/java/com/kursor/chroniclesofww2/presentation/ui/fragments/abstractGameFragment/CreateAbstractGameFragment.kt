package com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Const.connection.HOST_IS_WITH_PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.INVALID_JSON
import com.kursor.chroniclesofww2.objects.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_GAME_DATA
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.Server
import com.kursor.chroniclesofww2.databinding.FragmentCreateGameBinding
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.data.GameData
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.presentation.ui.activities.GameActivity
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.kursor.chroniclesofww2.viewModels.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.GameDataViewModel
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

/**
 *
 * THIS ABSTRACT CLASS IS FOR MULTIPLAYER!!!!!!!!!!!!
 * FOR SINGLEPLAYER THERE IS ANOTHER CLASS CreateSinglePlayerGameFragment.kt
 */
abstract class CreateAbstractGameFragment : BundleFragment() {

    var gameDataJson: String = ""
    lateinit var binding: FragmentCreateGameBinding

    var currentDialog: DialogFragment? = null

    protected lateinit var server: Server
    lateinit var connection: Connection
    protected var isHostReady = false
    lateinit var gameData: GameData
    lateinit var battle: Battle

    abstract val actionToBattleChooseFragmentId: Int

    val battleViewModel by activityViewModels<BattleViewModel>()
    val gameDataViewModel by activityViewModels<GameDataViewModel>()
    val settings by inject<Settings>()

    protected val receiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    if (server.password != null) {
                        connection.send(HOST_IS_WITH_PASSWORD)
                    } else {
                        currentDialog?.dismiss()
                        buildMessageConnectionRequest(connection.host)
                    }
                }
                REQUEST_GAME_DATA -> {
                    Log.i("Server", "Client sent $REQUEST_GAME_DATA")
                    connection.send(gameDataJson)
                    val intent = Intent(activity, GameActivity::class.java)
                    intent.putExtra(Const.connection.CONNECTED_DEVICE, connection.host)
                        .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.BATTLE, gameDataJson)
                    Tools.currentConnection = connection
                    server.stopListening()
                    startActivity(intent)
                }
                CANCEL_CONNECTION -> {
                    Log.i("Server", CANCEL_CONNECTION)
                    connection.shutdown()
                    Log.i("Server", "Sent invalid json")
                }
                INVALID_JSON -> Log.i("Server", "Sent invalid json")
                else -> {
                    if (string.contains(PASSWORD)) {
                        val password = string.removePrefix(PASSWORD)
                        if (password == server.password) {
                            currentDialog?.dismiss()
                            buildMessageConnectionRequest(connection.host)
                        }
                    }
                }
            }
        }

        override fun onDisconnected() {
            TODO("Not yet implemented")
        }

    }

    protected val serverListener = object : Server.Listener {
        override fun onConnectionEstablished(connection: Connection) {
            Tools.currentConnection = connection.apply {
                this.receiveListener = this@CreateAbstractGameFragment.receiveListener
            }
            gameDataViewModel.enemyNameLiveData.value = connection.host.name
        }

        override fun onStartedListening(host: Host) {
            buildMessageWaitingForConnections()
        }

        override fun onStartedListening() {
            buildMessageWaitingForConnections()
        }

        override fun onListeningStartError(e: Exception) {
            Toast.makeText(activity, "Listening start error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameDataFragment.visibility = View.GONE
        gameDataViewModel.apply {
            myNameLiveData.value = settings.username
            meInitiator = true
        }

        battleViewModel.battleLiveData.observe(viewLifecycleOwner) { battle ->
            this.battle = battle
            gameDataViewModel.battleDataLiveData.value = battle.data
            binding.chosenScenarioTextView.text = battle.name
            Log.i("CreateAbstractGameFragment", "set name battle")
            binding.gameDataFragment.visibility = View.VISIBLE
        }

        binding.chooseScenarioButton.setOnClickListener {
            navigate(
                actionToBattleChooseFragmentId,
                BATTLE_REQUEST_CODE
            )
        }
        binding.readyButton.setOnClickListener { v ->
            if (!checkConditionsForServerInit()) {
                Toast.makeText(
                    requireContext(),
                    "U need to connect to wi fi network",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (!this::battle.isInitialized) return@setOnClickListener
            gameDataJson =
                Moshi.GAMEDATA_ADAPTER.toJson(gameDataViewModel.createGameData())

            initServer()
            v.isEnabled = false
        }
    }

    abstract fun initServer()

    abstract fun checkConditionsForServerInit(): Boolean

    private fun buildMessageWaitingForConnections(): SimpleDialogFragment {
        val dialog: SimpleDialogFragment =
            SimpleDialogFragment.Builder(activity).setCancelable(false)
                .setNegativeButton("Cancel") { dialog, which ->
                    server.stopListening()
                    binding.readyButton.isEnabled = true
                }.setMessage("Waiting for Connections...")
                .setOnCancelListener {
                    server.stopListening()
                    binding.readyButton.isEnabled = true
                }
                .build()
        dialog.show(parentFragmentManager, "Waiting for connections")
        return dialog
    }

    private fun buildMessageConnectionRequest(host: Host) {
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(activity)
            .setMessage(host.name + " wants to connect to this  device. Do you agree?")
            .setCancelable(false)
            .setNegativeButton("Refuse") { dialog, which ->
                connection.send(Const.connection.REJECTED)
            }
            .setPositiveButton("Allow") { dialog, which ->
                connection.send(Const.connection.ACCEPTED)
            }
            .setOnCancelListener { connection.send(Const.connection.REJECTED) }
            .build()
        dialog.show(parentFragmentManager, "Waiting for Connected")
        Toast.makeText(activity, R.string.waiting_for_connected, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::server.isInitialized) server.stopListening()
    }

    companion object {
        const val BATTLE_REQUEST_CODE = 202
    }

}