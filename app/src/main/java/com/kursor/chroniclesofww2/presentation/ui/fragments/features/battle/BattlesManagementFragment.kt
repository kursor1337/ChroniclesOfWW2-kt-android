package com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.adapters.BattleAdapter
import com.kursor.chroniclesofww2.databinding.FragmentBattlesManagementBinding
import com.kursor.chroniclesofww2.presentation.ui.dialogs.CreateNewBattleDialogFragment
import com.kursor.chroniclesofww2.setTitleColor
import com.kursor.chroniclesofww2.viewModels.features.battle.BattlesManagementViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BattlesManagementFragment : Fragment() {

    lateinit var binding: FragmentBattlesManagementBinding

    lateinit var battleAdapter: BattleAdapter

    val battlesManagementViewModel by viewModel<BattlesManagementViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattlesManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        battlesManagementViewModel.selectedBattleIndexesListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) binding.publishBattlesImageButton.visibility = View.GONE
            else binding.publishBattlesImageButton.visibility = View.VISIBLE
        }

        binding.publishedBattlesButton.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray
            )
        )

        binding.savedBattlesButton.setOnClickListener {
            battlesManagementViewModel.loadBattleList(BattlesManagementViewModel.DataSource.LOCAL)
            binding.savedBattlesButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.publishedBattlesButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            )
        }

        binding.publishedBattlesButton.setOnClickListener {
            battlesManagementViewModel.loadBattleList(BattlesManagementViewModel.DataSource.REMOTE)
            binding.publishedBattlesButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            binding.savedBattlesButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            )
        }

        battlesManagementViewModel.battleListLiveData.observe(viewLifecycleOwner) { battleList ->
            battleAdapter = BattleAdapter(requireActivity(), battleList).apply {
                setOnItemClickListener { view, position, battle ->
                    if (battlesManagementViewModel.currentDataSource == BattlesManagementViewModel.DataSource.LOCAL) {
                        battlesManagementViewModel.selectOrUnselectBattleIndex(position)
                        if (battlesManagementViewModel
                                .selectedBattleIndexesListLiveData
                                .value?.contains(position) == true
                        ) view.setBackgroundColor(Color.LTGRAY)
                        else view.setBackgroundColor(0)
                    }
                }
            }
            binding.battlesRecyclerView.adapter = battleAdapter
        }

        binding.newBattleButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_battlesManagementFragment_to_createNewBattleDialogFragment4,
                Bundle().apply {
                    putInt(
                        CreateNewBattleDialogFragment.NAVIGATION_GRAPH_ID,
                        R.id.navigation_settings
                    )
                }
            )
        }

        binding.battlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        registerForContextMenu(binding.battlesRecyclerView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menu_delete, menu)
        menu.findItem(R.id.delete).setTitleColor(0x000000)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = battleAdapter.contextMenuPosition
        when (item.itemId) {
            R.id.delete -> {
                battlesManagementViewModel.deleteBattleByPosition(pos)
                battleAdapter.notifyItemRemoved(pos)
            }
        }
        return true
    }

}