package com.example.customview.view.StickerEditView.impl;



import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.iface.IStickerContainer;
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 贴纸容器
 */
public class StickerContainer implements IStickerContainer {

    private List<ISticker> mStickerList = new ArrayList<>();

    @Override
    public void addSticker(ISticker sticker) {
        mStickerList.add(sticker);
    }

    @Override
    public ISticker removeSticker(String id) {
        ISticker sticker = getSticker(id);
        mStickerList.remove(sticker);
        return sticker;
    }

    @Override
    public void removeAllSticker() {
        mStickerList.clear();
    }

    @Override
    public ISticker getSticker(String id) {
        for (ISticker sticker : mStickerList) {
            if (sticker.getTransformationInfo().getId().equals(id)) {
                return sticker;
            }
        }
        return null;
    }

    @Override
    public void setCurrentTime(long timeMs) {
        for (ISticker sticker : mStickerList) {
            sticker.setCurrentTimeMs(timeMs);
        }
    }

    @Override
    public void changeStickerTime(String id, long startTimeMs, long durationMs) {
        ISticker sticker = getSticker(id);
        sticker.setShowTime(startTimeMs, durationMs);
    }

    @Override
    public void changeTextStickerText(String id, String text) {
        ISticker sticker = getSticker(id);
        if(sticker != null && sticker instanceof TextSticker){
            ((TextSticker) sticker).setText(text);
        }
    }

    @Override
    public List<StickerTransformInfoBean> getStickerTransformationInfo() {
        List<StickerTransformInfoBean> stickerTransformInfoBeanList = new ArrayList<>();
        for(ISticker sticker : mStickerList){
            stickerTransformInfoBeanList.add(sticker.getTransformationInfo());
        }
        return stickerTransformInfoBeanList;
    }
}
