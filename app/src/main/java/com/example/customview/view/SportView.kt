package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.customview.utils.dp

private val RANG_WIDTH = 10f.dp
private val RADIUS = 100f.dp

/**
 * 表盘
 */
class SportView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bound = Rect()
    private val fontMetrics = Paint.FontMetrics()

    init {
        paint.textSize = 50f.dp
        paint.textAlign = Paint.Align.CENTER
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        //1绘制环
        val stroke = Paint.Style.STROKE
        paint.style = stroke
        paint.color = Color.BLACK
        paint.strokeWidth = RANG_WIDTH
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        //2绘制进度条
        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2 + RADIUS,
                -90f, 225f, false, paint)

        //3.绘制静态文字 居中纵向测量
//        paint.style = Paint.Style.FILL
//        paint.getTextBounds("abcp", 0, "absd".length, bound)
//        //竖直居中对其
//        canvas.drawText("abcP", width / 2f, height / 2f - (bound.top + bound.bottom) / 2f, paint)

        //4.绘制动态文字 居中纵向测量
        paint.style = Paint.Style.FILL
        paint.getFontMetrics(fontMetrics)
        canvas.drawText("abcp", width / 2f,
                height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2, paint)


        //左上对齐 取消系统自带的空隙
        paint.textSize = 15.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds("abab", 0, 4, bound)
        canvas.drawText("abab", -bound.left.toFloat(), -bound.top.toFloat(), paint)

        paint.textSize = 150.dp
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds("abab", 0, 4, bound)
        canvas.drawText("abab", -bound.left.toFloat(), -bound.top.toFloat(), paint)

    }

}