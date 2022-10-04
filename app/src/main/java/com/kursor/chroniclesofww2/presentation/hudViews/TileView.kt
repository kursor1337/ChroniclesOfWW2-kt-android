package com.kursor.chroniclesofww2.presentation.hudViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.getDivisionDrawableResId
import com.kursor.chroniclesofww2.model.game.board.Division
import com.kursor.chroniclesofww2.model.game.board.Tile

class TileView(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context, attributeSet) {

    enum class State {
        NORMAL, ATTACKED, LEGAL
    }

    private var isFogged = true //TODO(set and get for drawing fog)

    var state: State = State.NORMAL
        set(value) {
            if (field == value) return
            onStateChanged(field, value)
            field = value
        }

    private val layerMap = mutableMapOf<Int, Drawable>()
    private var layerDrawable = LayerDrawable(emptyArray())
    lateinit var onTileViewClickListener: OnTileViewClickListener

    var tile: Tile? = null
        set(value) {
            if (field != null) return
            if (value != null) init(value)
            field = value
        }

    private fun init(tile: Tile) {
        state = State.NORMAL
        Log.i(TAG, "init: Setting tile listener to the tile with coordinate ${tile.coordinate}")
        val listener = object : Tile.Listener {
            override fun onDivisionSet(division: Division) {
                Log.i(TAG, "onDivisionSet: ")
                Log.i(
                    "TileView",
                    "drawable resource = " + division.getDivisionDrawableResId()
                )
                addLayer(division.getDivisionDrawableResId())
            }

            override fun onTileCleared() {
                Log.i(TAG, "onTileCleared: ")
                clear()
            }

        }
        Log.i(TAG, "init: listener = $listener")
        tile.listener = listener
        id = tile.coordinate
        scaleType = ScaleType.CENTER_INSIDE
        setImage(R.drawable.empty)
    }

    private fun onStateChanged(stateCurrent: State, stateNext: State) {
        when (stateCurrent) {
            State.ATTACKED -> hideIsAttacked()
            State.LEGAL -> hideIsLegal()
            State.NORMAL -> {
                //nothing special here
            }
        }
        when (stateNext) {
            State.ATTACKED -> showIsAttacked()
            State.LEGAL -> showIsLegal()
            State.NORMAL -> {
                //nothing special here
            }
        }
    }

    fun setImage(@DrawableRes id: Int) {
        val drawable = ContextCompat.getDrawable(context, id) ?: return
        Log.i("TileView", "Setting image... $drawable")
        layerMap.clear()
        layerMap[id] = drawable
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
        setImageResource(0)
        setImageDrawable(layerDrawable)
        Log.i(
            "TileView",
            "Set image " + layerDrawable + " length = " + layerDrawable.numberOfLayers
        )
    }

    fun clear() {
        setImage(R.drawable.empty)
    }

    private fun hideIsLegal() {
        Log.i("TileView", "Hide is legal")
        removeLayer(R.drawable.legal)
    }

    private fun hideIsAttacked() {
        Log.i("TileView", "Hide is legal")
        removeLayer(R.drawable.attacked)
    }

    private fun showIsLegal() {
        addLayer(R.drawable.legal)
        Log.i("TileView", "Show is legal")
    }

    private fun showIsAttacked() {
        addLayer(R.drawable.attacked)
        Log.i("TileView", "Show is attacked")
    }

    private fun addLayer(@DrawableRes id: Int) {
        val drawable = ContextCompat.getDrawable(context, id) ?: return
        Log.i("TileView", drawable.toString())
        layerMap[id] = drawable
        Log.i("TileView", "addLayer")
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
        Log.i(
            "TileView",
            "Set image " + layerDrawable + " length = " + layerDrawable.getNumberOfLayers()
        )
        setImageDrawable(layerDrawable)
    }

    private fun removeLayer(@DrawableRes id: Int) {
        val d = layerMap.remove(id) ?: return
        d.alpha = 0
        Log.i("TileView", "removed layer: $d")
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
    }

    fun interface OnTileViewClickListener {
        fun onClick(i: Int, j: Int, tileView: TileView)
    }

    companion object {
        const val TAG = "TileView"
    }

}