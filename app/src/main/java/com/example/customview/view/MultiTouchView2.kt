package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.customview.utils.dp
import com.example.customview.utils.getAvatar

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/7
 * @desc   : 协作型多点触摸（关键是找到多点的中心点）
 */
class MultiTouchView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var offsetX = 0f
    private var offsetY = 0f
    private var initOffsetX = 0f
    private var initOffsetY = 0f
    private var downX = 0f
    private var downY = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, 200.dp.toInt())

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val focusX: Float
        val focusY: Float
        var pointCount = event.pointerCount
        var sumX = 0f
        var sumY = 0f
        //是否抬起,抬起时不要累加，也不要多除于
        val isPointUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        //遍历所有手指
        for (index in 0 until pointCount) {
            if (!(isPointUp && index == event.actionIndex)) {
                sumX += event.getX(index)
                sumY += event.getY(index)
            }
        }
        if (isPointUp) {
            --pointCount
        }
        focusX = sumX / pointCount
        focusY = sumY / pointCount

        //event.action不能准确反馈多点触控
        //多点触控时，需要用event.getX(index)来精准获取接管触摸事件的手指，但是index是可变的，唯有view的id是不变的
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> {
                downX = focusX
                downY = focusY
                initOffsetX = offsetX
                initOffsetY = offsetY
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + initOffsetX
                offsetY = focusY - downY + initOffsetY
                invalidate()
            }
        }
        return true
    }

}