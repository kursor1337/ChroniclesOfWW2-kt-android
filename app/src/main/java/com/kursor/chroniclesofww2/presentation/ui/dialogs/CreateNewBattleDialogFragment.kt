package com.kursor.chroniclesofww2.presentation.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.objects.Const.game.BATTLE
import com.kursor.chroniclesofww2.objects.Moshi
import com.kursor.chroniclesofww2.databinding.FragmentCreateBattleBinding
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.presentation.ui.fragments.BattleChooseFragment
import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment.Companion.BATTLE_REQUEST_CODE
import com.kursor.chroniclesofww2.viewModels.BattleViewModel
import com.phelat.navigationresult.navigateUp
import org.koin.android.ext.android.inject

class CreateNewBattleDialogFragment : DialogFragment() {

    lateinit var binding: FragmentCreateBattleBinding

    var nations: Array<Nation> = Nation.values()
    private var nation1: Nation? = null
    private var nation2: Nation? = null

    var navigationGraphId: Int? = null

    val battleViewModel by navGraphViewModels<BattleViewModel>(R.id.navigation_local_game)
    val localCustomBattleRepository by inject<LocalCustomBattleRepository>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationGraphId = requireArguments().getInt(BattleChooseFragment.NAVIGATION_GRAPH_ID)
    }

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
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, nations)
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
            val battle = getBattleFromViews() ?: return@setOnClickListener
            battleViewModel.battleLiveData.value = battle
            findNavController().popBackStack(R.id.battleChooseFragment, inclusive = true)
        }

        binding.readyAndSaveButton.setOnClickListener {
            val battle = getBattleFromViews() ?: return@setOnClickListener
            battleViewModel.battleLiveData.value = battle

            localCustomBattleRepository.saveBattle(battle)
            findNavController().popBackStack(R.id.battleChooseFragment, inclusive = true)
        }
    }

    fun getBattleFromViews(): Battle? {
        if (nation1 == null || nation2 == null) return null
        if (nation1 == nation2) {
            Toast.makeText(
                context,
                "Страна не может воевать против самой себя!", Toast.LENGTH_LONG
            ).show()
            return null
        }
        val newMissionName = binding.nameEditText.text.toString().ifBlank { return null }
        val newMissionIntro = binding.introEditText.text.toString()
        val player1infantry = binding.infantry1EditText.text.toString().ifBlank { "0" }.toInt()
        val player1armored = binding.armored1EditText.text.toString().ifBlank { "0" }.toInt()
        val player1artillery = binding.artillery1EditText.text.toString().ifBlank { "0" }.toInt()
        val player2infantry = binding.infantry2EditText.text.toString().ifBlank { "0" }.toInt()
        val player2armored = binding.armored2EditText.text.toString().ifBlank { "0" }.toInt()
        val player2artillery = binding.artillery2EditText.text.toString().ifBlank { "0" }.toInt()

        if (player1infantry + player1armored + player1artillery < 1 ||
            player2infantry + player2armored + player2artillery < 1
        ) {
            Toast.makeText(
                context,
                "Необходимо минимум по одной дивизии кождой стороне", Toast.LENGTH_LONG
            ).show()
            return null
        }

        val battle = Battle(
            localCustomBattleRepository.nextBattleId(), newMissionName, newMissionIntro,
            Battle.Data(
                localCustomBattleRepository.nextBattleId(), nation1!!, mapOf(
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
        return battle
    }
}