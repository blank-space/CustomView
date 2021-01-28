package com.example.customview.view.StickerEditView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;


import com.example.customview.view.StickerEditView.model.TextShadowStyleBean;
import com.example.customview.view.StickerEditView.model.TextStrokeStyleBean;
import com.example.customview.view.StickerEditView.model.TextStyleBean;

import java.util.List;

/**
 * Desc:
 */
public class TextStyleHelp {
    private int TIME = 5; //倍数，跟文字大小有关
    private float TEXT_SIZE = 40;
    private int PADDING = 10;

    private TextPaint mTextPaint = new TextPaint();
    private StaticLayout mStaticLayout;
    private String mTextContext;
    private int mMaxWidth;
    private int mMaxHeight;
    /**
     * 是否自适应文字内容，动态改变宽高
     */
    private boolean mIsAdaptive;
    private TextStyleBean mTextStyle;

    public TextStyleHelp() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);//粗体
        mTextPaint.setColor(Color.parseColor("#55000000"));
        mTextPaint.setStrokeWidth(10);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public synchronized void draw(Canvas canvas) {

        if (mStaticLayout != null) {
            canvas.save();
            float dx = (canvas.getWidth() - mStaticLayout.getWidth()) / 2;
            float dy = (canvas.getHeight() - mStaticLayout.getHeight()) / 2;
            canvas.translate(dx, dy);

            //叠影
            if (mTextStyle != null && mTextStyle.getRepeatTextStyleBean() != null) {
                canvas.translate(0, mTextStyle.getOffsetY() * 1.5f);
                drawText(canvas, mTextStyle.getRepeatTextStyleBean());
                canvas.translate(0, -mTextStyle.getOffsetY() * 1.5f);
            }
            drawText(canvas, mTextStyle);

            if (StickerConfig.IS_DEBUG) {
                /** 测试 绘制文字区 */
                Paint tempPaint = new Paint();
                tempPaint.setColor(Color.BLUE);
                tempPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(1, 1,
                        mStaticLayout.getWidth() - 1, mStaticLayout.getHeight() - 1, tempPaint);
            }
            canvas.restore();
        }

        if (StickerConfig.IS_DEBUG) {
            /** 测试 绘制文字区 */
            mTextPaint.setStyle(Paint.Style.STROKE);
            mTextPaint.setStrokeWidth(1);
            mTextPaint.setColor(Color.BLUE);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mTextPaint);
        }
    }

    private void drawText(Canvas canvas, TextStyleBean textStyleBean) {

        if (textStyleBean != null) {
            //背景
            if (!TextUtils.isEmpty(textStyleBean.getBackgroundColor())) {
                drawBackground(canvas, textStyleBean.getBackgroundColor());
            }
            //描边
            if (textStyleBean.getTextStrokeStyleBeanList() != null) {
                drawStroke(canvas, textStyleBean.getTextStrokeStyleBeanList());
            }
        }
        //文字
        String textColor = textStyleBean != null ? textStyleBean.getTextColor() : "#000000";
        TextShadowStyleBean shadowStyleBean = textStyleBean != null ? textStyleBean.getTextShadowStyleBean() : null;
        drawTextCore(canvas, textColor, shadowStyleBean);
    }

    /**
     * 背景
     */
    Rect lineRect = new Rect();

    private void drawBackground(Canvas canvas, String color) {
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.parseColor(color));
        for (int line = 0; line < mStaticLayout.getLineCount(); line++) {
            float lineWidth = mStaticLayout.getLineWidth(line);
            mStaticLayout.getLineBounds(line, lineRect);
            lineRect.inset(Math.round((lineRect.width() - lineWidth) / 2), 2);
            //检测是否有文字
            if (lineRect.width() >= mTextPaint.measureText("y")) {
                canvas.drawRect(lineRect, mTextPaint);
            }
        }
    }

    /**
     * 描边
     */
    private void drawStroke(Canvas canvas, List<TextStrokeStyleBean> textStrokeStyleList) {
        int maxStokeWidth = 0;
        for (TextStrokeStyleBean textStrokeStyle : textStrokeStyleList) {
            maxStokeWidth += textStrokeStyle.getStrokeWidth();
        }

        maxStokeWidth = maxStokeWidth * TIME;
        for (int index = textStrokeStyleList.size() - 1; index >= 0; index--) {
            TextStrokeStyleBean textStrokeStyle = textStrokeStyleList.get(index);
            mTextPaint.setColor(Color.parseColor(textStrokeStyle.getStrokeColor()));
            mTextPaint.setStrokeWidth(maxStokeWidth);
            mTextPaint.setTextSize(TEXT_SIZE);
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mStaticLayout.draw(canvas);
            maxStokeWidth -= textStrokeStyle.getStrokeWidth() * TIME;
        }
    }

    /**
     * 文字 / 阴影
     */
    private void drawTextCore(Canvas canvas, String color, TextShadowStyleBean shadowStyle) {
        mTextPaint.setColor(Color.parseColor(color));
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(TEXT_SIZE);
        //阴影
        if (shadowStyle != null) {
            mTextPaint.setShadowLayer(shadowStyle.getRadius() * TIME,
                    shadowStyle.getDx() * TIME, shadowStyle.getDy() * TIME,
                    Color.parseColor(shadowStyle.getShadowColor()));
        }
        mTextPaint.setStyle(Paint.Style.FILL);
        mStaticLayout.draw(canvas);
        mTextPaint.clearShadowLayer();
    }

    public void setAdaptive(boolean isAdaptive) {
        mIsAdaptive = isAdaptive;
    }


    public void setMaxWH(int mMaxWidth, int mMaxHeight) {
        this.mMaxWidth = mMaxWidth;
        this.mMaxHeight = mMaxHeight;
    }

    public void setTextStyle(TextStyleBean style) {
        mTextStyle = style;
    }

    public String getText() {
        return mTextContext;
    }

    public void setText(String text) {
        mTextContext = text;
        if (mIsAdaptive) {
            //自适应布局
            mTextPaint.setTextSize(TEXT_SIZE);
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

            mStaticLayout = new StaticLayout(mTextContext, mTextPaint, mMaxWidth, Layout.Alignment.ALIGN_CENTER,
                    1.0f, 0.0f, false);
            float maxLineWidth = 0;
            for (int line = 0; line < mStaticLayout.getLineCount(); line++) {
                if (maxLineWidth < mStaticLayout.getLineWidth(line)) {
                    maxLineWidth = mStaticLayout.getLineWidth(line);
                }
            }

            mStaticLayout = new StaticLayout(mTextContext, mTextPaint, Math.round(maxLineWidth), Layout.Alignment.ALIGN_CENTER,
                    1.0f, 0.0f, false);

        } else {
            //非自适布局
            mTextPaint.setTextSize(mMaxHeight);
            mStaticLayout = new StaticLayout(text, mTextPaint, mMaxWidth,
                    Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

            while (mStaticLayout.getHeight() > mMaxHeight) {
                mTextPaint.setTextSize(mTextPaint.getTextSize() * 0.9f);  //修改文字大小
                mStaticLayout = new StaticLayout(text, mTextPaint, mMaxWidth,
                        Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
        }
    }

    public int getWidth() {
        return mStaticLayout.getWidth() + PADDING;
    }

    public int getHeight() {
        return mStaticLayout.getHeight() + PADDING;
    }

    public boolean isAdaptive() {
        return mIsAdaptive;
    }

}
