package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kursor.chroniclesofww2.objects.Const.game.BATTLE
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.RemoteCustomBattleRepository
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.StandardBattleRepository
import com.kursor.chroniclesofww2.databinding.FragmentBattleChooseBinding
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.presentation.adapters.BattleAdapter
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment.Companion.BATTLE_REQUEST_CODE
import com.kursor.chroniclesofww2.viewModels.BattleListViewModel
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp
import org.koin.android.ext.android.inject

class BattleChooseFragment : BundleFragment() {

    lateinit var binding: FragmentBattleChooseBinding

    val standardBattleRepository by inject<StandardBattleRepository>()
    val localCustomBattleRepository by inject<LocalCustomBattleRepository>()
    val remoteCustomBattleRepository by inject<RemoteCustomBattleRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val battleListViewModel by viewModels<BattleListViewModel>()
        battleListViewModel.changeDataSource(standardBattleRepository)
        battleListViewModel.getBattleLiveData().observe(viewLifecycleOwner) { newBattleList ->
            binding.battleRecyclerView.adapter =
                BattleAdapter(requireActivity(), newBattleList).apply {
                    setOnItemClickListener { view, position, battle ->
                        navigateUpWithBattle(battle)
                    }
                }
        }
        binding.defaultMissionButton.setOnClickListener {
            navigateUpWithBattle(standardBattleRepository.defaultBattle())
        }
        binding.customMissionButton.setOnClickListener {
            navigate(
                R.id.action_battleChooseFragment_to_createNewBattleFragment,
                CUSTOM_MISSION_REQUEST_CODE
            )
        }

        binding.standardBattlesButton.setOnClickListener {
            battleListViewModel.changeDataSource(standardBattleRepository)
        }
        binding.localCustomBattlesButton.setOnClickListener {
            battleListViewModel.changeDataSource(localCustomBattleRepository)
        }
        binding.remoteCustomBattlesButton.setOnClickListener {
            battleListViewModel.changeDataSource(remoteCustomBattleRepository)
        }
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        if (requestCode != CUSTOM_MISSION_REQUEST_CODE) return
        navigateUp(BATTLE_REQUEST_CODE, bundle)
    }

    private fun navigateUpWithBattle(battle: Battle) {
        navigateUp(BATTLE_REQUEST_CODE, Bundle().apply {
            putString(BATTLE, Moshi.BATTLE_ADAPTER.toJson(battle))
        })
    }

    companion object {
        const val MISSION_FRAGMENT = "MISSION_FRAGMENT"

        const val CUSTOM_MISSION_REQUEST_CODE = 505
    }

}