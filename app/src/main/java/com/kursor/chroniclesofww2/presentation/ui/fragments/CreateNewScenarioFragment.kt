package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.kursor.chroniclesofww2.model.Nation

class CreateNewScenarioFragment : DialogFragment() {

    var nations: Array<Nation> = Nation.values()
    private var newMissionName = "Default"
    private var newMissionIntro = ""
    private var nation1: Nation? = null, private  var nation2:Nation? = null
    private var listener: OnScenarioListener? = null
    private var player1infantry =
        0, private  var player1armored:Int = 0, private  var player1artillery:Int = 0
    private var player2infantry =
        0, private  var player2armored:Int = 0, private  var player2artillery:Int = 0

    //widgets
//    private var nation_spinner1: Spinner? = null,  //widgets
//    private var nation_spinner2: Spinner? = null
//    private var name: EditText? = null, private  var intro:EditText? = null
//    private var infantry1: EditText? =
//        null, private  var armored1:EditText? = null, private  var artillery1:EditText? = null
//    private var infantry2: EditText? =
//        null, private  var armored2:EditText? = null, private  var artillery2:EditText? = null
//    private var ready: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter: ArrayAdapter<Nation> =
            ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, nations)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        nation_spinner1.setAdapter(adapter)
        nation_spinner2.setAdapter(adapter)
        nation_spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                nation1 = Nation.values().get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                nation1 = null
            }
        })
        nation_spinner2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                nation2 = Nation.values().get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                nation2 = null
            }
        })
        ready!!.setOnClickListener {
            if (nation1 !== nation2) {
                if (name!!.text.toString() != "") {
                    newMissionName = name!!.text.toString()
                }
                newMissionIntro = intro.getText().toString()
                player1infantry = infantry1!!.text.toString().toInt()
                player1armored = armored1.getText().toString().toInt()
                player1artillery = artillery1.getText().toString().toInt()
                player2infantry = infantry2!!.text.toString().toInt()
                player2armored = armored2.getText().toString().toInt()
                player2artillery = artillery2.getText().toString().toInt()
                val mission = Mission(
                    Mission.DEFAULT, newMissionName, newMissionIntro,
                    nation1, player1infantry, player1armored, player1artillery,
                    nation2, player2infantry, player2armored, player2artillery
                )
                val result = Bundle()
                result.putString(MISSION, Mission.toJson(mission))
                parentFragmentManager.setFragmentResult(MissionFragment.CUSTOM_MISSION_INFO, result)
                dismiss()
            }
            if (nation1 === nation2) {
                Toast.makeText(
                    context,
                    "Страна не может воевать против самой себя!", Toast.LENGTH_LONG
                ).show()
            }
            if (player1infantry < 1 && player1armored < 1 && player1artillery < 1 && player2infantry < 1 && player2armored < 1 && player2artillery < 1
            ) {
                Toast.makeText(
                    context,
                    "Необходимо минимум по одной дивизии кождой стороне", Toast.LENGTH_LONG
                ).show()
            }
        }
        return view
    }
}