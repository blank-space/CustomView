package com.example.customview.view.StickerEditView;


import com.example.customview.view.StickerEditView.iface.ControlListener;
import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.iface.StickerTouchListener;

public interface IControlView {

    void setControlListener(ControlListener listener);

    void setStickerTouchListener(StickerTouchListener touchListener);

    void openControl(ISticker sticker);

    void closeControl();

    void uptateControlArea();

    ISticker getCurrentSticker();

    boolean isClose();
}
