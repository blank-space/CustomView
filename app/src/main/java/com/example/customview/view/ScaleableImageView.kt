package com.example.customview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.example.customview.utils.dp
import com.example.customview.utils.getAvatar
import kotlin.math.max
import kotlin.math.min

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/11
 * @desc   : 可以缩放滑动和拉伸的ImageView
 */
private val IMAGE_SIZE = 200.dp
private val EXTRA_SCALE_FACTOR = 1.5f

class ScaleableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE.toInt())
    private var offsetX = 0f
    private var offsetY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var smallScaleFactor = 0f
    private var bigScaleFactor = 0f
    private var big = false
    private val onFlingRunnable = OnFlingRunnable()
    private val gestureListener = InnerGestureListener()
    private val gestureDetector = GestureDetectorCompat(context, gestureListener)
    private val scaleGestureListener = InnerScaleGestureListener()
    private val scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator =
        ObjectAnimator.ofFloat(this, "currentScale", smallScaleFactor, bigScaleFactor)


    //相对OverScroller，Scroller没初始速度。用于自动计算滑动偏移
    private val scroll = OverScroller(context)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - bitmap.height) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScaleFactor = width / bitmap.width.toFloat()
            bigScaleFactor = height / bitmap.height * EXTRA_SCALE_FACTOR
        } else {
            bigScaleFactor = width / bitmap.width * EXTRA_SCALE_FACTOR
            smallScaleFactor = height / bitmap.height.toFloat()
        }

        currentScale = smallScaleFactor
        //smallScaleFactor,bigScaleFactor是动态值，每次都要更新
        scaleAnimator.setFloatValues(smallScaleFactor, bigScaleFactor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //捏撑比
        val scaleRate = (currentScale - smallScaleFactor) / (bigScaleFactor - smallScaleFactor)
        canvas.translate(offsetX * scaleRate, offsetY * scaleRate)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        //没执行scale手势时，方可执行gestureDetector
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    inner class InnerGestureListener : GestureDetector.SimpleOnGestureListener() {
        /**
         * action_down事件按下的时候会被调用，在这里返回true,可以保证必然消费事件
         */
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        /**
         * @param distanceX = 按下的位置减去当前的位置，所以使用的时候需要取他的负数
         * @param e1 :用户按下的actionDown事件
         * @param e2 ：当前事件
         */
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()

            }
            return false
        }


        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (big) {
                // minX, maxX = （图片放大后的最大宽度-View的宽度）/2
                scroll.fling(
                    offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    (-(bitmap.width * bigScaleFactor - width) / 2).toInt(),
                    ((bitmap.width * bigScaleFactor - width) / 2).toInt(),
                    (-(bitmap.height * bigScaleFactor - height) / 2).toInt(),
                    ((bitmap.height * bigScaleFactor - height) / 2).toInt()
                )
            }
            //在下一帧刷新
            ViewCompat.postOnAnimation(this@ScaleableImageView, onFlingRunnable)
            return false
        }


        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                offsetX = (e.x - width / 2f) * (1 - bigScaleFactor / smallScaleFactor)
                offsetY = (e.x - height / 2f) * (1 - bigScaleFactor / smallScaleFactor)
                fixOffset()
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return true
        }

        /**
         * 修正边界
         */
        private fun fixOffset() {
            offsetX = min(offsetX, (bitmap.width * bigScaleFactor - width) / 2)
            offsetX = max(offsetX, -(bitmap.width * bigScaleFactor - width) / 2)
            offsetY = min(offsetY, (bitmap.height * bigScaleFactor - height) / 2)
            offsetY = max(offsetY, -(bitmap.height * bigScaleFactor - height) / 2)
        }

    }


    inner class OnFlingRunnable : Runnable {
        override fun run() {
            if (scroll.computeScrollOffset()) {
                offsetX = scroll.currX.toFloat()
                offsetY = scroll.currY.toFloat()
                invalidate()
                //在下一帧主线程执行
                ViewCompat.postOnAnimation(this@ScaleableImageView, this)
            }
        }
    }


    inner class InnerScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        /**
         * @return return true,detector.scaleFactor是与上一刻的比值，return false ,detector.scaleFactor是与初始值的比值
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempValue = detector.scaleFactor * currentScale
            big = if (tempValue > smallScaleFactor) true else false
            if (tempValue < smallScaleFactor || tempValue > bigScaleFactor) {
                return false
            } else {
                currentScale = tempValue
                return true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScaleFactor / smallScaleFactor)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScaleFactor / smallScaleFactor)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }
    }
}