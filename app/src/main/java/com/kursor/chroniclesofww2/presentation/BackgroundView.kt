package com.kursor.chroniclesofww2.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kursor.chroniclesofww2.R


class BackgroundView(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    
    private var backgroundBitmap: Bitmap
    private lateinit var backgroundThread: BackgroundThread
    private var scaled = false

    init {
        backgroundBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.world_4k)
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        backgroundThread = BackgroundThread(holder)
        backgroundThread.setRunning(true)
        backgroundThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        backgroundThread.setRunning(false)
        while (retry) {
            try {
                backgroundThread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    internal inner class BackgroundThread(private var surfaceHolder: SurfaceHolder) : Thread() {
        @Volatile
        private var running = false
        fun setRunning(b: Boolean) {
            running = b
        }

        override fun run() {
            var dx = 2
            var dy = 1
            val width = width
            val height = height
            if (!scaled) {
                backgroundBitmap = checkBitmap(backgroundBitmap, width, height)
            }
            val top = backgroundBitmap.height / 3
            val left = backgroundBitmap.width / 3
            var canvas: Canvas?
            val dst = Rect(0, 0, width, height)
            val src = Rect(left, top, left + width, top + height)
            while (running) {
                canvas = null
                try {
                    val then = System.currentTimeMillis()
                    canvas = surfaceHolder.lockCanvas()
                    if (canvas == null) continue
                    canvas.drawBitmap(backgroundBitmap, src, dst, null)
                    if (backgroundBitmap.width < src.right && dx > 0) dx = -dx
                    if (src.left < 0 && dx < 0) dx = -dx
                    if (backgroundBitmap.height < src.bottom && dy > 0) dy = -dy
                    if (src.top < 0 && dy < 0) dy = -dy
                    src.offset(dx, dy)
                    val now = System.currentTimeMillis()
                    val dif = now - then
                    if (dif < 20) {
                        sleep(20 - dif)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }

        private fun checkBitmap(oldBitmap: Bitmap, width: Int, height: Int): Bitmap {
            var bitmap = oldBitmap
            while (bitmap.height < height / 2) {
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    bitmap.width * 3 / 2, bitmap.height * 3 / 2, false
                )
            }
            while (bitmap.height / 2 > height) {
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    bitmap.width * 2 / 3, bitmap.height * 2 / 3, false
                )
            }
            return bitmap
        }
    }
}