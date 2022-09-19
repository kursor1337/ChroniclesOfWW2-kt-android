package com.kursor.chroniclesofww2.presentation.ui.fragments.features.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.databinding.FragmentChangePasswordBinding
import com.kursor.chroniclesofww2.viewModels.features.user.ChangePasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    lateinit var binding: FragmentChangePasswordBinding

    val changePasswordViewModel by viewModel<ChangePasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.newPasswordEditText.doOnTextChanged { text, start, before, count ->
            changePasswordViewModel.setNewPassword(text.toString())
        }

        binding.currentPasswordEditText.doOnTextChanged { text, start, before, count ->
            changePasswordViewModel.setCurrentPassword(text.toString())
        }

        binding.repeatPasswordEditText.doOnTextChanged { text, start, before, count ->
            changePasswordViewModel.setRepeatPassword(text.toString())
        }

        binding.readyButton.setOnClickListener {
            if (binding.newPasswordEditText.text != binding.repeatPasswordEditText.text) {
                Toast.makeText(
                    requireContext(),
                    "Passwords don't match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            changePasswordViewModel.ready()
        }

        changePasswordViewModel.statusLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }
}