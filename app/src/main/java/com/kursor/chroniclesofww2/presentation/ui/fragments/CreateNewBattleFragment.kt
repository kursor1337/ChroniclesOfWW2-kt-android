package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.Const.game.BATTLE
import com.kursor.chroniclesofww2.Moshi
import com.kursor.chroniclesofww2.databinding.FragmentCreateBattleBinding
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.presentation.ui.fragments.BattleChooseFragment.Companion.CUSTOM_MISSION_REQUEST_CODE
import com.phelat.navigationresult.navigateUp

class CreateNewBattleFragment : DialogFragment() {

    lateinit var binding: FragmentCreateBattleBinding

    var nations: Array<Nation> = Nation.values()
    private var newMissionName = "Default"
    private var newMissionIntro = ""
    private var nation1: Nation? = null
    private var nation2: Nation? = null
    private var player1infantry = 0
    private var player1armored = 0
    private var player1artillery = 0
    private var player2infantry = 0
    private var player2armored = 0
    private var player2artillery = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter: ArrayAdapter<Nation> =
            ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, nations)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.nation1Spinner.adapter = adapter
        binding.nation2Spinner.adapter = adapter
        binding.nation1Spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    nation1 = Nation.values()[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    nation1 = null
                }
            }
        binding.nation2Spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                nation2 = Nation.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                nation2 = null
            }
        }
        binding.readyButton.setOnClickListener {
            if (nation1 == null || nation2 == null) return@setOnClickListener
            if (nation1 != nation2) {
                if (binding.nameEditText.text.toString() != "") {
                    newMissionName = binding.nameEditText.text.toString()
                }
                newMissionIntro = binding.introEditText.text.toString()
                player1infantry = binding.infantry1EditText.text.toString().toInt()
                player1armored = binding.armored1EditText.text.toString().toInt()
                player1artillery = binding.artillery1EditText.text.toString().toInt()
                player2infantry = binding.infantry2EditText.text.toString().toInt()
                player2armored = binding.armored2EditText.text.toString().toInt()
                player2artillery = binding.artillery2EditText.text.toString().toInt()
                val mission = Battle(
                    -1,
                    newMissionName,
                    newMissionIntro,
                    Battle.Data(
                        -1, nation1!!,
                        mapOf(
                            Division.Type.INFANTRY to player1infantry,
                            Division.Type.ARMORED to player1armored,
                            Division.Type.ARTILLERY to player1artillery
                        ),
                        nation2!!, mapOf(
                            Division.Type.INFANTRY to player2infantry,
                            Division.Type.ARMORED to player2armored,
                            Division.Type.ARTILLERY to player2artillery
                        )
                    )
                )
                navigateUp(
                    CUSTOM_MISSION_REQUEST_CODE, Bundle().apply {
                        putString(BATTLE, Moshi.BATTLE_ADAPTER.toJson(mission))
                    }
                )

            }
            if (nation1 == nation2) {
                Toast.makeText(
                    context,
                    "Страна не может воевать против самой себя!", Toast.LENGTH_LONG
                ).show()
            }
            if (player1infantry < 1 && player1armored < 1 && player1artillery < 1 ||
                player2infantry < 1 && player2armored < 1 && player2artillery < 1
            ) {
                Toast.makeText(
                    context,
                    "Необходимо минимум по одной дивизии кождой стороне", Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}