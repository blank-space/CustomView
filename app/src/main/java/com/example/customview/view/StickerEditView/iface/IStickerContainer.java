package com.example.customview.view.StickerEditView.iface;

import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;

import java.util.List;


public interface IStickerContainer {

    void addSticker(ISticker sticker);

    ISticker removeSticker(String id);

    void removeAllSticker();

    ISticker getSticker(String id);

    void setCurrentTime(long timeMs);

    void changeStickerTime(String id, long startTimeMs, long durationMs);

    void changeTextStickerText(String id, String text);

    List<StickerTransformInfoBean> getStickerTransformationInfo();
}
