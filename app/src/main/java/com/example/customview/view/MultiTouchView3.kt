package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.example.customview.utils.dp
import com.example.customview.utils.getAvatar

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/7
 * @desc   : 互不干扰型多点触摸（各自为战）
 */
class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5.dp
        strokeCap = Paint.Cap.ROUND
        //设置连接处的样式
        strokeJoin = Paint.Join.ROUND
    }
    private val paths = SparseArray<Path>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until paths.size())
            //paths的key不是一般的index,而是view范围内的pointId
            canvas.drawPath(paths.valueAt(i), paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val path = Path()
                val actionIndex = event.actionIndex
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                paths.put(event.getPointerId(actionIndex), path)
            }
            //没有区分哪个根手指的必要
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until paths.size()){
                    val pointId = event.getPointerId(i)
                    val path =paths[pointId]
                    val index=event.findPointerIndex(pointId)
                    path.lineTo(event.getX(index), event.getY(index))
                }
            }

            MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointId = event.getPointerId(actionIndex)
                paths.remove(pointId)
            }
        }
        invalidate()
        return true
    }

}