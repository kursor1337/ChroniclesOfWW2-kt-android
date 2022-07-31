package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.databinding.FragmentCreateNonNetworkGameBinding


class CreateNonNetworkGameFragment : Fragment() {


    lateinit var binding: FragmentCreateNonNetworkGameBinding

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

    }

}