package com.kursor.chroniclesofww2.view.menu.hudViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.Tools
import com.kursor.chroniclesofww2.model.Player
import com.kursor.chroniclesofww2.model.board.Board
import com.kursor.chroniclesofww2.model.board.Division
import com.kursor.chroniclesofww2.model.board.MotionMove
import com.kursor.chroniclesofww2.model.board.Tile

class BoardView(
    context: Context,
    attributeSet: AttributeSet? = null
) : TableLayout(context, attributeSet) {

    var board: Board? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }


    private lateinit var tileViews: List<List<TileView>>

    private fun init(board: Board) {
        tileViews = List(board.height) { i ->
            List(board.width) { j ->
                TileView(context).apply {
                    tile = board[i, j]
                    setOnClickListener {
                        val y = it.id / 10
                        val x = it.id % 10
                        onTileViewClickListener.onClick(y, x, this)
                    }
                }
            }
        }

        val tileWidth = minOf(
            Tools.getScreenWidth() / board.width,
            Tools.getScreenHeight() / board.height
        )
        val tableRowLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, tileWidth)
        val tileViewLayoutParams = TableRow.LayoutParams(tileWidth, tileWidth)
        for (i in 0 until board.height) {
            val currentTableRow = TableRow(context).apply { layoutParams = tableRowLayoutParams }
            addView(currentTableRow)
            for (j in 0 until board.width) {
                currentTableRow.addView(tileViews[i][j].apply {
                    layoutParams = tileViewLayoutParams
                })
            }
        }
    }

    fun showPossibleMoves(motionMoveList: List<MotionMove>) {
        motionMoveList.forEach { move ->
            val dest = move.destination
            val state = if (move.isAttack) TileView.State.ATTACKED
            else TileView.State.LEGAL
            tileViews[dest.row][dest.column].state = state
        }
    }

    fun calculateAndShowPossibleMoves(i: Int, j: Int, playerName: String) {
        showPossibleMoves(board!!.calculatePossibleMoves(i, j, playerName))
    }

    fun hideLegalMoves() {
        tileViews.forEach { tileRow ->
            tileRow.forEach { tileView ->
                tileView.state = TileView.State.NORMAL
            }
        }
    }

    fun setOnTileViewClickListener(onTileViewClickListener: TileView.OnTileViewClickListener) {
        tileViews.forEach { tileRow ->
            tileRow.forEach { tile ->
                tile.onTileViewClickListener = onTileViewClickListener
            }
        }
    }

}