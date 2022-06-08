package com.kursor.chroniclesofww2.view.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.connection.Host

class HostAdapter(
    private val activity: Activity,
    private val hostList: List<com.kursor.chroniclesofww2.connection.Host>
) : RecyclerView.Adapter<HostAdapter.HostHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class HostHolder(private val activity: Activity, val view: View) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostHolder {
        return HostHolder(
            activity,
            LayoutInflater.from(activity)
                .inflate(R.layout.recyclerview_hosts, LinearLayout(activity), false)
        )
    }

    override fun onBindViewHolder(holder: HostHolder, position: Int) {
        val textView = holder.view.findViewById<TextView>(android.R.id.text1)
        textView.text = hostList[position].name
        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.view, position, hostList[position]
            )
        }
    }

    override fun getItemCount(): Int = hostList.size


    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, host: com.kursor.chroniclesofww2.connection.Host)
    }
}