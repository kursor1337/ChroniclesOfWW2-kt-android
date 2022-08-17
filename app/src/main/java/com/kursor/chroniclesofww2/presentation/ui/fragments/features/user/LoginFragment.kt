package com.kursor.chroniclesofww2.presentation.ui.fragments.features.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.kursor.chroniclesofww2.Settings
import com.kursor.chroniclesofww2.databinding.FragmentLoginBinding
import com.kursor.chroniclesofww2.viewModels.features.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {


    lateinit var binding: FragmentLoginBinding
    val loginViewModel by viewModel<LoginViewModel>()
    val settings by inject<Settings>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginEditText.setText(loginViewModel.login)
        binding.passwordEditText.setText(loginViewModel.password)

        loginViewModel.loginResponseLiveData.observe(viewLifecycleOwner) { loginResponseDTO ->
            if (loginResponseDTO == null) return@observe
            if (loginResponseDTO.token == null) Toast.makeText(
                requireContext(),
                loginResponseDTO.message,
                Toast.LENGTH_SHORT
            ).show()
            else settings.token = loginResponseDTO.token
        }


        binding.loginEditText.doOnTextChanged { text, start, before, count ->
            loginViewModel.login = text.toString()
        }
        binding.passwordEditText.doOnTextChanged { text, start, before, count ->
            loginViewModel.password = text.toString()
        }
        binding.signInButton.setOnClickListener {
            loginViewModel.login()
        }

    }

}