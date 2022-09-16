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
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO

class WaitingGameAdapter(
    private val activity: ComponentActivity,
    private val gameList: List<WaitingGameInfoDTO>
) : RecyclerView.Adapter<WaitingGameAdapter.Holder>() {

    private var onItemClickListener: OnItemClickListener? = null

    class Holder(private val activity: Activity, val view: View) :
        RecyclerView.ViewHolder(view),
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
            LayoutInflater.from(activity)
                .inflate(R.layout.adapter_waiting_game, LinearLayout(activity), false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val loginTextView = holder.view.findViewById<TextView>(R.id.login_text_view)
        val idTextView = holder.view.findViewById<TextView>(R.id.game_id_text_view)
        val game = gameList[position]
        loginTextView.text = game.initiatorLogin
        idTextView.text = game.id.toString()
        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.view, position, game
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