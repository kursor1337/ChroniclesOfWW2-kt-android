package com.kursor.chroniclesofww2.view

import android.content.Context
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
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
}

class TileView(
    tile: Tile,
    private val context: Context,
    val imageView: ImageView = ImageView(context)
) {

    init {
        tile.listener = object : Tile.Listener {
            override fun onDivisionSet(division: Division) {
                TODO("Not yet implemented")
            }

            override fun onTileCleared() {
                TODO("Not yet implemented")
            }

        }
    }
}