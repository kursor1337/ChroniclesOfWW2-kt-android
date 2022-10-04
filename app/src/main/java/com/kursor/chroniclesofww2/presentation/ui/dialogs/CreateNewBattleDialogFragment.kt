package com.kursor.chroniclesofww2.presentation.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentCreateBattleBinding
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.serializable.Battle
import com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle.BattleChooseFragment
import com.kursor.chroniclesofww2.viewModels.features.battle.CreateNewBattleViewModel
import com.kursor.chroniclesofww2.viewModels.shared.BattleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNewBattleDialogFragment : DialogFragment() {

    lateinit var binding: FragmentCreateBattleBinding

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
        binding = FragmentCreateBattleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val battleViewModel by navGraphViewModels<BattleViewModel>(navigationGraphId!!)
        val createNewBattleViewModel by viewModel<CreateNewBattleViewModel>()

        createNewBattleViewModel.statusLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                CreateNewBattleViewModel.Status.SAME_NATIONS_AS_ENEMIES -> {
                    Toast.makeText(
                        requireContext(),
                        "Same nations as enemies",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                CreateNewBattleViewModel.Status.TOO_LITTLE_DIVISIONS -> {
                    Toast.makeText(
                        requireContext(),
                        "Too little divisions",
                        Toast.LENGTH_LONG
                    ).show()
                }
                CreateNewBattleViewModel.Status.BATTLE_CREATED -> {
                    battleViewModel.battleLiveData.value = arg as Battle
                }
            }
        }

        val adapter: ArrayAdapter<Nation> =
            ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                createNewBattleViewModel.nations
            )
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
                    createNewBattleViewModel.setNation1ByPosition(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    createNewBattleViewModel.clearNation1()
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
                createNewBattleViewModel.setNation2ByPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                createNewBattleViewModel.clearNation2()
            }
        }
        binding.readyButton.setOnClickListener {
            createNewBattleViewModel.ready()
            requireActivity().onBackPressed()
        }

        binding.readyAndSaveButton.setOnClickListener {
            createNewBattleViewModel.readyAndSave()
            findNavController().popBackStack(R.id.battleChooseFragment, inclusive = true)
            requireActivity().onBackPressed()
        }

        binding.nameEditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setBattleName(text.toString())
        }

        binding.introEditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setBattleDescription(text.toString())
        }

        binding.infantry1EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer1Infantry(text.toString())
        }

        binding.armored1EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer1Armored(text.toString())
        }

        binding.artillery1EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer1Artillery(text.toString())
        }

        binding.infantry2EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer2Infantry(text.toString())
        }

        binding.armored2EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer2Armored(text.toString())
        }

        binding.artillery2EditText.doOnTextChanged { text, start, before, count ->
            createNewBattleViewModel.setPlayer2Artillery(text.toString())
        }
    }

    companion object {
        const val NAVIGATION_GRAPH_ID = "navigation graph id"
    }

}