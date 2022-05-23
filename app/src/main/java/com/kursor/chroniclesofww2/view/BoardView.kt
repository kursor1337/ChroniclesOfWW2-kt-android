package com.kursor.chroniclesofww2.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.model.board.Board
import com.kursor.chroniclesofww2.model.board.Division
import com.kursor.chroniclesofww2.model.board.Tile

class BoardView(
    board: Board,
    tileListener: Tile.Listener,
    private val context: Context,
    private val tableLayout: TableLayout
) {

    private val tileViews: List<List<TileView>> = List(board.height) { i ->
        List(board.width) { j ->
            TileView(
                board[i, j],
                context
            )
        }
    }

    init {
        val tileWidth = minOf(
            Tools.getScreenWidth() / board.width,
            Tools.getScreenHeight() / board.height
        )
        val tableRowLayoutParams =
            TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, tileWidth)
        val tileViewLayoutParams = TableRow.LayoutParams(tileWidth, tileWidth)
        for (i in 0 until board.height) {
            val currentTableRow = TableRow(context).apply { layoutParams = tableRowLayoutParams }
            tableLayout.addView(currentTableRow)
            for (j in 0 until board.width) {
                currentTableRow.addView(tileViews[i][j].imageView.apply {
                    layoutParams = tileViewLayoutParams
                })
            }
        }
    }

    fun showLegalMoves(motionMoveList: List<MotionMove>) {
        motionMoveList.forEach { move ->
            val dest = move.destination
            val state = if (move.isAttack) TileView.State.ATTACKED
            else TileView.State.LEGAL
            tileViews[dest.row][dest.column].state = state
        }
    }
}

class TileView(
    tile: Tile,
    private val context: Context,
    val imageView: ImageView = ImageView(context)
) {

    enum class State {
        NORMAL, ATTACKED, LEGAL
    }

    private var isFogged = true

    var state: State = State.NORMAL
        set(value) {
            onStateChanged(field, value)
            field = value
        }

    private val layerMap = mutableMapOf<Int, Drawable>()
    private var layerDrawable = LayerDrawable(emptyArray())


    init {
        state = State.NORMAL
        tile.listener = object : Tile.Listener {
            override fun onDivisionSet(division: Division) {
                Log.i("TileView", "Callback from Tile")
                Log.i(
                    "TileView",
                    "drawable resource = " + Division.getDrawableResource(division.type)
                )
                addLayer(Division.getDrawableResource(division.type))
            }

            override fun onTileCleared() {
                Log.i("TileView", "Callback from Tile")
                clear()
            }

        }

        imageView.id = tile.coordinate
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
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
        imageView.setImageResource(0)
        imageView.setImageDrawable(layerDrawable)
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
        imageView.setImageDrawable(layerDrawable)
    }

    private fun removeLayer(@DrawableRes id: Int) {
        val d = layerMap.remove(id) ?: return
        d.alpha = 0
        Log.i("TileView", d.toString() + "")
        val ds = layerMap.values.toTypedArray()
        layerDrawable = LayerDrawable(ds)
    }

    fun isFogged(): Boolean {
        return isFogged
    }

    fun setFog() {
        isFogged = true
    }

    fun removeFog() {
        isFogged = false
    }

}