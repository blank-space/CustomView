package com.example.customview.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import com.example.customview.R

val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = this.toFloat().dp

fun getAvatar(resources: Resources, width: Int): Bitmap {
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