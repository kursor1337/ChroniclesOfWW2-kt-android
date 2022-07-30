package com.kursor.chroniclesofww2

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.MenuItem
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

fun Division.getDivisionResId() = when (type) {
    Division.Type.INFANTRY -> R.drawable.unit_infantry
    Division.Type.ARMORED -> R.drawable.unit_armored
    Division.Type.ARTILLERY -> R.drawable.unit_artillery
}

fun MenuItem.setTitleColor(color: Int) {
    val hexColor = Integer.toHexString(color).uppercase(Locale.getDefault())
    val html = "<font color='#$hexColor'>$title</font>"
    this.title = html.parseAsHtml()
}



@Suppress("DEPRECATION")
fun String.parseAsHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}