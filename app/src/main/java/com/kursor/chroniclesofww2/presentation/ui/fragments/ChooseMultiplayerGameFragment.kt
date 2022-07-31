package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.objects.Const.connection.LOCAL
import com.kursor.chroniclesofww2.objects.Const.connection.MULTIPLAYER_TYPE
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentChooseMultiplayerGameBinding

class ChooseMultiplayerGameFragment : Fragment() {

    private lateinit var binding: FragmentChooseMultiplayerGameBinding

    private lateinit var multiplayerType: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            val type = it.getString(MULTIPLAYER_TYPE)
            if (type == null) activity?.onBackPressed()
            else multiplayerType = type
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseMultiplayerGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.joinGameButton.setOnClickListener {
            findNavController().navigate(getJoinGameActionId(multiplayerType))
        }

        binding.createGameButton.setOnClickListener {
            findNavController().navigate(getCreateGameActionId(multiplayerType))
        }
    }

    private fun getJoinGameActionId(multiplayerType: String) = if (multiplayerType == LOCAL) {
        R.id.action_chooseMultiplayerGameFragment_to_joinLocalGameFragment
    } else 0 //TODO(Web join game fragment action id)

    private fun getCreateGameActionId(multiplayerType: String) = if (multiplayerType == LOCAL) {
        R.id.action_chooseMultiplayerGameFragment_to_navigation_local_game
    } else 0 //TODO(Web create game fragment action id)

}