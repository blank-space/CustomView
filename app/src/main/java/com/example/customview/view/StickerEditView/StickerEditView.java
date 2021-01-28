package com.example.customview.view.StickerEditView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;
import com.example.customview.view.StickerEditView.iface.ControlListener;
import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.iface.ITextSticker;
import com.example.customview.view.StickerEditView.iface.StickerEditListener;
import com.example.customview.view.StickerEditView.iface.StickerTouchListener;
import com.example.customview.view.StickerEditView.iface.TextAreaChangeListener;
import com.example.customview.view.StickerEditView.impl.ImageSticker;
import com.example.customview.view.StickerEditView.impl.Node;
import com.example.customview.view.StickerEditView.impl.StickerContainer;
import com.example.customview.view.StickerEditView.impl.TextNode;
import com.example.customview.view.StickerEditView.impl.TextSticker;
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;
import com.example.customview.view.StickerEditView.model.TextStyleBean;

/**
 * @author : LeeZhaoXing
 * @date : 2021/1/25
 * @desc :
 */
public class StickerEditView extends FrameLayout {
    private StickerContainer mStickerContainer = new StickerContainer();
    private StickerControlView mStickerControlView;
    private StickerEditListener mStickerEditListener;
    private StickerTouchListener mStickerTouchListener;

    public StickerEditView(@NonNull Context context) {
        this(context, null);
    }

    public StickerEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeStickerCtl();
            }
        });
    }

    /**
     * Desc:添加文字贴纸
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-27
     */
    public void addTextSticker(StickerTransformInfoBean stickerTransformInfoBean) {
        stickerTransformInfoBean.setType(StickerTransformInfoBean.TEXT_STICKER_TYPE);
        ISticker textSticker = createSticker(stickerTransformInfoBean);
        openStickerControl(textSticker);
    }

    /**
     * Desc:添加贴纸
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-18
     */
    public void addSticker(StickerTransformInfoBean stickerTransformInfoBean, boolean isOpenControl) {
        ImageSticker imageSticker = createSticker(stickerTransformInfoBean);
        if (isOpenControl) {
            openStickerControl(imageSticker);
        }
    }

    public void removeAllSticker() {
        this.removeAllViews();
        mStickerContainer.removeAllSticker();
    }

    /**
     * Desc: 删除贴纸
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-25
     *
     * @param id
     */
    public void removeSticker(String id) {
        ISticker sticker = mStickerContainer.removeSticker(id);
        if(sticker != null){
            sticker.remove();
            closeStickerCtl();
        }
    }

    public void changeStickerTime(String id, long startTimeMs, long durationMs) {
        mStickerContainer.changeStickerTime(id, startTimeMs, durationMs);
    }

    private ImageSticker createSticker(StickerTransformInfoBean stickerTransformInfoBean) {
        View view;
        ImageSticker sticker;
        if (stickerTransformInfoBean.getType() == StickerTransformInfoBean.TEXT_STICKER_TYPE) {
            //文字贴纸
            view = new StickerTextView(getContext());
            this.addView(view);
            sticker = new TextSticker(stickerTransformInfoBean, new TextNode((StickerTextView) view), new TextAreaChangeListener() {
                @Override
                public void onChangeTextArea(String id, float x, float y, float width, float height) {
                    // TODO: 2020-05-01 文字范围变化，刷新控制范围
                    if (mStickerControlView != null && mStickerControlView.getVisibility() == View.VISIBLE) {
                        mStickerControlView.uptateControlArea();
                    }
                    if (mStickerEditListener != null) {
                        mStickerEditListener.onChangeTextArea(id, x, y, width, height);
                    }
                }
            });
        } else {
            //图盘贴纸
            view = new View(getContext());
            this.addView(view);
            sticker = new ImageSticker(stickerTransformInfoBean, new Node(view));
        }
        view.setTag(R.id.sticker_id, stickerTransformInfoBean.getId());
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setOnClickListener(new OnDefenseQuicklyClickListener() {

            @Override
            protected void onQuicklyClick(View v) {
                if (v.getTag(R.id.sticker_id) != null) {
                    ISticker sticker = mStickerContainer.getSticker((String) v.getTag(R.id.sticker_id));
                    openStickerControl(sticker);
                }
            }
        });

        mStickerContainer.addSticker(sticker);
        return sticker;
    }

    /**
     * Desc:开启贴纸操控
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-18
     *
     * @param sticker
     */
    private void openStickerControl(ISticker sticker) {
        boolean isSuccess = openStickerControlUI(sticker);
        if (isSuccess && mStickerEditListener != null) {
            mStickerEditListener.onOpenEdit(sticker.getTransformationInfo().getId());
        }
    }

    /**
     * Desc:开启编辑状态，不会有回调处理ControlListener
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-25
     *
     * @param id
     */
    public void openStickerControl(String id) {
        openStickerControlUI(mStickerContainer.getSticker(id));
    }

    private boolean openStickerControlUI(ISticker sticker) {
        if (sticker == null) return false;
        if (mStickerControlView == null) {
            mStickerControlView = new StickerControlView(getContext());
            mStickerControlView.setControlListener(new ControlListener() {
                @Override
                public void onClickDelete(String id) {
                    removeSticker(id);
                    if (mStickerEditListener != null) {
                        mStickerEditListener.onClickDelete(id);
                    }
                }

                @Override
                public void onChange(String id, float x, float y, float scale, float angle) {
                    if (mStickerEditListener != null) {
                        mStickerEditListener.onChange(id, x, y, scale, angle);
                    }
                }

                @Override
                public void onClickSticker(String id, int type) {
                    if (mStickerEditListener != null) {
                        mStickerEditListener.onClickSticker(id, type);
                    }
                }
            });
            mStickerControlView.setStickerTouchListener(mStickerTouchListener);
        }
        if (mStickerControlView.getCurrentSticker() != sticker) {
            removeView(mStickerControlView);
            addView(mStickerControlView, new LayoutParams(getWidth(), getHeight()));
            mStickerControlView.openControl(sticker);
            return true;
        }
        return false;
    }

    private void closeStickerCtl() {
        boolean isClose = closeStickerControlUI();
        if (isClose && mStickerEditListener != null) {
            mStickerEditListener.onCloseEdit();
        }
    }

    /**
     * Desc:关闭手势操控，不处理关闭监听 StickerEditListener
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-25
     */
    public void closeStickerControl() {
        closeStickerControlUI();
    }

    private boolean closeStickerControlUI() {
        if (mStickerControlView == null || mStickerControlView.isClose()) {
            return false;
        }
        if (mStickerControlView != null) {
            mStickerControlView.closeControl();
        }
        return true;
    }

    public void setCurrentTime(int timeMs) {
        mStickerContainer.setCurrentTime(timeMs);
    }

    public void changeTextStickerText(String id, String text) {
        mStickerContainer.changeTextStickerText(id, text);
    }

    public void setControlListener(StickerEditListener listener) {
        mStickerEditListener = listener;
        if (mStickerControlView != null) {
            mStickerControlView.setControlListener(mStickerEditListener);
        }
    }

    public String getCurrentControlStickerId() {
        if (mStickerControlView != null) {
            ISticker sticker = mStickerControlView.getCurrentSticker();
            if (sticker != null) {
                return sticker.getTransformationInfo().getId();
            }
        }
        return null;
    }

    public void setStickerTextStyle(String id, TextStyleBean textStyle) {
        ISticker sticker = mStickerContainer.getSticker(id);
        if (sticker instanceof ITextSticker) {
            ((ITextSticker) sticker).setTextStyle(textStyle);
        }
    }

    public Bitmap getTextStickerBitmap(String id) {
        ISticker sticker = mStickerContainer.getSticker(id);
        if (sticker instanceof ITextSticker) {
            return ((ITextSticker) sticker).getBitmap();
        }
        return null;
    }

    public void setStickerTouchListener(StickerTouchListener listener) {
        mStickerTouchListener = listener;
    }

    public void setStickerTopLayer(String id){
        if (mStickerContainer != null){
            ISticker sticker = mStickerContainer.getSticker(id);
            if(sticker !=null){
                sticker.getNode().setTopLayer();
            }
        }
    }
}