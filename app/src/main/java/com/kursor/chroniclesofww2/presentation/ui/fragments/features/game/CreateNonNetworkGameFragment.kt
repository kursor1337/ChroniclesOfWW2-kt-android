package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentCreateNonNetworkGameBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.presentation.ui.activities.SinglePlayerGameActivity
import com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle.BattleChooseFragment
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.GameDataViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel


class CreateNonNetworkGameFragment : Fragment() {


    lateinit var binding: FragmentCreateNonNetworkGameBinding
    val battleViewModel by koinNavGraphViewModel<BattleViewModel>(R.id.navigation_non_network_game)
    val gameDataViewModel by viewModels<GameDataViewModel>()
    val accountRepository by inject<AccountRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNonNetworkGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.gameDataFragment.visibility = View.GONE
        gameDataViewModel.apply {
            myNameLiveData.value = accountRepository.username
            enemyNameLiveData.value = "player"
            meInitiator = true
        }


        binding.chooseBattleButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_createNonNetworkGameFragment_to_battleChooseFragment2,
                bundleOf(BattleChooseFragment.NAVIGATION_GRAPH_ID to R.id.navigation_non_network_game)
            )
        }
        battleViewModel.battleLiveData.observe(viewLifecycleOwner) { battle ->
            gameDataViewModel.battleLiveData.value = battle
            Log.i("CreateAbstractGameFragment", "set name battle")
            binding.gameDataFragment.visibility = View.VISIBLE
        }
        binding.readyButton.setOnClickListener { v ->
            val gameData = gameDataViewModel.createGameData()
            if (gameData == null) {
                Toast.makeText(requireContext(), "Game data not obtained", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            startActivity(Intent(activity, SinglePlayerGameActivity::class.java).apply {
                putExtra(Const.game.GAME_DATA, Moshi.GAMEDATA_ADAPTER.toJson(gameData))
            })
        }
    }
}