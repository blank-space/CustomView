package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customview.utils.dp

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/25
 * @desc   : 完全自定义onMeasure，onDraw
 *
 * wrap_content -> MeasureSpec.AT_MOST ，此时MeasureSpec.getSize(widthMeasureSpec)的大小等于父容器的大小
 * match_parent(100dp) -> MeasureSpec.EXACTLY
 */


private val RADIO = 70.dp
private val PADDING = 80.dp

class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val SIZE = (PADDING + RADIO) * 2
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        /* resolveSize == 以下模版代码
        val w = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> if (SIZE > MeasureSpec.getSize(widthMeasureSpec)) MeasureSpec.getSize(widthMeasureSpec) else SIZE
            else -> SIZE
        }
        */
        val width = resolveSize(SIZE.toInt(), widthMeasureSpec)
        val height = resolveSize(SIZE.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(PADDING + RADIO, PADDING + RADIO, RADIO, paint)

    }

}