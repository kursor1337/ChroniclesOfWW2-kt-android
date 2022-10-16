package com.kursor.chroniclesofww2.presentation.hudViews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import com.kursor.chroniclesofww2.model.game.board.Board
import com.kursor.chroniclesofww2.model.game.moves.AddMove
import com.kursor.chroniclesofww2.model.game.moves.MotionMove
import com.kursor.chroniclesofww2.model.game.moves.Move
import com.kursor.chroniclesofww2.objects.Tools

class BoardView(
    context: Context,
    attributeSet: AttributeSet? = null
) : TableLayout(context, attributeSet) {

    lateinit var tileViews: List<List<TileView>>

    var previousMove: Pair<TileView?, TileView?> = null to null

    fun init(board: Board, meInitiator: Boolean) {
        Log.d(TAG, "init: Start")
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
            Tools.getScreenHeight() * 2 / board.height / 3
        )

        gravity = if (tileWidth * board.width < Tools.getScreenWidth()) Gravity.CENTER_HORIZONTAL
        else Gravity.CENTER_VERTICAL
        val tableRowLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, tileWidth)
        val tileViewLayoutParams = TableRow.LayoutParams(tileWidth, tileWidth)

        for (i in 0 until board.height) {
            val currentTableRow = TableRow(context).apply {
                layoutParams = tableRowLayoutParams
                gravity = Gravity.CENTER_HORIZONTAL
            }
            this.addView(currentTableRow)
            for (j in 0 until board.width) {
                val currentTileView = if (meInitiator) tileViews[i][j]
                else tileViews[board.height - i - 1][board.width - j - 1]
                currentTableRow.addView(currentTileView.apply {
                    layoutParams = tileViewLayoutParams
                })

            }
        }
        requestLayout()
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
        addMoveList
            .map { move -> move.destination }
            .forEach { tileViews[it.row][it.column].state = TileView.State.LEGAL }
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

    fun showLastMove(move: Move) {
        val from = tileViews[move.destination.row][move.destination.column]
        val to = if (move.type == Move.Type.MOTION) {
            move as MotionMove
            tileViews[move.start.row][move.start.column]
        } else null

        from.showIsPrevious()
        to?.showIsPrevious()
        previousMove.first?.hideIsPrevious()
        previousMove.second?.hideIsPrevious()
        previousMove = from to to
    }

    companion object {
        const val TAG = "BoardView"
    }


}