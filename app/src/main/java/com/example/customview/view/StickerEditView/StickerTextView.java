package com.example.customview.view.StickerEditView;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.example.customview.view.StickerEditView.model.TextStyleBean;


/**
 * Desc: 自适应文字View
 */
public class StickerTextView extends View {
    private TextStyleHelp mTextStyleHelp;
    private boolean mIsShowDraw = true;

    public StickerTextView(Context context) {
        super(context);
        mTextStyleHelp = new TextStyleHelp();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mIsShowDraw) {
            mTextStyleHelp.draw(canvas);
        }
    }

    public void drawText(Canvas canvas) {
        mTextStyleHelp.draw(canvas);
    }

    public void setAdaptive(boolean isAdaptive) {
        mTextStyleHelp.setAdaptive(isAdaptive);
    }

    public void setMaxWH(int width, int height) {
        mTextStyleHelp.setMaxWH(width, height);
    }

    public void setTextStyle(TextStyleBean style) {
        mTextStyleHelp.setTextStyle(style);
        postInvalidate();
    }

    public void setText(String text) {
        // TODO: 2020-05-01 调整逻辑 
        if (!TextUtils.isEmpty(mTextStyleHelp.getText()) && mTextStyleHelp.getText().equals(text))
            return;

        mTextStyleHelp.setText(text);
        if (mTextStyleHelp.isAdaptive()) {
            //自适应要动态改变大小
            int width = mTextStyleHelp.getWidth();
            int height = mTextStyleHelp.getHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            setTranslationX(getTranslationX() + (lp.width - width) / 2f);
            setTranslationY(getTranslationY() + (lp.height - height) / 2f);
            lp.width = width;
            lp.height = height;
            this.setLayoutParams(lp);
        }
    }

    public String getText() {
        return mTextStyleHelp.getText();
    }

    public void setShowDraw(boolean isShowDraw) {
        mIsShowDraw = isShowDraw;
        postInvalidate();
    }

}
