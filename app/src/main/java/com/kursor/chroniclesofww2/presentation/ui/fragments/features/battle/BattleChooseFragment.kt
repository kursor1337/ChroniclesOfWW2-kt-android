package com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.data.repositories.battle.LocalCustomBattleRepositoryImpl
import com.kursor.chroniclesofww2.data.repositories.battle.StandardBattleRepositoryImpl
import com.kursor.chroniclesofww2.databinding.FragmentBattleChooseBinding
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.presentation.adapters.BattleAdapter
import com.kursor.chroniclesofww2.viewModels.shared.BattleListViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

class BattleChooseFragment : BundleFragment() {

    lateinit var binding: FragmentBattleChooseBinding

    private val standardBattleRepository by inject<StandardBattleRepositoryImpl>()
    private val localCustomBattleRepository by inject<LocalCustomBattleRepositoryImpl>()
    //private val remoteCustomBattleRepository by inject<RemoteCustomBattleRepository>()

    var navigationGraphId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationGraphId = requireArguments().getInt(NAVIGATION_GRAPH_ID)
    }

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

        val battleViewModel by navGraphViewModels<BattleViewModel>(navigationGraphId!!)

        binding.battleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val battleListViewModel by viewModels<BattleListViewModel>()
        battleListViewModel.getBattleLiveData().observe(viewLifecycleOwner) { newBattleList ->
            binding.battleRecyclerView.adapter =
                BattleAdapter(requireActivity(), newBattleList).apply {
                    setOnItemClickListener { view, position, battle: Battle ->
                        navigateUpWithBattle(battle, battleViewModel)
                    }
                }
        }

        battleListViewModel.changeDataSource(standardBattleRepository)
        binding.defaultMissionButton.setOnClickListener {
            navigateUpWithBattle(standardBattleRepository.defaultBattle(), battleViewModel)
        }
        binding.customMissionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_battleChooseFragment2_to_createNewBattleDialogFragment2,
                Bundle().apply {
                    putInt(NAVIGATION_GRAPH_ID, navigationGraphId ?: return@setOnClickListener)
                })
        }

        binding.standardBattlesButton.setOnClickListener {
            battleListViewModel.changeDataSource(standardBattleRepository)
        }
        binding.localCustomBattlesButton.setOnClickListener {
            battleListViewModel.changeDataSource(localCustomBattleRepository)
        }
        binding.remoteCustomBattlesButton.setOnClickListener {
            //battleListViewModel.changeDataSource(remoteCustomBattleRepository)
        }
    }

    private fun navigateUpWithBattle(battle: Battle, battleViewModel: BattleViewModel) {
        battleViewModel.battleLiveData.value = battle
        requireActivity().onBackPressed()
    }

    companion object {
        const val MISSION_FRAGMENT = "MISSION_FRAGMENT"

        const val CUSTOM_MISSION_REQUEST_CODE = 505

        const val NAVIGATION_GRAPH_ID = "navigation graph id"
    }

}