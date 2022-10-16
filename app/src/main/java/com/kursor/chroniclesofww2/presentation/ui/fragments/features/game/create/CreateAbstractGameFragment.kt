package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentCreateNetworkGameBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.model.serializable.GameData
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle.BattleChooseFragment
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

/**
 *
 * THIS ABSTRACT CLASS IS FOR MULTIPLAYER!!!!!!!!!!!!
 * FOR SINGLEPLAYER THERE IS ANOTHER CLASS CreateNonNetworkGameFragment.kt
 */
abstract class CreateAbstractGameFragment : BundleFragment() {

    var gameDataJson: String = ""
    lateinit var gameData: GameData
    lateinit var binding: FragmentCreateNetworkGameBinding

    protected var isHostReady = false
    lateinit var battle: Battle

    abstract val actionToBattleChooseFragmentId: Int
    abstract val navigationGraphId: Int

    abstract val battleViewModel: BattleViewModel
    abstract val gameDataViewModel: GameDataViewModel
    val accountRepository by inject<AccountRepository>()

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
            myNameLiveData.value = accountRepository.username
            meInitiator = true
        }

        battleViewModel.battleLiveData.observe(viewLifecycleOwner) { battle ->
            this.battle = battle
            gameDataViewModel.battleLiveData.value = battle
            binding.gameDataFragment.visibility = View.VISIBLE
        }

        binding.chooseBattleButton.setOnClickListener {
            findNavController().navigate(
                actionToBattleChooseFragmentId,
                bundleOf(BattleChooseFragment.NAVIGATION_GRAPH_ID to navigationGraphId)
            )
        }
        binding.readyButton.setOnClickListener { v ->
            if (!checkConditionsForServerInit()) {
                Toast.makeText(
                    requireContext(),
                    R.string.need_wi_fi,
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (!this::battle.isInitialized)
                return@setOnClickListener
            gameDataJson =
                Moshi.GAMEDATA_ADAPTER.toJson(gameDataViewModel.createGameData())

            createGame()
        }
    }

    abstract fun createGame()

    abstract fun checkConditionsForServerInit(): Boolean

    protected fun showMessageWaitingForConnections(
        argText: String = "",
        onCancel: () -> Unit
    ) {
        onWaitingForConnectionsCancel = onCancel
        binding.readyButton.visibility = View.GONE
        binding.layoutConnectionRequest.layout.visibility = View.GONE

        binding.layoutWaitingForConnections.serviceMessagesTextView.text =
            getString(R.string.waiting_for_connected)
        binding.layoutWaitingForConnections.cancelButton.setOnClickListener {
            showReadyButton()
            onCancel()
        }
        if (argText.isNotBlank()) {
            binding.layoutWaitingForConnections.argTextView.text = argText
        }
        binding.layoutWaitingForConnections.layout.visibility = View.VISIBLE
    }

    private var onWaitingForConnectionsCancel: () -> Unit = { }
    private var waitingForConnectionsArgText: String = ""

    protected fun showMessageConnectionRequest(
        name: String,
        onAccept: () -> Unit,
        onRefuse: () -> Unit
    ) {
        binding.layoutWaitingForConnections.layout.visibility = View.GONE
        binding.readyButton.visibility = View.GONE
        binding.layoutConnectionRequest.acceptButton.setOnClickListener {
            onAccept()
        }
        binding.layoutConnectionRequest.refuseButton.setOnClickListener {
            showMessageWaitingForConnections(
                waitingForConnectionsArgText,
                onCancel = onWaitingForConnectionsCancel
            )
            onRefuse()
        }
        binding.layoutConnectionRequest.connectionRequestTextView.text =
            getString(R.string.connection_request, name)
        binding.layoutConnectionRequest.layout.visibility = View.VISIBLE
    }

    protected fun showReadyButton() {
        binding.layoutConnectionRequest.layout.visibility = View.GONE
        binding.layoutWaitingForConnections.layout.visibility = View.GONE
        binding.readyButton.visibility = View.VISIBLE
    }
}