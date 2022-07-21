package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import com.kursor.chroniclesofww2.Const.game.SCENARIO
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentBattleChooseBinding
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.presentation.adapters.BattleAdapter
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment.Companion.SCENARIO_REQUEST_CODE
import com.kursor.chroniclesofww2.viewModels.BattleListViewModel
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp

class BattleChooseFragment : BundleFragment() {

    lateinit var binding: FragmentBattleChooseBinding




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

        val battleAdapter =
            BattleAdapter(requireActivity(), )
        binding.battleRecyclerView.adapter = battleAdapter
        binding.battleRecyclerView.setOnItemClickListener { _, _, position, _ ->
            navigateUpWithScenario(Scenario.getScenarioList(requireContext())[position])
        }
        binding.defaultMissionButton.setOnClickListener {
            navigateUpWithScenario(Scenario.defaultScenario())
        }
        binding.customMissionButton.setOnClickListener {
            navigate(R.id.action)
        }
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {

    }

    private fun navigateUpWithScenario(scenario: Scenario) {
        navigateUp(SCENARIO_REQUEST_CODE, Bundle().apply {
            putString(SCENARIO, Tools.GSON.toJson(scenario))
        })
    }

    companion object {
        const val MISSION_FRAGMENT = "MISSION_FRAGMENT"

        const val CUSTOM_MISSION_REQUEST_CODE = 505
    }

}