package com.example.customview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.customview.R
import com.example.customview.utils.dp

private val PADDING = 8.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val TEXT_SIZE = 12.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

class MaterialEditTextView(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = Color.WHITE
    }
    private var mHasFillText = false
    //进度
    var mFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    val annotation by lazy {
        ObjectAnimator.ofFloat(this, "mFraction", 0f, 1f)
    }

    var userFloatLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    setPadding(paddingLeft, paddingTop + TEXT_SIZE.toInt(), paddingRight, paddingBottom)
                } else {
                    setPadding(paddingLeft, paddingTop - TEXT_SIZE.toInt(), paddingRight, paddingBottom)
                }
            }
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditTextView)
        userFloatLabel = typedArray.getBoolean(R.styleable.MaterialEditTextView_userFloatLabel, true)
        typedArray.recycle()

        if (userFloatLabel) {
            setPadding(HORIZONTAL_OFFSET.toInt(), paddingTop + (PADDING + TEXT_SIZE).toInt(), paddingRight, paddingBottom)
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.isNullOrEmpty() && mHasFillText) {
            mHasFillText = false
            //反转动画
            annotation.reverse()
        } else if (!text.isNullOrEmpty() && !mHasFillText) {
            mHasFillText = true
            annotation.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textPaint.alpha = (mFraction * 0xff).toInt()
        val crtVerticalOffset = VERTICAL_OFFSET + EXTRA_VERTICAL_OFFSET * (1 - mFraction)
        canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, crtVerticalOffset, textPaint)
    }

}