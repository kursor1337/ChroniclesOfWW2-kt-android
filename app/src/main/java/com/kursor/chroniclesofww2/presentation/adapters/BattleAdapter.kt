package com.kursor.chroniclesofww2.presentation.adapters

import android.app.Activity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.model.data.Battle

class BattleAdapter(
    private val activity: Activity,
    private val battleList: List<Battle>
) : RecyclerView.Adapter<BattleAdapter.BattleHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class BattleHolder(private val activity: Activity, val view: View) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattleHolder {
        return BattleHolder(
            activity,
            LayoutInflater.from(activity)
                .inflate(R.layout.recyclerview_battles, LinearLayout(activity), false)
        )
    }

    override fun onBindViewHolder(holder: BattleHolder, position: Int) {
        val textView = holder.view.findViewById<TextView>(android.R.id.text1)
        textView.text = battleList[position].name
        holder.view.setOnClickListener {
            onItemClickListener?.onItemClick(
                holder.view, position, battleList[position]
            )
        }
    }

    override fun getItemCount(): Int = battleList.size
//    :
//    ArrayAdapter<Host>(context, R.layout.listview_missions, hostList) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view = convertView ?: LayoutInflater.from(context)
//            .inflate(R.layout.listview_missions, LinearLayout(context))
//        val textView = view.findViewById<TextView>(android.R.id.text1)
//        textView.text = hostList[position].name
//        return view
//    }

    fun interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, battle: Battle)
    }
}