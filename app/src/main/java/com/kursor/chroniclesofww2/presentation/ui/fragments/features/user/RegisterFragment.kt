package com.kursor.chroniclesofww2.presentation.ui.fragments.features.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.databinding.FragmentRegisterBinding
import com.kursor.chroniclesofww2.viewModels.features.RegisterViewModel
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    val registerViewModel by inject<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.loginEditText.setText(registerViewModel.login)
        binding.usernameEditText.setText(registerViewModel.username)
        binding.passwordEditText.setText(registerViewModel.password)
        binding.repeatPasswordEditText.setText(registerViewModel.repeatPassword)

        binding.loginEditText.doOnTextChanged { text, start, before, count ->
            registerViewModel.login = text.toString()
        }

        binding.usernameEditText.doOnTextChanged { text, start, before, count ->
            registerViewModel.username = text.toString()
        }

        binding.passwordEditText.doOnTextChanged { text, start, before, count ->
            registerViewModel.password = text.toString()
        }

        binding.repeatPasswordEditText.doOnTextChanged { text, start, before, count ->
            registerViewModel.repeatPassword = text.toString()
        }

        binding.signUpButton.setOnClickListener {
            val coroutineStarted = registerViewModel.register()
            if (!coroutineStarted) Toast.makeText(
                requireContext(),
                "Not same text in password and repeat password",
                Toast.LENGTH_LONG
            ).show()
        }

        registerViewModel.registerResponseLiveData.observe(viewLifecycleOwner) { registerResponseDTO ->
            if (registerResponseDTO.token == null) Toast.makeText(
                requireContext(),
                "Something went wrong while registering",
                Toast.LENGTH_LONG
            ).show()
            else requireActivity().onBackPressed()
        }
    }
}