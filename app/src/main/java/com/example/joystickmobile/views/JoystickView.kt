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

    private var radiusLittleCircle: Float = 0f
    private var centerLittleCircle: PointF = PointF()
    private var radiusBigCircle: Float = 0f
    private var centerBigCircle: PointF = PointF()
    private var normalizedX = 0.0f
    private var normalizedY = 0.0f
    public lateinit var onChange: OnJoystickChange


    private val paintLittleCircle = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#808080")
        isAntiAlias = true
    }

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
            centerLittleCircle.x,
            centerLittleCircle.y,
            radiusLittleCircle,
            paintLittleCircle
        )
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        // make sure actual code handles padding well.
        radiusLittleCircle = 0.2f * min(width, height).toFloat()
        radiusBigCircle = 0.45f * min(width, height).toFloat()
        centerLittleCircle = PointF(width / 2.0f, height / 2.0f)
        centerBigCircle = PointF(width / 2.0f, height / 2.0f)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }
        when (event.action) {
            //MotionEvent.ACTION_DOWN -> TODO()
            MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                centerLittleCircle.x = (this.width / 2).toFloat()
                centerLittleCircle.y = (this.height / 2).toFloat()
                invalidate()
                onChange.change(0f, 0f)
            }
        }
        return true
    }

    private fun touchMove(x: Float, y: Float) {
        //Update positions and properties of drawn items:

        println("the big circle Diameter is ${2*radiusBigCircle}")
        val distance = distance(x,y,centerBigCircle.x, centerBigCircle.y)

        if (distance <= (radiusBigCircle - radiusLittleCircle)) {
            this.centerLittleCircle.x = x
            this.centerLittleCircle.y = y
        }
        normalizedX = (centerLittleCircle.x  - this.width / 2) / (radiusBigCircle - radiusLittleCircle)
        normalizedY = (centerLittleCircle.y - this.height / 2) / (radiusBigCircle - radiusLittleCircle) * (-1)

        // will render again the screen.
        invalidate()
        onChange.change(normalizedX, normalizedY)
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }


    init {
        println("Joystick created")
    }


}