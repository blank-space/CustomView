package com.example.customview.view.StickerEditView.iface;

import android.graphics.Bitmap;

import com.example.customview.view.StickerEditView.model.TextStyleBean;


public interface ITextNode extends INode {

    void setText(String text);

    String getText();

    void setTextStyle(TextStyleBean style);

    void setAdaptive(boolean isAdaptive);

    void setMaxWH(int maxWidth,int maxHeight);

    int[] getWH();

    float[] getPosition();

    void setShowDraw(boolean isShowDraw);

    Bitmap getBitmap();
}
