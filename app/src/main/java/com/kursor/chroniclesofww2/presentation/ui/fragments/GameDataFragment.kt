package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kursor.chroniclesofww2.data.repositories.BattleManager
import com.kursor.chroniclesofww2.databinding.FragmentGameDataBinding
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.viewModels.GameDataViewModel
import org.koin.android.ext.android.inject

class GameDataFragment : Fragment() {

    lateinit var binding: FragmentGameDataBinding

    val gameDataViewModel by activityViewModels<GameDataViewModel>()
    val battleManager by inject<BattleManager>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.battleNameTextView.text =
            battleManager.findBattleById(gameDataViewModel.battleData.id)?.name
    }


    fun setTextsToViews() {
        binding.nation1TextView.text = gameDataViewModel.battleData.nation1.toString()
        binding.nation2TextView.text = gameDataViewModel.battleData.nation2.toString()

        binding.infantry1TextView.text =
            gameDataViewModel.battleData.nation1divisions[Division.Type.INFANTRY].toString()
        binding.infantry2TextView.text =
            gameDataViewModel.battleData.nation2divisions[Division.Type.INFANTRY].toString()
        binding.armored1TextView.text =
            gameDataViewModel.battleData.nation1divisions[Division.Type.ARMORED].toString()
        binding.armored2TextView.text =
            gameDataViewModel.battleData.nation2divisions[Division.Type.ARMORED].toString()
        binding.artillery1TextView.text =
            gameDataViewModel.battleData.nation1divisions[Division.Type.ARTILLERY].toString()
        binding.artillery2TextView.text =
            gameDataViewModel.battleData.nation2divisions[Division.Type.ARTILLERY].toString()
    }

    fun invertNations() {
        gameDataViewModel.invertNations = !gameDataViewModel.invertNations
    }

    fun invertNationsViews() {

    }

}