package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.Const.connection.LOCAL
import com.kursor.chroniclesofww2.Const.connection.MULTIPLAYER_TYPE
import com.kursor.chroniclesofww2.Const.connection.WEB
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.localGameButton.setOnClickListener(getOnClickListener(LOCAL))

        binding.webGameButton.setOnClickListener(getOnClickListener(WEB))
    }

    private fun getOnClickListener(multiplayerType: String) = View.OnClickListener {
        findNavController().navigate(
            R.id.action_mainFragment_to_chooseMultiplayerGameFragment,
            Bundle().apply {
                putString(MULTIPLAYER_TYPE, multiplayerType)
            })
    }
}