package com.kursor.chroniclesofww2.view.menu.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentScenarioChooseBinding
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuActivity = context as MenuActivity
    }

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
        binding.button_defa = view.findViewById(R.id.default_mission)
        button_customMission = view.findViewById(R.id.custom_mission)
        missionListView = view.findViewById(R.id.mission_list)
        missionList = AllMissions.getMissionList()
        missionNames = arrayOfNulls(missionList!!.size)
        for (i in missionList!!.indices) {
            missionNames[i] = missionList!![i].getMissionName()
        }
        missionAdapter = ArrayAdapter(activity!!, R.layout.listview_missions, missionNames)
        missionListView.setAdapter(missionAdapter)
        missionListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val result = Bundle()
            result.putString(MISSION, Mission.toJson(AllMissions.getMissionList().get(position)))
            parentFragmentManager.setFragmentResult(CreateHostFragment.MISSION_INFO, result)
            parentFragmentManager.popBackStackImmediate()
        })
        button_defaultMission.setOnClickListener(View.OnClickListener {
            val result = Bundle()
            result.putString(MISSION, Mission.toJson(AllMissions.getDefaultMission()))
            parentFragmentManager.setFragmentResult(CreateHostFragment.MISSION_INFO, result)
            parentFragmentManager.popBackStackImmediate()
        })
        button_customMission.setOnClickListener(View.OnClickListener {
            val fragment = CreateNewScenarioFragment()
            fragment.show(parentFragmentManager, "CreateNewScenarioFragment")
        })
        return view
    }

    fun sendScenario(mission: Mission?) {  }

    companion object {
        const val MISSION_FRAGMENT = "MISSION_FRAGMENT"

        const val CUSTOM_MISSION_INFO = "customMissionInfo"
    }

}