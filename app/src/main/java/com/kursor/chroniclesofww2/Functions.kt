package com.kursor.chroniclesofww2

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.kursor.chroniclesofww2.model.game.Nation
import com.kursor.chroniclesofww2.model.game.board.Division
import java.util.*

fun Nation.getFlagResId() = when (this) {
    Nation.GERMANY -> R.drawable.nation_germany
    Nation.BRITAIN -> R.drawable.nation_britain
    Nation.FRANCE -> R.drawable.nation_france
    Nation.JAPAN -> R.drawable.nation_japan
    Nation.USSR -> R.drawable.nation_ussr
    Nation.USA -> R.drawable.nation_usa
    Nation.ITALY -> R.drawable.nation_italy
    Nation.DEFAULT -> R.drawable.nation_default
}

fun Nation.getNationNameStringResId() = when (this) {
    Nation.DEFAULT -> R.string.nation_default
    Nation.BRITAIN -> R.string.nation_britain
    Nation.FRANCE -> R.string.nation_france
    Nation.GERMANY -> R.string.nation_germany
    Nation.ITALY -> R.string.nation_italy
    Nation.JAPAN -> R.string.nation_japan
    Nation.USA -> R.string.nation_usa
    Nation.USSR -> R.string.nation_ussr
}

fun Division.getDivisionDrawableResId() = type.getDivisionTypeDrawableResId()

fun Division.Type.getDivisionTypeDrawableResId() = when (this) {
    Division.Type.INFANTRY -> R.drawable.unit_infantry
    Division.Type.ARMORED -> R.drawable.unit_armored
    Division.Type.ARTILLERY -> R.drawable.unit_artillery
}

fun Division.Type.getDivisionTypeNameResId() = when (this) {
    Division.Type.INFANTRY -> R.string.infantry
    Division.Type.ARMORED -> R.string.armored
    Division.Type.ARTILLERY -> R.string.artillery
}


fun MenuItem.setTitleColor(color: Int) {
    val hexColor = Integer.toHexString(color).uppercase(Locale.getDefault())
    val html = "<font color='#$hexColor'>$title</font>"
    this.title = html.parseAsHtml()
}

@Suppress("DEPRECATION")
fun String.parseAsHtml(): Spanned {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}


fun drawNationBattleDataOnLayout(
    context: Context,
    nation: Nation,
    divisionResources: Map<Division.Type, Int>,
    layoutToDrawOn: LinearLayout,
    inverseViews: Boolean
) {

    layoutToDrawOn.addView(LinearLayout(context).apply {
        setPadding(20)
        orientation = LinearLayout.VERTICAL
        addView(
            TextView(context).apply {
                setText(nation.getNationNameStringResId())
                setTextColor(Color.BLACK)
            }
        )
        divisionResources.forEach { (type, quantity) ->
            addView(LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                if (inverseViews) {
                    this.addView(
                        TextView(context).apply {
                            text = quantity.toString()
                            setTextColor(Color.BLACK)
                        }
                    )
                    this.addView(
                        TextView(context).apply {
                            setText(type.getDivisionTypeNameResId())
                            setTextColor(Color.BLACK)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                            textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
                            setPadding(20, 0, 0, 0)
                        }
                    )
                } else {
                    this.addView(
                        TextView(context).apply {
                            setText(type.getDivisionTypeNameResId())
                            setTextColor(Color.BLACK)
                        }
                    )
                    this.addView(
                        TextView(context).apply {
                            text = quantity.toString()
                            setTextColor(Color.BLACK)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                            textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
                            setPadding(20, 0, 0, 0)
                        }
                    )
                }
            })
        }
    })
}