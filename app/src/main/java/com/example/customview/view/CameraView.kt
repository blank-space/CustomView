package com.example.customview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.withSave
import com.example.customview.R
import com.example.customview.utils.dp

private val BITMAP_SIZE = 200.dp
private val BITMAP_PADDING = 100.dp

class CameraView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(BITMAP_SIZE.toInt())
    //范围裁切
    /* private val clipped =Path().apply {
         addOval(BITMAP_PADDING, BITMAP_PADDING,BITMAP_PADDING+ BITMAP_SIZE
         ,BITMAP_PADDING+ BITMAP_SIZE,Path.Direction.CCW
         )
     }*/

    private val camera = Camera()

    init {
        camera.rotateX(30f)
        camera.setLocation(0f, 0f, -6 * Resources.getSystem().displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas) {
        //范围裁切
        /*canvas.clipPath(clipped)*/

        //上部分
        canvas.withSave {
            canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
            //bitMap的中心点在(0,0)
            //canvas.rotate(-30f)
            canvas.clipRect(-BITMAP_SIZE / 2, -BITMAP_SIZE / 2,
                    BITMAP_SIZE / 2, 0F)
            //canvas.rotate(30f)
            canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE / 2), -(BITMAP_PADDING + BITMAP_SIZE / 2))
            canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
        }


        //下半部分
        canvas.withSave {
            canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
            //canvas.rotate(-30f)
            camera.applyToCanvas(canvas)
            //bitMap的中心点在(0,0)
            canvas.clipRect(-BITMAP_SIZE / 2, 0f,
                    BITMAP_SIZE / 2, BITMAP_SIZE / 2)
            //canvas.rotate(30f)
            canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE / 2), -(BITMAP_PADDING + BITMAP_SIZE / 2))
            canvas.drawBitmap(bitmap, BITMAP_PADDING, BITMAP_PADDING, paint)
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