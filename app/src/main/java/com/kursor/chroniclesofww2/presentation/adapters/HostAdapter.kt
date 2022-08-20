package com.kursor.chroniclesofww2.presentation.adapters

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
import com.kursor.chroniclesofww2.domain.connection.Host

class HostAdapter(
    private val activity: ComponentActivity,
    private val hostList: List<Host>
) : RecyclerView.Adapter<HostAdapter.HostHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

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
        val host = hostList[position]
        textView.text = host.name
        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.view, position, host
            )
        }
    }

    override fun getItemCount(): Int = hostList.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, host: Host)
    }
}