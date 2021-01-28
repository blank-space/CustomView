package com.example.customview.view.StickerEditView.iface;

/**
 * 描述: 贴纸编辑监听
 */
public interface StickerEditListener extends ControlListener{

    void onOpenEdit(String id);

    void onCloseEdit();

    void onChangeTextArea(String id, float x, float y, float w, float h);
}
