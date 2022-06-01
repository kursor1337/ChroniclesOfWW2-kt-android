package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentScenarioChooseBinding
import com.kursor.chroniclesofww2.model.Scenario
import com.phelat.navigationresult.BundleFragment

class ScenarioChooseFragment : BundleFragment() {

    lateinit var binding: FragmentScenarioChooseBinding

//    var missionNames: Array<String?>
//    var missionList: List<Mission>? = null
//    var missionAdapter: ArrayAdapter<String?>? = null
//    var missionListView: ListView? = null
//    var button_defaultMission: Button? = null
//    var button_customMission: Button? = null
//    private var menuActivity: MenuActivity? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScenarioChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scenarioNames = Scenario.getScenarioNames(requireContext())

        val scenarioAdapter =
            ArrayAdapter(requireContext(), R.layout.listview_missions, scenarioNames)
        binding.scenarioListView.adapter = scenarioAdapter
        binding.scenarioListView.setOnItemClickListener { _, _, position, _ ->

        }
        binding.defaultMissionButton.setOnClickListener {

        }
        binding.customMissionButton.setOnClickListener {

        }
        return view
    }

    companion object {
        const val MISSION_FRAGMENT = "MISSION_FRAGMENT"

        const val CUSTOM_MISSION_INFO = "customMissionInfo"
    }

}