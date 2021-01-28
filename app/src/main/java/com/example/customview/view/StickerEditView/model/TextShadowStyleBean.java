package com.example.customview.view.StickerEditView.model;


public class TextShadowStyleBean {

    private String shadowColor;
    private float radius;//radius越大越模糊，越小越清晰
    private float dx,dy;

    public TextShadowStyleBean(String color, float radius, float dx, float dy){

        this.shadowColor = color;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
    }

    public String getShadowColor() {
        return shadowColor;
    }

    public float getRadius() {
        return radius;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
}
