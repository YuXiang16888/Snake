package com.xiang.snake

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap


class SnakeGameView(context: Context, attrs: AttributeSet?) : View(context, attrs){
    private val BORDERSIZE = 20
    private var snakeHeadBitmap : Bitmap? = null
    private var snakeBodyBitmap : Bitmap? = null
    private var appleFoodBitmap : Bitmap? = null
    private val matrixRotation  = Matrix()
    var snakeBody : List<Position>? = null
    var apple : Position? = null
    var direction : Direction? = null
    private var bodySize = 0

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.game_border)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            snakeBody?.let { position ->
                for(index in position.indices) {
                    if(index == 0) {
                        when(direction) {
                            Direction.UP -> matrixRotation.setRotate(180f)
                            Direction.DOWN -> matrixRotation.setRotate(0f)
                            Direction.LEFT -> matrixRotation.setRotate(90f)
                            Direction.RIGHT -> matrixRotation.setRotate(-90f)
                        }
                        val rotationHeadBitmap = createBitmap(
                            snakeHeadBitmap!!, 0, 0, snakeHeadBitmap!!.width // 寬度
                            , snakeHeadBitmap!!.height // 高度
                            , matrixRotation, true
                        )
                        drawBitmap(rotationHeadBitmap!!,
                            (position[index].x * bodySize).toFloat(),
                            (position[index].y * bodySize).toFloat(),
                            null)
                    } else {
                        drawBitmap(snakeBodyBitmap!!,
                            (position.get(index).x * bodySize).toFloat(),
                            (position.get(index).y * bodySize).toFloat(),
                            null)
                    }
                }
            }
            apple?.let {
                drawBitmap(appleFoodBitmap!!,
                    (apple!!.x * bodySize).toFloat(),
                    (apple!!.y * bodySize).toFloat(),
                    null)
            }

            //border
            for(i in 0..BORDERSIZE) {
                //x
                drawLine(0f,
                    (i * bodySize).toFloat(),
                    (BORDERSIZE * bodySize).toFloat(),
                    (i * bodySize).toFloat(),borderPaint)
                //y
                drawLine((i * bodySize).toFloat(),
                    0f,
                    (i * bodySize).toFloat(),
                    (BORDERSIZE * bodySize).toFloat(),borderPaint)
            }

        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        bodySize = width / BORDERSIZE
        if(snakeHeadBitmap == null) {
            snakeHeadBitmap = ContextCompat.getDrawable(context, R.drawable.img_snake_head)
                ?.toBitmap(bodySize,bodySize)
        }
        if(snakeBodyBitmap == null) {
            snakeBodyBitmap = ContextCompat.getDrawable(context, R.drawable.img_snake_body)
                ?.toBitmap(bodySize-5 ,bodySize-5)
        }

        if(appleFoodBitmap == null) {
            appleFoodBitmap = ContextCompat.getDrawable(context, R.drawable.img_apple)
                ?.toBitmap(bodySize ,bodySize)
        }

    }
}