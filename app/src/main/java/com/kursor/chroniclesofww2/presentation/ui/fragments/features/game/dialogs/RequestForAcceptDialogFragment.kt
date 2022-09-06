package com.kursor.chroniclesofww2.presentation.ui.fragments.features.game.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.kursor.chroniclesofww2.game.CreateGameStatus
import com.kursor.chroniclesofww2.presentation.ui.dialogs.SimpleDialogFragment
import com.kursor.chroniclesofww2.viewModels.game.dialog.RequestForAcceptViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestForAcceptDialogFragment : DialogFragment() {

    private lateinit var requestingUserName: String
    private var navGraphId = 0

    val viewModel by viewModel<RequestForAcceptViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navGraphId = requireArguments().getInt(NAV_GRAPH_ID)
        requestingUserName = requireArguments().getString(REQUESTING_USER_NAME)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = AlertDialog.Builder(requireContext())
//            .setMessage("Waiting for connections")
//            .setCancelable(false)
//            .setNegativeButton("Refuse", )
////            { dialog, which ->
////                connection.send(Const.connection.REJECTED)
////            }
//            .setPositiveButton("Allow", )
////            { dialog, which ->
////                connection.send(Const.connection.ACCEPTED)
////            }
//            //{ connection.send(Const.connection.REJECTED) }
//            .build()


        return super.onCreateDialog(savedInstanceState)
    }

    fun setLiveData(liveData: LiveData<Pair<CreateGameStatus, Any?>>) {
        viewModel.statusLiveData = liveData
    }

    companion object {
        const val NAV_GRAPH_ID = "nav graph id"
        const val REQUESTING_USER_NAME = "requesting user name"
    }

}