package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentCreateNonNetworkGameBinding
import com.kursor.chroniclesofww2.viewModels.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.GameDataViewModel


class CreateNonNetworkGameFragment : Fragment() {


    lateinit var binding: FragmentCreateNonNetworkGameBinding
    val battleViewModel by navGraphViewModels<BattleViewModel>(R.id.navigation_non_network_game)
    val gameDataViewModel by viewModels<GameDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNonNetworkGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.chooseBattleButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_createNonNetworkGameFragment_to_battleChooseFragment2,
                bundleOf(BattleChooseFragment.NAVIGATION_GRAPH_ID to R.id.navigation_non_network_game)
            )
        }
    }
}