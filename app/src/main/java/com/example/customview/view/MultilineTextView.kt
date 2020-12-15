package com.example.customview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customview.R
import com.example.customview.utils.dp

private val IMAGE_WIDTH = 150.dp
private val IMAGE_PADDING = 50.dp

class MultilineTextView(context: Context, attributeSet: AttributeSet)
    : View(context, attributeSet) {

    val text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit," +
            " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
            " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
            " voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint" +
            " occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim " +
            "id est laborum."

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
    }
    private val fontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(getAvatar(IMAGE_WIDTH.toInt()), 0f,
                IMAGE_PADDING, paint)

        var measureWidth = floatArrayOf(0f)
        paint.getFontMetrics(fontMetrics)
        var start = 0
        var count: Int
        var verticalOffset = -fontMetrics.top
        var maxWidth: Float
        var horizantalOffset:Float
        while (start < text.length) {
            if (verticalOffset + fontMetrics.bottom < IMAGE_PADDING || verticalOffset + fontMetrics.top > IMAGE_PADDING + IMAGE_WIDTH) {
                //设置一行的最大长度
                maxWidth = width.toFloat()
                horizantalOffset=0f
            } else {
                //设置一行的最大长度
                maxWidth = width - IMAGE_WIDTH
                horizantalOffset= IMAGE_WIDTH
            }
            //截断一行,有多少个字
            var count = paint.breakText(text, start, text.length, true, maxWidth, measureWidth)
            canvas.drawText(text, start, start + count, horizantalOffset, verticalOffset, paint)
            start += count
            verticalOffset += paint.fontSpacing
        }


    }


    fun getAvatar(width: Int): Bitmap {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.mipmap.meizi, option)
        option.inJustDecodeBounds = false
        //原大小
        option.inDensity = option.outWidth
        //需要的大小
        option.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.mipmap.meizi, option)
    }
}