package com.kursor.chroniclesofww2.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.data.repositories.battleRepositories.LocalCustomBattleRepository
import com.kursor.chroniclesofww2.databinding.FragmentSavedBattlesManagementBinding
import com.kursor.chroniclesofww2.presentation.adapters.BattleAdapter
import com.kursor.chroniclesofww2.setTitleColor
import com.kursor.chroniclesofww2.viewModels.BattleListViewModel
import org.koin.android.ext.android.inject

class SavedBattlesManagementFragment : Fragment() {

    lateinit var binding: FragmentSavedBattlesManagementBinding

    val localCustomBattleRepository by inject<LocalCustomBattleRepository>()
    lateinit var battleAdapter: BattleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBattlesManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedBattlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        battleAdapter = BattleAdapter(requireActivity(), localCustomBattleRepository.battleList)
        binding.savedBattlesRecyclerView.adapter = battleAdapter
        registerForContextMenu(binding.savedBattlesRecyclerView)
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
                val battle = localCustomBattleRepository.battleList[pos]
                localCustomBattleRepository.deleteBattle(battle)
                battleAdapter.notifyItemRemoved(pos)
            }
        }
        return true
    }

}