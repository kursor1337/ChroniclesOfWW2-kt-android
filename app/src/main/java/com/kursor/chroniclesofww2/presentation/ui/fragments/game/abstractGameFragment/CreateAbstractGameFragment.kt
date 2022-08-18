package com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.interfaces.LocalServer
import com.kursor.chroniclesofww2.databinding.FragmentCreateNetworkGameBinding
import com.kursor.chroniclesofww2.domain.interfaces.AccountRepository
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Const.connection.HOST_IS_WITH_PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.INVALID_JSON
import com.kursor.chroniclesofww2.objects.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_GAME_DATA
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle.BattleChooseFragment
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

/**
 *
 * THIS ABSTRACT CLASS IS FOR MULTIPLAYER!!!!!!!!!!!!
 * FOR SINGLEPLAYER THERE IS ANOTHER CLASS CreateSinglePlayerGameFragment.kt
 */
abstract class CreateAbstractGameFragment : BundleFragment() {

    var gameDataJson: String = ""
    lateinit var binding: FragmentCreateNetworkGameBinding

    var currentDialog: DialogFragment? = null

    protected lateinit var localServer: LocalServer
    lateinit var connection: Connection
    protected var isHostReady = false
    lateinit var gameData: GameData
    lateinit var battle: Battle

    abstract val actionToBattleChooseFragmentId: Int

    abstract val battleViewModel: BattleViewModel
    abstract val gameDataViewModel: GameDataViewModel
    val settings by inject<AccountRepository>()

    protected val receiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    if (localServer.password != null) {
                        connection.send(HOST_IS_WITH_PASSWORD)
                    } else {
                        currentDialog?.dismiss()
                        buildMessageConnectionRequest(connection.host)
                    }
                }
                REQUEST_GAME_DATA -> {
                    Log.i("Server", "Client sent $REQUEST_GAME_DATA")
                    connection.send(gameDataJson)
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.connection.CONNECTED_DEVICE, connection.host)
                        .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.GAME_DATA, gameDataJson)
                    Tools.currentConnection = connection
                    localServer.stopListening()
                    startActivity(intent)
                }
                CANCEL_CONNECTION -> {
                    Log.i("Server", CANCEL_CONNECTION)
                    connection.shutdown()
                    Log.i("Server", "Sent invalid json")
                }
                INVALID_JSON -> Log.i("Server", "Sent invalid json")
                else -> {
                    if (string.startsWith(PASSWORD)) {
                        val password = string.removePrefix(PASSWORD)
                        if (password == localServer.password) {
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

    protected val serverListener = object : LocalServer.Listener {
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
        binding = FragmentCreateNetworkGameBinding.inflate(inflater, container, false)
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
            gameDataViewModel.battleLiveData.value = battle
            binding.chosenScenarioTextView.text = battle.name
            Log.i("CreateAbstractGameFragment", "set name battle")
            binding.gameDataFragment.visibility = View.VISIBLE
        }

        binding.chooseBattleButton.setOnClickListener {
            findNavController().navigate(
                actionToBattleChooseFragmentId,
                bundleOf(BattleChooseFragment.NAVIGATION_GRAPH_ID to R.id.navigation_local_game)
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
                    localServer.stopListening()
                    binding.readyButton.isEnabled = true
                }.setMessage("Waiting for Connections...")
                .setOnCancelListener {
                    localServer.stopListening()
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
        if (this::localServer.isInitialized) localServer.stopListening()
    }

    companion object {
        const val BATTLE_REQUEST_CODE = 202
    }

}