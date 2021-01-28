package com.example.customview.view.StickerEditView;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


class DensityUtils {
    public static int dp2px(Context context, int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, Resources.getSystem().getDisplayMetrics());
    }


    /**
     * Desc:计算两点距离
     */
    public static float getLength(float x1, float y1, float x2, float y2) {
        double dx = Math.pow(x1 - x2, 2);
        double dy = Math.pow(y1 - y2, 2);
        double d = Math.sqrt(dx + dy);
        return (float) d;
    }
}
