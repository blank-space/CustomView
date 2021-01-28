package com.example.customview.view.StickerEditView.impl;

import android.graphics.Bitmap;

import com.example.customview.view.StickerEditView.iface.ITextNode;
import com.example.customview.view.StickerEditView.iface.ITextSticker;
import com.example.customview.view.StickerEditView.iface.TextAreaChangeListener;
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;
import com.example.customview.view.StickerEditView.model.TextStyleBean;


/**
 * Desc:
 */
public class TextSticker extends ImageSticker implements ITextSticker {

    private ITextNode mTextNode;
    private TextAreaChangeListener mTextAreaChangeListener;

    public TextSticker(StickerTransformInfoBean stickerTransformInfoBean, TextNode textNode, TextAreaChangeListener listener) {
        super(stickerTransformInfoBean, textNode);
        mTextAreaChangeListener = listener;
        mTextNode = textNode;
        mTextNode.setShowDraw(false);
        mTextNode.setAdaptive(stickerTransformInfoBean.isAdaptive());
        mTextNode.setMaxWH(stickerTransformInfoBean.getTextWidth(), stickerTransformInfoBean.getTextHeight());
        if (stickerTransformInfoBean.isAdaptive()) {
            setText(stickerTransformInfoBean.getText());
        }
    }

    @Override
    public void setText(String text) {
        if (mTextNode.getText() == null || !mTextNode.getText().equals(text)) {
            mTextNode.setText(text);
            mStickerTransformInfoBean.setText(text);
            if (mStickerTransformInfoBean.isAdaptive()) {
                //重新计算控制区大小
                int[] wh = mTextNode.getWH();
                float[] xy = mTextNode.getPosition();
                mStickerTransformInfoBean.setX(xy[0]);
                mStickerTransformInfoBean.setY(xy[1]);
                mStickerTransformInfoBean.setWidth(wh[0]);
                mStickerTransformInfoBean.setHeight(wh[1]);
                if (mTextAreaChangeListener != null) {
                    mTextAreaChangeListener.onChangeTextArea(mStickerTransformInfoBean.getId(),
                            mStickerTransformInfoBean.getX(), mStickerTransformInfoBean.getY(),
                            mStickerTransformInfoBean.getWidth(), mStickerTransformInfoBean.getHeight());
                }
            }
        }
    }

    @Override
    public String getText() {
        return mTextNode.getText();
    }

    public void setTextStyle(TextStyleBean textStyle) {
        mTextNode.setTextStyle(textStyle);
    }

    @Override
    public void setAdaptive(boolean isAdaptive) {
        mTextNode.setAdaptive(isAdaptive);
    }

    @Override
    public void setMaxWH(int maxWidth, int maxHeight) {
        mTextNode.setMaxWH(maxWidth, maxHeight);
    }

    @Override
    public int[] getWH() {
        return new int[0];
    }

    @Override
    public float[] getPosition() {
        return new float[0];
    }

    @Override
    public void setShowDraw(boolean isShowDraw) {
        mTextNode.setShowDraw(isShowDraw);
    }

    @Override
    public Bitmap getBitmap() {
        return mTextNode.getBitmap();
    }
}
