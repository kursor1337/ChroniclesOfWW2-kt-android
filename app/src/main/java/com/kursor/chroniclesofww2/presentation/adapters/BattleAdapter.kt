package com.kursor.chroniclesofww2.presentation.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.RecyclerviewBattlesBinding
import com.kursor.chroniclesofww2.model.data.Battle

class BattleAdapter(
    private val activity: Activity,
    private val battleList: List<Battle>
) : RecyclerView.Adapter<BattleAdapter.BattleHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    var contextMenuPosition: Int = 0
        private set


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class BattleHolder(private val activity: Activity, val binding: RecyclerviewBattlesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            activity.menuInflater.inflate(R.menu.menu_delete, menu)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattleHolder {
        return BattleHolder(
            activity,
            RecyclerviewBattlesBinding.inflate(
                LayoutInflater.from(activity),
                LinearLayout(activity),
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BattleHolder, position: Int) {
        holder.binding.battleNameTextView.text = battleList[position].name
        holder.binding.root.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.binding.root, position, battleList[position]
            )
        }
        holder.binding.root.setOnLongClickListener {
            contextMenuPosition = holder.adapterPosition
            false
        }
    }

    override fun getItemCount(): Int = battleList.size

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, battle: Battle)
    }
}