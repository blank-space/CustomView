package com.example.customview.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.customview.utils.dp
import kotlin.math.cos
import kotlin.math.sin


private const val OPEN_ANGLE = 120f
private val DASH_WIDTH = 2f.dp
private val DASH_HEIGHT = 10f.dp
private val RADIO = 150f.dp
private val LENGTH = 100f.dp
private val MARK = 6

/**
 * 仪表盘
 */
class DashboardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dash = Path()
    private val path = Path()
    private lateinit var pathEffect: PathEffect

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f.dp
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CCW)
    }

    /**
     * 计划路径长度 平分给20个刻度
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()
        path.addArc(width / 2f - RADIO, height / 2f - RADIO, width / 2 + RADIO,
                height / 2f + RADIO, 90 + OPEN_ANGLE / 2, 360 - OPEN_ANGLE)
        val pathMeasure = PathMeasure(path, false)
        val offset = pathMeasure.length / 21
        //刻度沿着轨迹：PathDashPathEffect.Style.ROTATE  绘制
        pathEffect = PathDashPathEffect(dash, offset, 0F, PathDashPathEffect.Style.ROTATE)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        //画弧线
        canvas.drawPath(path, paint)
        paint.pathEffect = pathEffect
        //画刻度
        canvas.drawPath(path, paint)
        paint.pathEffect = null
        //画指针
        // 三⻆函数的计算 横向的位移是 cos，纵向的位移是 sin
        canvas.drawLine(width / 2f, height / 2f,
                width / 2f + LENGTH * cos(mark2Radians(MARK)).toFloat(),
                height / 2 + LENGTH * sin(mark2Radians(MARK)).toFloat(), paint)

    }

    /**
     * 第几个刻度的角度
     * 90+ OPEN_ANGLE/2f : 仪表盘的起始角度
     * (360- OPEN_ANGLE)/20f*mark 第n个刻度划过的角度
     */
    private fun mark2Radians(mark: Int) =
            Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())

}