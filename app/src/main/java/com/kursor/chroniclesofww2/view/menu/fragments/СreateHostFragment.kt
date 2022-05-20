package com.kursor.chroniclesofww2.view.menu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kursor.chroniclesofww2.Const
import com.kursor.chroniclesofww2.connection.interfaces.Connection
import com.kursor.chroniclesofww2.connection.local.LocalServer
import com.kursor.chroniclesofww2.databinding.FragmentCreateHostBinding
import com.kursor.chroniclesofww2.model.Scenario

class СreateHostFragment : Fragment() {

    private lateinit var binding: FragmentCreateHostBinding

    val SCENARIO_INFO = "SCENARIO_INFO"

    var currentDialog: DialogFragment? = null

    var chosenScenarioJson = ""
    lateinit var connection: Connection

    private var isHostReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            SCENARIO_INFO, this
        ) { key, bundle ->
            chosenScenarioJson = bundle.getString(Const.game.SCENARIO) ?: ""
            if (chosenScenarioJson.isBlank()) return@setFragmentResultListener
            val scenario = Scenario.fromJson(chosenScenarioJson)
            binding.chosenScenarioTextView.text = scenario.name
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nsdHelper = NsdHelper(requireActivity(), broadcastListener)
        binding.chooseScenarioButton.setOnClickListener(View.OnClickListener {
            findNavController()
//            menuActivity.changeFragment(
//                JavaMissionFragment(),
//                true,
//                false
//            )
        })
        binding.readyButton.setOnClickListener(View.OnClickListener { v ->
            if (chosenScenarioJson.isBlank()) return@OnClickListener
            server = LocalServer(
                binding.hostPasswordEditText.text.toString(),
                Connection.EMPTY_SEND_LISTENER,
                receiveListener,
                serverListener
            )
            server.startListening()
            v.isEnabled = false
            isHostReady = true
        })
    }
}