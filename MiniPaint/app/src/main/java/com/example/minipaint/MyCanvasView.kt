package com.example.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH = 12f

class MyCanvasView constructor(context: Context) :
    View(context) {

    private var backColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private var drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private var currentPath = Path()
    private var drawPath = Path()

    private var tocuhMotionEventX = 0f
    private var touchMotionEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private lateinit var rect: Rect

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = drawColor
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeWidth = STROKE_WIDTH
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val inset = 50
        rect = Rect(inset, inset, w - inset, h - inset)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(backColor)
        canvas?.drawPath(drawPath, paint)
        canvas?.drawPath(currentPath, paint)
        canvas?.drawRect(rect, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchMotionEventY = event.y
        tocuhMotionEventX = event.x

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchUp() {
        drawPath.addPath(currentPath)
        currentPath.reset()
    }

    private fun touchMove() {
        currentPath.quadTo(
            currentX,
            currentY,
            (tocuhMotionEventX + currentX) / 2,
            (touchMotionEventY + currentY) / 2
        )

        currentY = touchMotionEventY
        currentX = tocuhMotionEventX

        invalidate()
    }

    private fun touchStart() {
        currentPath.reset()
        currentPath.moveTo(tocuhMotionEventX, touchMotionEventY)
        currentX = tocuhMotionEventX
        currentY = touchMotionEventY
    }
}