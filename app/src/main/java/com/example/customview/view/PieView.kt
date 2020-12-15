package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.customview.utils.dp
import kotlin.math.cos
import kotlin.math.sin

private val RADIUS = 150f.dp
private val ANGLES = floatArrayOf(90f, 120f, 150f)
private val COLORS = listOf(Color.parseColor("#558b3f"),
        Color.parseColor("#ff0000"), Color.parseColor("#000000"))
private val OFFSET_LENGTH = 10f.dp
private const val CHANGE_INDEX = 2

/**
 * 饼图
 */
class PieView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        var startAngle = 0f
        for ((index, angle) in ANGLES.withIndex()) {
            paint.color = COLORS[index]
            //⽤ Canvas.translate() 来移动扇形，并⽤ Canvas.save() 和 Canvas.restore() 来 保存和恢复位置
            if (index == CHANGE_INDEX) {
                canvas.save()
                //三⻆函数的计算 横向的位移是 cos，纵向的位移是 sin
                canvas.translate(OFFSET_LENGTH * cos(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat(),
                        OFFSET_LENGTH * sin(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat())
            }
            canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS,
                    startAngle, angle, true, paint)
            startAngle += angle
            if (index == CHANGE_INDEX) {
                canvas.restore()
            }
        }

    }


}