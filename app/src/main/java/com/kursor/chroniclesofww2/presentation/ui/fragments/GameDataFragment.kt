package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.databinding.FragmentGameDataBinding
import com.kursor.chroniclesofww2.model.data.Battle
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.game.board.Division.Type.*
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.viewModels.GameDataViewModel
import org.koin.android.ext.android.inject

class GameDataFragment : Fragment() {

    lateinit var binding: FragmentGameDataBinding

    val gameDataViewModel by viewModels<GameDataViewModel>({ requireParentFragment() })
    val battleManager by inject<BattleManager>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.defaultBoardSizeButton.apply {
            text = "${Board.DEFAULT_SIZE}x${Board.DEFAULT_SIZE}"
            setOnClickListener {
                binding.boardWidthEditText.setText(Board.DEFAULT_SIZE.toString())
                binding.boardHeightEditText.setText(Board.DEFAULT_SIZE.toString())
            }
        }

        gameDataViewModel.apply {
            battleDataLiveData.observe(viewLifecycleOwner) { battleData ->
                binding.battleNameTextView.text =
                    battleManager.findBattleById(battleData.id)?.name
                setTextsToViews(battleData)
            }
            myNameLiveData.observe(viewLifecycleOwner) { myName ->
                binding.myNameTextView.text = myName
            }
            enemyNameLiveData.observe(viewLifecycleOwner) { enemyName ->
                binding.enemyNameTextView.text = enemyName
            }
        }
        binding.invertNationsImageButton.setOnClickListener {
            invertNations()
            invertNationsViews()
        }
        binding.commitButton.setOnClickListener {
            gameDataViewModel.boardHeight = binding.boardHeightEditText.text.toString().toInt()
            gameDataViewModel.boardWidth = binding.boardWidthEditText.text.toString().toInt()
        }

    }


    fun setTextsToViews(battleData: Battle.Data) {
        binding.nation1TextView.text = battleData.nation1.toString()
        binding.nation2TextView.text = battleData.nation2.toString()

        binding.infantry1TextView.text = battleData.nation1divisions[INFANTRY].toString()
        binding.infantry2TextView.text = battleData.nation2divisions[INFANTRY].toString()
        binding.armored1TextView.text = battleData.nation1divisions[ARMORED].toString()
        binding.armored2TextView.text = battleData.nation2divisions[ARMORED].toString()
        binding.artillery1TextView.text = battleData.nation1divisions[ARTILLERY].toString()
        binding.artillery2TextView.text = battleData.nation2divisions[ARTILLERY].toString()
    }

    fun invertNations() {
        gameDataViewModel.invertNations = !gameDataViewModel.invertNations
    }

    fun invertNationsViews() {
        val nation1 = binding.nation1TextView.text
        val nation2 = binding.nation2TextView.text
        val infantry1 = binding.infantry1TextView.text
        val infantry2 = binding.infantry2TextView.text
        val armored1 = binding.armored1TextView.text
        val armored2 = binding.armored2TextView.text
        val artillery1 = binding.artillery1TextView.text
        val artillery2 = binding.artillery2TextView.text

        binding.nation2TextView.text = nation1
        binding.nation1TextView.text = nation2
        binding.infantry2TextView.text = infantry1
        binding.infantry1TextView.text = infantry2
        binding.armored2TextView.text = armored1
        binding.armored1TextView.text = armored2
        binding.artillery2TextView.text = artillery1
        binding.artillery1TextView.text = artillery2
    }

}