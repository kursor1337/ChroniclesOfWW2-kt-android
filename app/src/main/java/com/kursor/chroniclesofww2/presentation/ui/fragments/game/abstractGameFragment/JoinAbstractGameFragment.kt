package com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kursor.chroniclesofww2.objects.Const.connection.ACCEPTED
import com.kursor.chroniclesofww2.objects.Const.connection.CANCEL_CONNECTION
import com.kursor.chroniclesofww2.objects.Const.connection.CLIENT
import com.kursor.chroniclesofww2.objects.Const.connection.CONNECTED_DEVICE
import com.kursor.chroniclesofww2.objects.Const.connection.HOST_IS_WITH_PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.PASSWORD
import com.kursor.chroniclesofww2.objects.Const.connection.REJECTED
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_FOR_ACCEPT
import com.kursor.chroniclesofww2.objects.Const.connection.REQUEST_GAME_DATA
import com.kursor.chroniclesofww2.objects.Const.game.MULTIPLAYER_GAME_MODE
import com.kursor.chroniclesofww2.objects.Const.game.BATTLE
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.domain.connection.LocalClient
import com.kursor.chroniclesofww2.domain.connection.Connection
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.presentation.ui.activities.GameActivity
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

abstract class JoinAbstractGameFragment : BundleFragment() {


    private lateinit var binding: FragmentJoinGameBinding

    abstract val actionToPasswordDialogFragmentId: Int
    abstract val clientInitErrorMessageResId: Int


    val accountRepository by inject<AccountRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clientInitErrorTextView.setText(clientInitErrorMessageResId)

        tryToInitClient()

        binding.retryTextView.setOnClickListener { tryToInitClient() }
    }

    abstract fun initClient()

    abstract fun checkConditionsForClientInit(): Boolean

    abstract fun tryToInitClient()

    fun showError() {
        binding.notConnectedLayout.visibility = View.VISIBLE
        binding.gamesRecyclerView.visibility = View.GONE
    }

    fun showList() {
        binding.notConnectedLayout.visibility = View.GONE
        binding.gamesRecyclerView.visibility = View.VISIBLE

    }

    private fun buildMessageWaitingForAccepted() {
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(activity)
            .setMessage(R.string.waiting_for_accepted)
            .setNegativeButton(
                R.string.cancel_request_for_accepted
            ) { dialog, which ->
                Tools.currentConnection!!.send(CANCEL_CONNECTION)
                dialog.dismiss()
            }.build()
        dialog.show(parentFragmentManager, "WaitingForAccepted")
    }

    companion object {
        const val PASSWORD_REQUEST_ID = 101
    }

}