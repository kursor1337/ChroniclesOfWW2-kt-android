package com.kursor.chroniclesofww2.presentation.hudViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import com.kursor.chroniclesofww2.objects.Tools
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove

class BoardView(
    context: Context,
    attributeSet: AttributeSet? = null
) : TableLayout(context, attributeSet) {

    var board: Board? = null
        set(value) {
            if (value != null) init(value)
            field = value
        }


    lateinit var tileViews: List<List<TileView>>

    private fun init(board: Board) {
        Log.i(TAG, "init: Start")
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
        Log.i(TAG, "init: tileWidth = $tileWidth")
        val tableRowLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, tileWidth)
        val tileViewLayoutParams = TableRow.LayoutParams(tileWidth, tileWidth)
        for (i in 0 until board.height) {
            val currentTableRow = TableRow(context).apply { layoutParams = tableRowLayoutParams }
            Log.i(TAG, "init: New TileRow, index = $i")
            this.addView(currentTableRow)
            for (j in 0 until board.width) {
                currentTableRow.addView(tileViews[i][j].apply {
                    layoutParams = tileViewLayoutParams
                })

            }
        }
        invalidate()
    }

    fun showPossibleMotionMoves(motionMoveList: List<MotionMove>) {
        motionMoveList.forEach { move ->
            val dest = move.destination
            val state = if (move.isAttack) TileView.State.ATTACKED
            else TileView.State.LEGAL
            tileViews[dest.row][dest.column].state = state
        }
    }

    fun showPossibleAddMoves(addMoveList: List<AddMove>) {
        addMoveList.forEach { move ->
            val dest = move.destination
            tileViews[dest.row][dest.column].state = TileView.State.LEGAL
        }
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

    companion object {
        const val TAG = "BoardView"
    }


}