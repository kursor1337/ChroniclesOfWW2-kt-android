package com.kursor.chroniclesofww2.view.menu.gameMenu

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.kursor.chroniclesofww2.model.DivisionResources

class DivisionResourcesView(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    var divisionResources: DivisionResources? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }

    private fun init(divisionResources: DivisionResources) {

    }

}