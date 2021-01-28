package com.example.customview.view.StickerEditView.iface;


import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;

public interface ISticker extends INode{

    StickerTransformInfoBean getTransformationInfo();

    INode getNode();

    void setPositionBy(float dx,float dy);

    void setAngleBy(float da);

    void setShowTime(long startTimeMs,long durationMs);

    void setCurrentTimeMs(long timeMs);

}
