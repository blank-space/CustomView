package com.example.customview.view.StickerEditView.impl;


import com.example.customview.view.StickerEditView.iface.INode;
import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;

public class ImageSticker implements ISticker {

    protected StickerTransformInfoBean mStickerTransformInfoBean;
    private INode mNode;

    public ImageSticker(StickerTransformInfoBean stickerTransformInfoBean, Node node) {
        mNode = node;
        mStickerTransformInfoBean = stickerTransformInfoBean;
        setWH(mStickerTransformInfoBean.getWidth(), mStickerTransformInfoBean.getHeight());
        setPosition(mStickerTransformInfoBean.getX(), mStickerTransformInfoBean.getY());
        setScale(mStickerTransformInfoBean.getScale());
        setAngle(mStickerTransformInfoBean.getAngle());
    }

    @Override
    public void setAngle(float angle) {
        mStickerTransformInfoBean.setAngle(angle);
        mNode.setAngle(angle);
    }

    @Override
    public void setPosition(float x, float y) {
        mStickerTransformInfoBean.setX(x);
        mStickerTransformInfoBean.setY(y);
        mNode.setPosition(x, y);
    }

    @Override
    public void setPositionBy(float dx, float dy) {
        setPosition(dx + mStickerTransformInfoBean.getX(), dy + mStickerTransformInfoBean.getY());
    }

    @Override
    public void setAngleBy(float da) {
        setAngle(da + mStickerTransformInfoBean.getAngle());
    }

    @Override
    public void setShowTime(long startTimeMs, long durationMs) {
        mStickerTransformInfoBean.setStartTimeMs(startTimeMs);
        mStickerTransformInfoBean.setDurationMs(durationMs);
    }

    @Override
    public void setCurrentTimeMs(long timeMs) {
        long endTimeMs = mStickerTransformInfoBean.getStartTimeMs() + mStickerTransformInfoBean.getDurationMs();
        if (timeMs >= mStickerTransformInfoBean.getStartTimeMs() && timeMs <= endTimeMs) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    @Override
    public void setScale(float scale) {
        mStickerTransformInfoBean.setScale(scale);
        mNode.setScale(scale);
    }

    @Override
    public void setWH(int w, int h) {
        mStickerTransformInfoBean.setWidth(w);
        mStickerTransformInfoBean.setHeight(h);
        mNode.setWH(w, h);
    }

    @Override
    public void remove() {
        mNode.remove();
    }

    @Override
    public void setTopLayer() {
        mNode.setTopLayer();
    }

    @Override
    public void setVisible(boolean visible) {
        mNode.setVisible(visible);
    }

    @Override
    public StickerTransformInfoBean getTransformationInfo() {
        return mStickerTransformInfoBean;
    }

    @Override
    public INode getNode() {
        return mNode;
    }
}
