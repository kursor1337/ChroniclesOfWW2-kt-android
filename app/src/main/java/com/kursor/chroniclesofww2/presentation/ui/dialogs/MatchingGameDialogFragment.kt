package com.kursor.chroniclesofww2.presentation.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentMatchBinding
import com.kursor.chroniclesofww2.features.MatchingGameMessageDTO
import com.kursor.chroniclesofww2.features.MatchingUserInfoDTO
import com.kursor.chroniclesofww2.game.MatchGameStatus
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.presentation.ui.activities.MainActivity
import com.kursor.chroniclesofww2.presentation.ui.activities.MultiplayerGameActivity
import com.kursor.chroniclesofww2.viewModels.features.game.MatchingGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchingGameDialogFragment : DialogFragment() {

    lateinit var binding: FragmentMatchBinding

    val matchingGameViewModel by viewModel<MatchingGameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showStartMatchingButton()
        matchingGameViewModel.messagesLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
        matchingGameViewModel.statusLiveData.observe(viewLifecycleOwner) { (status, arg) ->
            when (status) {
                MatchGameStatus.FOUND -> {
                    Toast.makeText(requireContext(), "Found match", Toast.LENGTH_LONG).show()
                    showFoundMatch(arg as MatchingUserInfoDTO)
                }
                MatchGameStatus.TIMEOUT -> {
                    Toast.makeText(requireContext(), "Timeout", Toast.LENGTH_LONG).show()
                    showStartMatchingButton()
                }
                MatchGameStatus.UNAUTHORIZED -> {
                    Toast.makeText(requireContext(), "Unauthorized", Toast.LENGTH_LONG).show()
                }
                MatchGameStatus.ERROR -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
                }
                MatchGameStatus.GAME_DATA_OBTAINED -> {
                    startActivity(
                        Intent(
                            requireActivity(),
                            MultiplayerGameActivity::class.java
                        ).apply {
                            putExtra(Const.game.GAME_DATA, arg as String)
                        }
                    )
                }
                MatchGameStatus.REJECT -> {
                    showStartMatchingButton()
                    Toast.makeText(requireContext(), "Rejected", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.startMatchingButton.setOnClickListener {
            matchingGameViewModel.startMatching()
            showMatchingLayout()
        }

        binding.stopMatchingButton.setOnClickListener {
            matchingGameViewModel.stopMatching()
            showStartMatchingButton()
        }

        binding.acceptButton.setOnClickListener {
            matchingGameViewModel.accept()
        }

        binding.rejectButton.setOnClickListener {
            matchingGameViewModel.reject()
        }

    }


    fun showFoundMatch(matchingUserInfoDTO: MatchingUserInfoDTO) {
        binding.loginTextView.text = matchingUserInfoDTO.login
        binding.scoreTextView.text = matchingUserInfoDTO.score.toString()
        binding.matchSuggestionLayout.visibility = View.VISIBLE
        hideMatchingLayout()
        hideStartMatchingButton()
    }

    fun hideFoundMatch() {
        binding.matchSuggestionLayout.visibility = View.GONE
        binding.loginTextView.text = ""
        binding.scoreTextView.text = ""
    }

    fun showStartMatchingButton() {
        binding.startMatchingButton.visibility = View.VISIBLE
        hideFoundMatch()
        hideMatchingLayout()
    }

    fun hideStartMatchingButton() {
        binding.startMatchingButton.visibility = View.GONE
    }

    fun showMatchingLayout() {
        binding.stopMatchingButton.visibility = View.VISIBLE
        binding.matchingProgressBar.visibility = View.VISIBLE
        hideFoundMatch()
        hideStartMatchingButton()
    }

    fun hideMatchingLayout() {
        binding.stopMatchingButton.visibility = View.GONE
        binding.matchingProgressBar.visibility = View.GONE
    }

}