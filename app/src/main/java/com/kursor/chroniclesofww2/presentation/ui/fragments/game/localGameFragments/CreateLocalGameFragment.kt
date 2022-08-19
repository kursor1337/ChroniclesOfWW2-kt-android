package com.kursor.chroniclesofww2.presentation.ui.fragments.game.localGameFragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.domain.interfaces.Connection
import com.kursor.chroniclesofww2.domain.interfaces.LocalServer
import com.kursor.chroniclesofww2.connection.local.NsdLocalServer
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.CreateAbstractGameFragment
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel

class CreateLocalGameFragment : CreateAbstractGameFragment() {


    override val actionToBattleChooseFragmentId =
        R.id.action_createLocalGameFragment_to_battleChooseFragment
    override val battleViewModel by navGraphViewModels<BattleViewModel>(R.id.navigation_local_game)
    override val gameDataViewModel by viewModels<GameDataViewModel>()

    protected val receiveListener = object : Connection.ReceiveListener {
        override fun onReceive(string: String) {
            when (string) {
                Const.connection.REQUEST_FOR_ACCEPT -> if (isHostReady) {
                    if (localServer.password != null) {
                        connection.send(Const.connection.HOST_IS_WITH_PASSWORD)
                    } else {
                        currentDialog?.dismiss()
                        buildMessageConnectionRequest(connection.host)
                    }
                }
                Const.connection.REQUEST_GAME_DATA -> {
                    Log.i("Server", "Client sent ${Const.connection.REQUEST_GAME_DATA}")
                    connection.send(gameDataJson)
                    val intent = Intent(activity, MultiplayerGameActivity::class.java)
                    intent.putExtra(Const.connection.CONNECTED_DEVICE, connection.host)
                        .putExtra(Const.game.MULTIPLAYER_GAME_MODE, Const.connection.HOST)
                        .putExtra(Const.game.GAME_DATA, gameDataJson)
                    Tools.currentConnection = connection
                    localServer.stopListening()
                    startActivity(intent)
                }
                Const.connection.CANCEL_CONNECTION -> {
                    Log.i("Server", Const.connection.CANCEL_CONNECTION)
                    connection.shutdown()
                    Log.i("Server", "Sent invalid json")
                }
                Const.connection.INVALID_JSON -> Log.i("Server", "Sent invalid json")
                else -> {
                    if (string.startsWith(Const.connection.PASSWORD)) {
                        val password = string.removePrefix(Const.connection.PASSWORD)
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

    override fun initServer() {
        if (gameDataJson.isBlank()) return
        localServer = NsdLocalServer(
            requireActivity(),
            settings.username,
            binding.hostPasswordEditText.text.toString(),
            serverListener
        )
        localServer.startListening()
        isHostReady = true
    }

    override fun checkConditionsForServerInit(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }


}