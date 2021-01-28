package com.example.customview.view.StickerEditView.model;


public class StickerTransformInfoBean {

    public static int IMAGE_STICKER_TYPE = 1;
    public static int TEXT_STICKER_TYPE = 2;

    private String id;
    private int type;
    private float angle;
    private float x, y;
    private float scale = 1;
    private int width, height;
    private long startTimeMs;
    private long durationMs;
    private boolean isAdaptive;//宽高是否自适应，根据文字内容多少动态改变宽高
    private String text; //文字内容
    private int textWidth;//文字范围宽
    private int textHeight;//文字范围的高

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public void setStartTimeMs(long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long duration) {
        this.durationMs = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAdaptive() {
        return isAdaptive;
    }

    public void setAdaptive(boolean adaptive) {
        isAdaptive = adaptive;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
    }

    public int getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }

    private StickerTransformInfoBean() {
    }

    public static class Builder {

        private StickerTransformInfoBean stickerTransformInfoBean;

        public Builder() {
            stickerTransformInfoBean = new StickerTransformInfoBean();
        }

        public Builder setId(String id) {
            stickerTransformInfoBean.id = id;
            return this;
        }

        public Builder setType(int type){
            stickerTransformInfoBean.type = type;
            return this;
        }

        public Builder setPosition(float x, float y) {
            stickerTransformInfoBean.x = x;
            stickerTransformInfoBean.y = y;
            return this;
        }

        public Builder setWH(int w, int h) {
            stickerTransformInfoBean.width = w;
            stickerTransformInfoBean.height = h;
            return this;
        }

        public Builder setAngle(float angle) {
            stickerTransformInfoBean.angle = angle;
            return this;
        }

        public Builder setScale(float scale) {
            stickerTransformInfoBean.scale = scale;
            return this;
        }

        public Builder setTime(long startTimeMs, long duration) {
            stickerTransformInfoBean.startTimeMs = startTimeMs;
            stickerTransformInfoBean.durationMs = duration;
            return this;
        }

        public Builder setAdaptive(boolean isAdaptive){
            stickerTransformInfoBean.setAdaptive(isAdaptive);
            return this;
        }

        public Builder setText(String text){
            stickerTransformInfoBean.setText(text);
            return this;
        }

        public Builder setTextWH(int width,int height){
            stickerTransformInfoBean.textWidth = width;
            stickerTransformInfoBean.textHeight = height;
            return this;
        }

        public StickerTransformInfoBean build() {
            return stickerTransformInfoBean;
        }

        public static Builder create() {

            return new Builder();
        }
    }
}
