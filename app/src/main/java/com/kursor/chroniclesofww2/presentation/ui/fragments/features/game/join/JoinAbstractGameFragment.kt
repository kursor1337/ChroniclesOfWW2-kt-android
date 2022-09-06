package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.join

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.FragmentJoinGameBinding
import com.kursor.chroniclesofww2.domain.repositories.AccountRepository
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.phelat.navigationresult.BundleFragment
import org.koin.android.ext.android.inject

abstract class JoinAbstractGameFragment : BundleFragment() {


    protected lateinit var binding: FragmentJoinGameBinding

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

        obtainGamesList()

        binding.retryTextView.setOnClickListener { obtainGamesList() }
    }

    abstract fun checkConditionsForGame(): Boolean

    abstract fun obtainGamesList()

    fun showError() {
        binding.notConnectedLayout.visibility = View.VISIBLE
        binding.gamesRecyclerView.visibility = View.GONE
    }

    fun showList() {
        binding.notConnectedLayout.visibility = View.GONE
        binding.gamesRecyclerView.visibility = View.VISIBLE

    }

    protected fun buildMessageWaitingForAccepted(
        onPositiveButtonClickListener: DialogInterface.OnClickListener?,
        onNegativeButtonClickListener: DialogInterface.OnClickListener?,
        onCancelListener: DialogInterface.OnCancelListener?
    ) {
        val dialog: SimpleDialogFragment = SimpleDialogFragment.Builder(activity)
            .setMessage(R.string.waiting_for_accepted)
            .setNegativeButton(
                R.string.cancel_request_for_accepted,
                onNegativeButtonClickListener
            )
//            { dialog, which ->
//                Tools.currentConnection!!.send(CANCEL_CONNECTION)
//                dialog.dismiss()
//            }
            .build()
        dialog.show(parentFragmentManager, "WaitingForAccepted")
    }

    companion object {
        const val PASSWORD_REQUEST_ID = 101
    }

}