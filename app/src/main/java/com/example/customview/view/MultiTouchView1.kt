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
 * @desc   : 接力型多点触摸（关键是怎么接棒）
 */
class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var offsetX = 0f
    private var offsetY = 0f
    private var initOffsetX = 0f
    private var initOffsetY = 0f
    private var downX = 0f
    private var downY = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, 200.dp.toInt())
    private var trackPointId = 0;

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //event.action不能准确反馈多点触控
        //多点触控时，需要用event.getX(index)来精准获取接管触摸事件的手指，但是index是可变的，唯有view的id是不变的
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                //down的时候只有一根手指，所以index=0
                trackPointId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                initOffsetX = offsetX
                initOffsetY = offsetY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                trackPointId = event.getPointerId(actionIndex)
                downX = event.getX(actionIndex)
                downY = event.getY(actionIndex)
                initOffsetX = offsetX
                initOffsetY = offsetY
            }

            MotionEvent.ACTION_POINTER_UP -> {
                //up事件包含了正在抬起的手指，手指抬起后需要指定下一根手指接棒
                val actionIndex = event.actionIndex
                val pointId = event.getPointerId(actionIndex)
                if (pointId == trackPointId) {
                    //如果正好有三根手指（pointCount=3），序号是 ：0，1，2。如果第三根手指抬起了，index=3-1是不对的，正确的是再减1
                    val newActionIndex = if (actionIndex == event.pointerCount - 1) {
                        event.pointerCount - 2
                    } else {
                        event.pointerCount - 1
                    }
                    trackPointId = event.getPointerId(newActionIndex)
                    downX = event.getX(newActionIndex)
                    downY = event.getY(newActionIndex)
                    initOffsetX = offsetX
                    initOffsetY = offsetY

                }
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = event.getX(event.findPointerIndex(trackPointId)) - downX + initOffsetX
                offsetY = event.getY(event.findPointerIndex(trackPointId)) - downY + initOffsetY
                invalidate()
            }
        }
        return true
    }

}