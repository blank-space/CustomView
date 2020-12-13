package com.example.customview.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.customview.R
import com.example.customview.utils.dp


val IMAGE_WIDTH=200f.dp
val IMAGE_PADDING =20f.dp
val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)


/**
 * 使用Xfermode合成圆形头像
 */
class AvatarView(context: Context,attributeSet: AttributeSet):View(context,attributeSet) {
    private val paint =Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds=RectF(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING+ IMAGE_WIDTH,
        IMAGE_PADDING+ IMAGE_WIDTH)



    /*为了把需要互相作⽤的图形放在单独的位置来绘制，不会受 View 本身的影响。
    如果不使⽤ saveLayer()，绘制的⽬标区域将总是整个 View 的范围，两个图形
    的交叉区域就错误了。*/
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        //把绘制区域拉到单独的离屏缓冲区
        val count =canvas.saveLayer(bounds,null)
      canvas.drawOval(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING+ IMAGE_WIDTH,
          IMAGE_PADDING+ IMAGE_WIDTH,paint)
        paint.xfermode = XFERMODE
        canvas.drawBitmap(getAvatar(IMAGE_WIDTH.toInt()), IMAGE_PADDING, IMAGE_PADDING,paint)
        paint.xfermode=null
        //⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域
        canvas.restoreToCount(count)
    }


    fun getAvatar(width:Int):Bitmap{
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds =true
        BitmapFactory.decodeResource(resources, R.mipmap.meizi,option)
        option.inJustDecodeBounds=false
        //原大小
        option.inDensity =option.outWidth
        //需要的大小
        option.inTargetDensity=width
        return   BitmapFactory.decodeResource(resources, R.mipmap.meizi,option)
    }

}