package com.example.customview.view.StickerEditView.iface;

/**
 * Desc: 贴纸Touch监听
 */
public interface StickerTouchListener {

    void onActionDown(String id);

    void onActionMove(String id, float cx, float cy);

    void onActionUp(String id);
}
