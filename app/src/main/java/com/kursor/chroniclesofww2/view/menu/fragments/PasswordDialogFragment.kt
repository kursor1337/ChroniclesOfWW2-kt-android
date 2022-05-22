package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kursor.chroniclesofww2.databinding.DialogPasswordBinding
import com.phelat.navigationresult.BundleFragment

class PasswordDialogFragment : DialogFragment() {

    lateinit var binding: DialogPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.readyButton.setOnClickListener {

        }
    }

}