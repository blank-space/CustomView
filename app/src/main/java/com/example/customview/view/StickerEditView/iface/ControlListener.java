package com.example.customview.view.StickerEditView.iface;


public interface ControlListener {

    void onClickDelete(String id);

    void onChange(String id, float x, float y, float scale, float angle);

    void onClickSticker(String id, int type);
}
