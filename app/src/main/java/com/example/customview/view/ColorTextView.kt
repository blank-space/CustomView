package com.example.customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginLeft
import com.example.customview.utils.dp
import kotlin.random.Random

private val COLORS = intArrayOf(
    Color.BLUE,
    Color.RED,
    Color.CYAN,
    Color.DKGRAY,
    Color.MAGENTA,
    Color.BLACK
)
private val TEXT_SIZE = intArrayOf(15, 18, 21)
private val PADDING_TOP = 4.dp.toInt()
private val PADDING_LEFT = 8.dp.toInt()

class ColorTextView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    private val random = Random
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = COLORS[random.nextInt(COLORS.size)]
    }

    init {
        setTextColor(Color.WHITE)
        setPadding(PADDING_LEFT, PADDING_TOP, PADDING_LEFT, PADDING_TOP)
        textSize = TEXT_SIZE[random.nextInt(TEXT_SIZE.size)].toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paint)
        super.onDraw(canvas)
    }

}