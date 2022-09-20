package com.kursor.chroniclesofww2.presentation.ui.fragments.features.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.databinding.FragmentDeleteAccountBinding
import com.kursor.chroniclesofww2.viewModels.features.user.DeleteAccountViewModel
import org.koin.android.ext.android.inject

class DeleteAccountFragment : Fragment() {

    lateinit var binding: FragmentDeleteAccountBinding

    val deleteAccountViewModel by inject<DeleteAccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginEditText.doOnTextChanged { text, start, before, count ->
            deleteAccountViewModel.setLogin(text.toString())
        }

        binding.currentPasswordEditText.doOnTextChanged { text, start, before, count ->
            deleteAccountViewModel.setPassword(text.toString())
        }

        binding.repeatPasswordEditText.doOnTextChanged { text, start, before, count ->
            deleteAccountViewModel.setRepeatPassword(text.toString())
        }

        deleteAccountViewModel.statusLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

}