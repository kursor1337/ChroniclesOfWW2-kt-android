package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.joinGameButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_joinGameFragment,
                Bundle().apply {

                })
        }

        binding.createHostButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_createHostFragment,
                Bundle().apply {

                })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}