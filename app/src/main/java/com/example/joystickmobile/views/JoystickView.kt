package com.example.joystickmobile.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class JoystickView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radiusSmallCircle: Float = 0f
    private var centerSmallCircle: PointF = PointF()
    private var radiusBigCircle: Float = 0f
    private var centerBigCircle: PointF = PointF()
    private var normalizedX = 0.0f
    private var normalizedY = 0.0f
    public lateinit var onChange: OnJoystickChange

    /** Fill the small circle */
    private val paintSmallCircle = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#808080")
        isAntiAlias = true
    }

    /** Fill the big circle */
    private val paintBigCircle = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
        isAntiAlias = true
    }


    /** Draw the joystick to screen */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerBigCircle.x, centerBigCircle.y, radiusBigCircle, paintBigCircle)
        canvas.drawCircle(
            centerSmallCircle.x,
            centerSmallCircle.y,
            radiusSmallCircle,
            paintSmallCircle
        )
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        radiusSmallCircle = 0.2f * min(width, height).toFloat()
        radiusBigCircle = 0.45f * min(width, height).toFloat()
        centerSmallCircle = PointF(width / 2.0f, height / 2.0f)
        centerBigCircle = PointF(width / 2.0f, height / 2.0f)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        when (event.action) {
            //MotionEvent.ACTION_DOWN -> TODO()
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            // onMouseUP
            MotionEvent.ACTION_UP -> {
                // return the small circle to it initial position, and then render the view
                centerSmallCircle.x = (this.width / 2).toFloat()
                centerSmallCircle.y = (this.height / 2).toFloat()
                invalidate()
                onChange.change(0f, 0f)
            }
        }
        return true
    }

    //Update positions and properties of drawn items:
    private fun touchMove(x: Float, y: Float) {

        // calculate the distance between the new position and the old one
        val distance = distance(x,y,centerBigCircle.x, centerBigCircle.y)

        // if dot is inside limits, update the center coordinates of the small circle
        if (distance <= (radiusBigCircle - radiusSmallCircle)) {
            this.centerSmallCircle.x = x
            this.centerSmallCircle.y = y
        }

        // normalized point, related to the size of the canvas and the circles
        normalizedX = (centerSmallCircle.x  - this.width / 2) / (radiusBigCircle - radiusSmallCircle)
        normalizedY = (centerSmallCircle.y - this.height / 2) / (radiusBigCircle - radiusSmallCircle) * (-1)

        // will render again the screen.
        invalidate()

        // notify the joystick the coordinates changed
        onChange.change(normalizedX, normalizedY)
    }

    /**  function to calculate the euclid distance between to points  **/
    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }

}