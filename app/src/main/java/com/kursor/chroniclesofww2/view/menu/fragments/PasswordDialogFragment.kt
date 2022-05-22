package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kursor.chroniclesofww2.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.databinding.DialogPasswordBinding
import com.kursor.chroniclesofww2.view.menu.fragments.abstractGameFragment.JoinAbstractGameFragment
import com.kursor.chroniclesofww2.view.menu.fragments.abstractGameFragment.JoinAbstractGameFragment.Companion.PASSWORD_REQUEST_ID
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp

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
            val text = binding.passwordEditText.text.toString()
            if (text.isBlank()) return@setOnClickListener
            navigateUp(PASSWORD_REQUEST_ID, Bundle().apply {
                putString(PASSWORD, text)
            })
        }
    }

}