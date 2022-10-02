package com.kursor.chroniclesofww2.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.databinding.AdapterWaitingGameBinding
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import com.kursor.chroniclesofww2.model.game.board.Division

class WaitingGameAdapter(
    private val activity: ComponentActivity,
    private val gameList: List<WaitingGameInfoDTO>
) : RecyclerView.Adapter<WaitingGameAdapter.Holder>() {

    private var onItemClickListener: OnItemClickListener? = null

    class Holder(private val activity: Activity, val binding: AdapterWaitingGameBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnCreateContextMenuListener {

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            //activity.menuInflater.inflate(R.menu.menu_delete, menu)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            activity,
            AdapterWaitingGameBinding.inflate(
                LayoutInflater.from(activity),
                LinearLayout(activity),
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val game = gameList[position]
        holder.binding.loginTextView.text = game.initiatorLogin
        holder.binding.gameIdTextView.text = game.id.toString()

        holder.binding.nation1TextView.text = game.battleData.nation1.toString()
        holder.binding.nation2TextView.text = game.battleData.nation2.toString()

        holder.binding.infantry1TextView.text =
            game.battleData.nation1divisions[Division.Type.INFANTRY].toString()
        holder.binding.armored1TextView.text =
            game.battleData.nation1divisions[Division.Type.ARMORED].toString()
        holder.binding.artillery1TextView.text =
            game.battleData.nation1divisions[Division.Type.ARTILLERY].toString()

        holder.binding.infantry2TextView.text =
            game.battleData.nation2divisions[Division.Type.INFANTRY].toString()
        holder.binding.armored2TextView.text =
            game.battleData.nation2divisions[Division.Type.ARMORED].toString()
        holder.binding.artillery2TextView.text =
            game.battleData.nation2divisions[Division.Type.ARTILLERY].toString()

        holder.binding.root.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.binding.root, position, game
            )
        }
    }

    override fun getItemCount(): Int = gameList.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, waitingGameInfoDTO: WaitingGameInfoDTO)
    }
}