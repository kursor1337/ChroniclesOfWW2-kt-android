package com.kursor.chroniclesofww2.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.databinding.AdapterWaitingGameBinding
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO
import com.kursor.chroniclesofww2.getDivisionTypeNameResId
import com.kursor.chroniclesofww2.getNationNameStringResId
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import org.w3c.dom.Text

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

        drawNationBattleDataOnLayout(
            nation = game.battleData.nation1,
            divisionResources = game.battleData.nation1divisions,
            layoutToDrawOn = holder.binding.battleDataLayout,
            inverseViews = false
        )

        drawNationBattleDataOnLayout(
            nation = game.battleData.nation2,
            divisionResources = game.battleData.nation2divisions,
            layoutToDrawOn = holder.binding.battleDataLayout,
            inverseViews = true
        )

        holder.binding.root.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.binding.root, position, game
            )
        }
    }

    override fun getItemCount(): Int = gameList.size

    private fun drawNationBattleDataOnLayout(
        nation: Nation,
        divisionResources: Map<Division.Type, Int>,
        layoutToDrawOn: LinearLayout,
        inverseViews: Boolean
    ) {

        layoutToDrawOn.addView(TextView(activity).apply { setText(nation.getNationNameStringResId()) })

        divisionResources.forEach { (type, quantity) ->
            layoutToDrawOn.addView(LinearLayout(activity).apply {
                orientation = LinearLayout.HORIZONTAL
                if (inverseViews) {
                    this.addView(TextView(activity).apply { text = quantity.toString() })
                    this.addView(TextView(activity).apply { setText(type.getDivisionTypeNameResId()) })
                } else {
                    this.addView(TextView(activity).apply { setText(type.getDivisionTypeNameResId()) })
                    this.addView(TextView(activity).apply { text = quantity.toString() })
                }
            })
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, waitingGameInfoDTO: WaitingGameInfoDTO)
    }
}