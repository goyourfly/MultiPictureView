package com.goyourfly.multi_picture

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.MotionEvent


/**
 * Created by gaoyufei on 2017/6/23.
 */
internal class CustomImageView(context: Context,
                               val index: Int,
                               val bitmap: Bitmap?,
                               val deleteCallback: MultiPictureView.DeleteClickCallback? = null) : ImageView(context) {
    var startX = 0F
    var startY = 0F
    var moveX = 0F
    var moveY = 0F
    var startTime = 0L
    val CLICK_ACTION_THRESHHOLD = 20
    var isTouching = false

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val deleteRect = Rect()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (index >= 0 && bitmap != null && !bitmap.isRecycled) {
            deleteRect.set((width - bitmap.width), 0, width, bitmap.height)
            canvas.drawBitmap(bitmap, (width - bitmap.width).toFloat(), 0F, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouching = true
                startX = event.x
                startY = event.y
                moveX = startX
                moveY = startY
                startTime = System.currentTimeMillis()
            }

            MotionEvent.ACTION_MOVE -> {
                moveX = event.x
                moveY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y
                if (isTouching && isAClick(startX, endX, startY, endY)) {
                    if (deleteRect.contains(event.x.toInt(), event.y.toInt())) {
                        deleteCallback?.onDeleted(index)
                        return false
                    } else {
                        performClick()
                        return false
                    }
                }
                isTouching = false
            }

            MotionEvent.ACTION_CANCEL -> {
                isTouching = false
            }
        }
        return true
    }


    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        if (differenceX > CLICK_ACTION_THRESHHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHHOLD) {
            return false
        }
        return true
    }

}