package com.kursor.chroniclesofww2.connection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.kursor.chroniclesofww2.R

class HostsAdapter(context: Context, private val hostList: List<Host>) :
    ArrayAdapter<Host>(context, R.layout.listview_missions, hostList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.listview_missions, LinearLayout(context))
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = hostList[position].name
        return view
    }
}