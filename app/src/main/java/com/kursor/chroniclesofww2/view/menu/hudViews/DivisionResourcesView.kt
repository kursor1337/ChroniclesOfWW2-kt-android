package com.kursor.chroniclesofww2.view.menu.hudViews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.model.DivisionResources
import com.kursor.chroniclesofww2.model.Player
import com.kursor.chroniclesofww2.model.Reserve

class DivisionResourcesView(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    init {
        orientation = HORIZONTAL
    }

    fun setup(player: Player) {
        divisionResources = player.divisionResources
        setBackgroundResource(player.nation.fladResId)
    }

    var divisionResources: DivisionResources? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }

    lateinit var reserveViews: List<ReserveView>

    var clickedReserveView: ReserveView? = null
        set(value) {
            if (field != null) field!!.isClicked = false
            if (value != null) value.isClicked = true
            field = value
        }

    private fun init(divisionResources: DivisionResources) {

        val tempReserveViews = mutableListOf<ReserveView>()
        val width = Tools.getScreenWidth() / reserveViews.size

        divisionResources.reserves.forEach { (_, reserve) ->
            val reserveView = ReserveView(context).apply { this.reserve = reserve }
            tempReserveViews.add(reserveView)

            reserveView.layoutParams = LayoutParams(width, LayoutParams.WRAP_CONTENT)
            addView(reserveView)
            reserveView.setOnClickListener {
                if (clickedReserveView === reserveView) {
                    reserveView.isClicked = false
                    clickedReserveView = null
                }
                clickedReserveView = reserveView
            }
        }
        reserveViews = tempReserveViews
    }
}

class ReserveView(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    var isClicked = false
        set(value) {
            setTextColor(
                if (value) Color.GRAY
                else Color.BLACK
            )
            field = value
        }

    var reserve: Reserve? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }

    private fun init(reserve: Reserve) {
        text = "${reserve.type}: ${reserve.size}"
        reserve.listener = object : Reserve.Listener {
            override fun onGetNewDivision() {
                updateText()
            }

            override fun onCancel() {
                updateText()
            }
        }
    }

    fun updateText() {
        text = "${reserve!!.type}: ${reserve!!.size}"
    }
}

