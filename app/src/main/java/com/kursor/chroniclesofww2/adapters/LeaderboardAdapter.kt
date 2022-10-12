package com.kursor.chroniclesofww2.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.databinding.AdapterLeaderboardBinding
import com.kursor.chroniclesofww2.features.UserInfo
import com.kursor.chroniclesofww2.features.WaitingGameInfoDTO

class LeaderboardAdapter(
    private val activity: Activity,
    private val leaderBoard: List<UserInfo>
) : RecyclerView.Adapter<LeaderboardAdapter.Holder>() {

    private var onItemClickListener: OnItemClickListener? = null

    class Holder(private val activity: Activity, val binding: AdapterLeaderboardBinding) :
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
            AdapterLeaderboardBinding.inflate(
                LayoutInflater.from(activity),
                LinearLayout(activity),
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = leaderBoard[position]
        holder.binding.positionTextView.text = (position + 1).toString()
        holder.binding.scoreTextView.text = user.score.toString()
        holder.binding.usernameTextView.text = user.username
    }

    override fun getItemCount(): Int = leaderBoard.size


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, waitingGameInfoDTO: WaitingGameInfoDTO)
    }

}