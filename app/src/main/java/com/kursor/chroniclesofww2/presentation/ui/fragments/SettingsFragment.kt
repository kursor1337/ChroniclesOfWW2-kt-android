package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentSettingsBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.domain.useCases.user.LogoutUseCase
import com.kursor.chroniclesofww2.features.LoginReceiveDTO
import com.kursor.chroniclesofww2.features.LoginResponseDTO
import com.kursor.chroniclesofww2.features.Routes
import com.kursor.chroniclesofww2.objects.Const
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    val accountRepository by inject<AccountRepository>()
    val httpClient by inject<HttpClient>()

    val logoutUseCase by inject<LogoutUseCase>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.usernameEditText.setText(accountRepository.username)
        binding.saveButton.setOnClickListener {
            if (accountRepository.username == binding.usernameEditText.text.toString()) return@setOnClickListener
            accountRepository.username = binding.usernameEditText.text.toString()
        }

        binding.savedBattlesButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedBattlesManagementFragment)
        }

        binding.logOutButton.setOnClickListener {
            logoutUseCase()
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_registerFragment)
        }

//        accountRepository.tokenLiveData.observe(viewLifecycleOwner) { token ->
//            viewLifecycleOwner.lifecycleScope.launch {
//                if (isSignedIn()) {
//                    binding.notSignedInLayout.visibility = View.GONE
//                    binding.signedInLayout.visibility = View.VISIBLE
//                    binding.loginTextView.text = accountRepository.login
//                } else {
//                    binding.notSignedInLayout.visibility = View.VISIBLE
//                    binding.signedInLayout.visibility = View.GONE
//                }
//            }
//        }

    }

    suspend fun isSignedIn(): Boolean {
        return tokenValid() || credentialsValid()
    }

    suspend fun tokenValid(): Boolean {
        if (accountRepository.token == null) return false
        val response =
            httpClient.post(
                Routes.Account.AUTH.absolutePath(Const.connection.FULL_SERVER_URL)
            ) {
                bearerAuth(accountRepository.token ?: return false)
            }
        return response.status == HttpStatusCode.OK
    }

    suspend fun credentialsValid(): Boolean {
        if (accountRepository.login == null || accountRepository.password == null) return false
        val response =
            httpClient.post(
                Routes.Users.LOGIN.absolutePath(Const.connection.FULL_SERVER_URL)
            ) {
                setBody(
                    LoginReceiveDTO(
                        login = accountRepository.login ?: return false,
                        password = accountRepository.password ?: return false
                    )
                )
            }
        val loginResponseDTO = response.body<LoginResponseDTO>()
        if (loginResponseDTO.token != null) {
            accountRepository.token = loginResponseDTO.token
            return true
        }
        return false
    }

}