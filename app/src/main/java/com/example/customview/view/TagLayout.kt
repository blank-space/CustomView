package com.example.customview.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private val childBounds = mutableListOf<Rect>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthUsed = 0
        var heightUsed = 0
        var crtLineWidthUsed = 0
        var crtLineHeightUsed = 0
        var crtLineMaxHeight = 0
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)

        //遍历测量子VIEW
        for ((index, child) in children.withIndex()) {
            //第一次测量
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            //如果当前行累积的宽度+该view的宽度大于父容器的宽度，则换行
            if (child.measuredWidth + crtLineWidthUsed > specWidthSize &&
                specWidthMode != MeasureSpec.UNSPECIFIED
            ) {
                crtLineWidthUsed = 0
                //不断累积
                heightUsed += crtLineMaxHeight
                crtLineMaxHeight = 0
                //二次测量
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }

            if (index >= childBounds.size) {
                childBounds.add(Rect())
            }
            val childBound = childBounds[index]
            //缓存子view的位置和大小
            childBound.set(
                crtLineWidthUsed, heightUsed, crtLineWidthUsed + child.measuredWidth,
                heightUsed + child.measuredHeight
            )
            //累积当前行已用的宽度
            crtLineWidthUsed += child.measuredWidth
            //记录当前行最高的
            crtLineMaxHeight = max(child.measuredHeight, crtLineMaxHeight)
            //每行中使用过最长的宽度,不需清零
            widthUsed = max(widthUsed,crtLineWidthUsed)
        }

        val myWidth = widthUsed
        //需加上当前行view的最大行高
        val myHeight = heightUsed+crtLineMaxHeight
        setMeasuredDimension(myWidth, myHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index, child) in children.withIndex()) {
            val bound = childBounds[index]
            //把测量好的子view宽高传递给子view保存
            child.layout(bound.left, bound.top, bound.right, bound.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context,attrs)
    }

}