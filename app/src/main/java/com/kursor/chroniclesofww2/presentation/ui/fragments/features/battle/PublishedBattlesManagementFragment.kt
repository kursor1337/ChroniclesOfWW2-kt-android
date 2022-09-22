package com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.databinding.FragmentPublishedBattlesManagementBinding

class PublishedBattlesManagementFragment : Fragment() {

    lateinit var binding: FragmentPublishedBattlesManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPublishedBattlesManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}