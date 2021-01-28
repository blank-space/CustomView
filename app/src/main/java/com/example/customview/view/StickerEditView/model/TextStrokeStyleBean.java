package com.example.customview.view.StickerEditView.model;


public class TextStrokeStyleBean {

    private String strokeColor;//描边颜色
    private float strokeWidth;//描边宽

    public TextStrokeStyleBean(String strokeColor, float strokeWidth) {

        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
