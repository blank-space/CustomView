package com.example.customview.view.StickerEditView.iface;

/**
 * 描述:操作对象
 */
public interface INode{

    void setAngle(float angle);

    void setPosition(float x, float y);

    void setScale(float scale);

    void setVisible(boolean visible);

    void setWH(int w,int h);

    void remove();

    void setTopLayer();
}
