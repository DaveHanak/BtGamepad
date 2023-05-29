package com.umpa.btgamepad

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Thumbstick @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet), GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {
    interface ThumbstickListener {
        fun onMove(thumbstick: Thumbstick?, x: Int, y: Int)
        fun onDoubleTapPressed(thumbstick: Thumbstick?)
        fun onDoubleTapReleased(thumbstick: Thumbstick?)
    }
    var listener: ThumbstickListener? = null

    private val paint: Paint = Paint()
    private var backgroundColor: Color
    private var knobColor: Color
    private val gestureDetector: GestureDetector
    private var centerX = 0f
    private var centerY = 0f
    private var posX = 0f
    private var posY = 0f
    private var backgroundRadius = 0f
    private var knobRadius = 0f
    private var x = 0
    private var y = 0

    init {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        gestureDetector = GestureDetector(context, this)
        gestureDetector.setIsLongpressEnabled(false)
        gestureDetector.setOnDoubleTapListener(this)
        if (attributeSet != null) {
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Thumbstick)
            backgroundColor = Color.valueOf(
                typedArray.getColor(
                    R.styleable.Thumbstick_backgroundColor,
                    Color.MAGENTA
                )
            )
            knobColor = Color.valueOf(
                typedArray.getColor(
                    R.styleable.Thumbstick_knobColor,
                    Color.BLUE
                )
            )
            typedArray.recycle()
        } else {
            backgroundColor = Color.valueOf(Color.MAGENTA)
            knobColor = Color.valueOf(Color.BLUE)
        }
    }
    private fun swapColors() {
        val tempColor = backgroundColor
        backgroundColor = knobColor
        knobColor = tempColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        centerX = width * 0.5f
        centerY = height * 0.5f
        posX = centerX
        posY = centerY
        val min = width.coerceAtMost(height)
        knobRadius = min * 0.125f
        backgroundRadius = min * 0.375f
    }

    public override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            paint.color = backgroundColor.toArgb()
            canvas.drawCircle(centerX, centerY, backgroundRadius, paint)
            paint.color = knobColor.toArgb()
            canvas.drawCircle(posX, posY, knobRadius, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                posX = event.x
                posY = event.y
                val theta = atan2((centerY - posY), (centerX - posX))
                val cosTheta = cos(theta)
                val sinTheta = sin(theta)
                var swerveRadius = sqrt((posX - centerX) * (posX - centerX) + (posY - centerY) * (posY - centerY))
                if (swerveRadius > backgroundRadius) {
                    swerveRadius = backgroundRadius
                    posX = centerX - backgroundRadius * cosTheta
                    posY = centerY - backgroundRadius * sinTheta
                }
                val power = 127 * swerveRadius / backgroundRadius
                x = -(power * cosTheta).toInt()
                y = -(power * sinTheta).toInt()
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                posX = centerX
                posY = centerY
                x = 0
                y = 0
                invalidate()
            }
        }
        listener?.onMove(this, x, y)
        return true
    }

    override fun onSingleTapConfirmed(motionEvent: MotionEvent): Boolean {
        return false
    }
    override fun onDoubleTap(motionEvent: MotionEvent): Boolean {
        return false
    }
    override fun onDoubleTapEvent(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            listener?.onDoubleTapPressed(this)
            swapColors()
        } else if (motionEvent.action == MotionEvent.ACTION_UP) {
            listener?.onDoubleTapReleased(this)
            swapColors()
        }
        return true
    }
    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }
    override fun onShowPress(motionEvent: MotionEvent) {}
    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }
    override fun onScroll(
        motionEvent: MotionEvent,
        motionEvent1: MotionEvent,
        v: Float,
        v1: Float,
    ): Boolean {
        return false
    }
    override fun onLongPress(motionEvent: MotionEvent) {}
    override fun onFling(
        motionEvent: MotionEvent,
        motionEvent1: MotionEvent,
        v: Float,
        v1: Float,
    ): Boolean {
        return false
    }
}
