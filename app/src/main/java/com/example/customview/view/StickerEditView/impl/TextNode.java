package com.example.customview.view.StickerEditView.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.ViewGroup;

import com.example.customview.view.StickerEditView.StickerTextView;
import com.example.customview.view.StickerEditView.iface.ITextNode;
import com.example.customview.view.StickerEditView.model.TextStyleBean;


/**
 * Desc: 贴纸文字绘制
 */
public class TextNode extends Node implements ITextNode {

    private StickerTextView mStickerTextView;

    public TextNode(StickerTextView view) {
        super(view);
        mStickerTextView = view;
    }

    @Override
    public void setText(String text) {
        mStickerTextView.setText(text);
    }

    @Override
    public String getText() {
        return mStickerTextView.getText();
    }

    @Override
    public void setTextStyle(TextStyleBean style) {
        mStickerTextView.setTextStyle(style);
    }

    @Override
    public void setAdaptive(boolean isAdaptive) {
        mStickerTextView.setAdaptive(isAdaptive);
    }

    @Override
    public void setMaxWH(int maxWidth, int maxHeight) {
        mStickerTextView.setMaxWH(maxWidth, maxHeight);
    }

    @Override
    public int[] getWH() {
        ViewGroup.LayoutParams lp = mStickerTextView.getLayoutParams();
        return new int[]{lp.width, lp.height};
    }

    @Override
    public float[] getPosition() {
        return new float[]{mStickerTextView.getTranslationX(), mStickerTextView.getTranslationY()};
    }

    @Override
    public void setShowDraw(boolean isShowDraw) {
        mStickerTextView.setShowDraw(isShowDraw);
    }

    @Override
    public Bitmap getBitmap() {
        int[] wh = getWH();
        Bitmap bitmap = Bitmap.createBitmap(wh[0], wh[1], Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mStickerTextView.drawText(canvas);
        return bitmap;
    }

}
