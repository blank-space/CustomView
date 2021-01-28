package com.example.customview.view.StickerEditView;

import android.view.View;

/**
 * 描述: 防快速点击事件
 */
public abstract class OnDefenseQuicklyClickListener implements View.OnClickListener {

    private final long INTERVAL_TIME_MS = 1000;  //间隔一秒

    private long mLastTimeMs;

    @Override
    public final void onClick(View v) {

        long currentTimeMs = System.currentTimeMillis();
        long interval =  currentTimeMs - mLastTimeMs;
        if(interval > INTERVAL_TIME_MS){
            mLastTimeMs = currentTimeMs;
            onQuicklyClick(v);
        }
    }

    protected abstract void onQuicklyClick(View v);

}
