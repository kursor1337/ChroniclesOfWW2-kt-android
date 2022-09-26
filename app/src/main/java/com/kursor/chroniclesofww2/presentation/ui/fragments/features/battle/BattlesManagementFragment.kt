package com.kursor.chroniclesofww2.presentation.ui.fragments.features.battle

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.adapters.BattleAdapter
import com.kursor.chroniclesofww2.databinding.FragmentBattlesManagementBinding
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
            if (it.isEmpty()) binding.publishedBattlesButton.visibility = View.GONE
            else binding.publishedBattlesButton.visibility = View.VISIBLE
        }

        binding.savedBattlesButton.setOnClickListener {
            battlesManagementViewModel.loadBattleList(BattlesManagementViewModel.DataSource.LOCAL)
        }

        binding.publishedBattlesButton.setOnClickListener {
            battlesManagementViewModel.loadBattleList(BattlesManagementViewModel.DataSource.REMOTE)
        }

        battlesManagementViewModel.battleListLiveData.observe(viewLifecycleOwner) { battleList ->
            battleAdapter = BattleAdapter(requireActivity(), battleList).apply {
                setOnItemClickListener { view, position, battle ->
                    if (battlesManagementViewModel.currentDataSource == BattlesManagementViewModel.DataSource.LOCAL) {
                        battlesManagementViewModel.selectOrUnselectBattleIndex(position)
                    }
                }
            }
            binding.battlesRecyclerView.adapter = battleAdapter
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