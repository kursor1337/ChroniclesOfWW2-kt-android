package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentSettingsBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.user.LogoutUseCase
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import com.kursor.chroniclesofww2.features.Routes
import com.kursor.chroniclesofww2.objects.Const
import com.kursor.chroniclesofww2.viewModels.SettingsViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        settingsViewModel.checkIsSignedIn()
        settingsViewModel.isSignedInLiveData.observe(viewLifecycleOwner) { (isSignedIn, args) ->
            if (isSignedIn) {
                binding.notSignedInLayout.visibility = View.GONE
                binding.signedInLayout.visibility = View.VISIBLE
                binding.loginTextView.text = args[SettingsViewModel.LOGIN]
            } else {
                binding.notSignedInLayout.visibility = View.VISIBLE
                binding.signedInLayout.visibility = View.GONE
            }
        }
        binding.usernameEditText.setText(settingsViewModel.usernameLiveData.value ?: "")
        binding.usernameEditText.doOnTextChanged { text, start, before, count ->
            settingsViewModel.changeUserName(text.toString())
        }
        binding.saveButton.setOnClickListener {
            settingsViewModel.save()
        }

        binding.savedBattlesButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedBattlesManagementFragment)
        }

        binding.logOutButton.setOnClickListener {
            settingsViewModel.logout()
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_registerFragment)
        }

        binding.changePasswordButton.setOnClickListener {

        }
    }


}