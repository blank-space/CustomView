package com.example.customview.view.StickerEditView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.view.StickerEditView.iface.ControlListener;
import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.iface.StickerTouchListener;


/**
 * 描述:手势操作外层，为了统一操作坐标系
 */
public class StickerControlView extends FrameLayout implements IControlView {

    private ControlView mControlView;

    public StickerControlView(@NonNull Context context) {
        this(context, null);
    }

    public StickerControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mControlView = new ControlView(context);
        addView(mControlView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mControlView.handlerTouchEvent(event);
    }

    @Override
    public void setControlListener(ControlListener listener) {

        mControlView.setControlListener(listener);
    }

    @Override
    public void setStickerTouchListener(StickerTouchListener touchListener) {

        mControlView.setStickerTouchListener(touchListener);
    }

    @Override
    public void openControl(ISticker sticker){
        setVisibility(View.VISIBLE);
        mControlView.openControl(sticker);
    }

    @Override
    public void closeControl(){
        setVisibility(View.GONE);
        mControlView.closeControl();
    }

    @Override
    public void uptateControlArea() {

        mControlView.uptateControlArea();
    }

    @Override
    public ISticker getCurrentSticker() {
        return mControlView.getCurrentSticker();
    }

    @Override
    public boolean isClose() {
        return mControlView.isClose();
    }
}
