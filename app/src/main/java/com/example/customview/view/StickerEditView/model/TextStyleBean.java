package com.example.customview.view.StickerEditView.model;

import java.util.ArrayList;
import java.util.List;


public class TextStyleBean {

    private int id;
    private int iconResId;
    private String textColor;//文字颜色
    private float offsetY;// y轴偏移值
    private float padding;//四周间距
    //背景
    private String backgroundColor;
    //描边
    private List<TextStrokeStyleBean> textStrokeStyleBeanList;
    //阴影
    private TextShadowStyleBean textShadowStyleBean;
    //叠影
    private TextStyleBean repeatTextStyleBean;

    public TextStyleBean(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<TextStrokeStyleBean> getTextStrokeStyleBeanList() {
        return textStrokeStyleBeanList;
    }

    public void addStrokeStyle(TextStrokeStyleBean textStrokeStyleBean) {
        if (textStrokeStyleBeanList == null) {
            textStrokeStyleBeanList = new ArrayList<>();
        }
        textStrokeStyleBeanList.add(textStrokeStyleBean);
    }

    public void setTextStrokeStyleBeanList(List<TextStrokeStyleBean> textStrokeStyleBeanList) {
        this.textStrokeStyleBeanList = textStrokeStyleBeanList;
    }

    public TextShadowStyleBean getTextShadowStyleBean() {
        return textShadowStyleBean;
    }

    public void setTextShadowStyleBean(TextShadowStyleBean textShadowStyleBean) {
        this.textShadowStyleBean = textShadowStyleBean;
    }

    public TextStyleBean getRepeatTextStyleBean() {
        return repeatTextStyleBean;
    }

    public void setRepeatTextStyleBean(TextStyleBean repeatTextStyleBean) {
        this.repeatTextStyleBean = repeatTextStyleBean;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }
}
