package com.example.customview.view.StickerEditView.impl;


import android.view.View;
import android.view.ViewGroup;

import com.example.customview.view.StickerEditView.iface.INode;

public class Node implements INode {

    private View view;

    public Node(View view) {
        this.view = view;
    }

    @Override
    public void setAngle(float angle) {
        view.setRotation(angle);
    }

    @Override
    public void setPosition(float x, float y) {
        view.setTranslationX(x);
        view.setTranslationY(y);
    }

    @Override
    public void setScale(float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    @Override
    public void setVisible(boolean visible) {
        int v = visible ? View.VISIBLE : View.INVISIBLE;
        view.setVisibility(v);
    }

    @Override
    public void setWH(int w, int h) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = w;
        lp.height = h;
        view.setLayoutParams(lp);
    }

    @Override
    public void remove() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void setTopLayer() {
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
            parent.addView(view);
        }
    }
}
