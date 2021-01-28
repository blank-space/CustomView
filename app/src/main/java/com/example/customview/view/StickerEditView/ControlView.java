package com.example.customview.view.StickerEditView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;
import com.example.customview.view.StickerEditView.iface.ControlListener;
import com.example.customview.view.StickerEditView.iface.ISticker;
import com.example.customview.view.StickerEditView.iface.ITextSticker;
import com.example.customview.view.StickerEditView.iface.StickerTouchListener;
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean;


/**
 * 描述:手势操控
 * Created by wenzhiyuan on 2019-07-19
 * E-Mail Address：wenzhiyuan@cd.ebupt.com
 */
public class ControlView extends FrameLayout implements IControlView, View.OnTouchListener {
    //操控的控件大小 dp
    private final int CONTROL_SIZE = DensityUtils.dp2px(getContext(), 26);
    //间隔边线 dp
    private final int CONTEN_MARGIN = DensityUtils.dp2px(getContext(), 31);
    //贴纸最大缩放值 2
    private final float MAX_SCALE = 2;
    //最小缩放值
    private final float MIN_SCALE = 0.5f;
    /**
     * 最大缩放值，最小缩放值
     */
    private float maxScale = MAX_SCALE;
    private float minScale = MIN_SCALE;

    private ImageView mCloneIV, mEditIV;
    private View mContentView;
    private RectF mLineRectF = new RectF();
    private Paint paint;
    private PointF mOneDownPointF = new PointF(-1, -1);
    private PointF mTwoDownPointF = new PointF(-1, -1);
    //处理单击事件使用
    private PointF mDownPointF = new PointF();
    private boolean isTouchEditCtl;//是否触碰编辑按钮
    private boolean isHanlderTouchEvent;//是否处理触碰事件
    private ControlListener mControlListener;
    private StickerTouchListener mStickerTouchListener;
    private ISticker mSticker;//贴纸

    public ControlView(@NonNull Context context) {
        this(context, null);
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtils.dp2px(getContext(), 2));

        mContentView = new View(getContext());
        if (StickerConfig.IS_DEBUG) {
            mContentView.setBackgroundResource(R.drawable.pat_shape_white_box);
            setBackgroundResource(R.drawable.pat_shape_white_box);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
        LayoutParams clp = new LayoutParams(100, 100);
        clp.gravity = Gravity.CENTER;
        this.addView(mContentView, clp);

        int pading = 10;
        int btnW = CONTROL_SIZE + pading * 2;
        int btnH = btnW;
        mCloneIV = new ImageView(getContext());
        mCloneIV.setPadding(pading, pading, pading, pading);
        mCloneIV.setImageResource(R.drawable.pat_ic_edit_close);
        LayoutParams closeLp = new LayoutParams(btnW, btnH);
        this.addView(mCloneIV, closeLp);

        mEditIV = new ImageView(getContext());
        mEditIV.setPadding(pading, pading, pading, pading);
        mEditIV.setImageResource(R.drawable.pat_ic_edit_rotate);
        LayoutParams editLp = new LayoutParams(btnW, btnH);
        editLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        this.addView(mEditIV, editLp);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        mCloneIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mControlListener != null && mSticker != null) {
                    try {
                        mControlListener.onClickDelete(mSticker.getTransformationInfo().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mEditIV.setOnTouchListener(this);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN && v == mEditIV) {
            isTouchEditCtl = true;
        }
        isHanlderTouchEvent = true;
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLineRectF.set(0, 0, canvas.getWidth(), canvas.getHeight());
        mLineRectF.inset(mCloneIV.getWidth() / 2, mCloneIV.getWidth() / 2);
        canvas.drawRoundRect(mLineRectF, 2, 2, paint);
    }

    public boolean handlerTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        if (isHanlderTouchEvent) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mDownPointF.set(event.getX(), event.getY());
                    if (mStickerTouchListener != null) {
                        mStickerTouchListener.onActionDown(mSticker.getTransformationInfo().getId());
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mDownPointF.set(event.getX(), event.getY());
                    updataRecordPoint(-1, -1, -1, -1);
                    isTouchEditCtl = false;
                case MotionEvent.ACTION_POINTER_UP:
                    updataRecordPoint(-1, -1, -1, -1);
                    break;
                case MotionEvent.ACTION_UP:
                    handlerSingleClick(event.getX(), event.getY());
                    isHanlderTouchEvent = false;
                    updataRecordPoint(-1, -1, -1, -1);
                    isTouchEditCtl = false;
                    if (mStickerTouchListener != null) {
                        mStickerTouchListener.onActionUp(mSticker.getTransformationInfo().getId());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x1 = event.getX();
                    float y1 = event.getY();
                    if (event.getPointerCount() == 1) {
                        if (isTouchEditCtl) { //编辑状态（旋转，缩放）

                            float cx = getTranslationX() + getWidth() / 2;
                            float cy = getTranslationY() + getHeight() / 2;
                            handlerScale(x1, y1, cx, cy);
                            handlerRotate(x1, y1, cx, cy);
                            updataRecordPoint(x1, y1, cx, cy);
                            updataLayoutUI();
                            callControlListener();
                        } else {//移动
                            handlerMove(x1, y1);
                            updataRecordPoint(x1, y1, -1, -1);
                            updataLayoutUI();
                            callControlListener();
                            if (mStickerTouchListener != null) {
                                float cx = mSticker.getTransformationInfo().getX() + mSticker.getTransformationInfo().getWidth() / 2;
                                float cy = mSticker.getTransformationInfo().getY() + mSticker.getTransformationInfo().getHeight() / 2;
                                mStickerTouchListener.onActionMove(mSticker.getTransformationInfo().getId(), cx, cy);
                            }
                        }
                    } else if (event.getPointerCount() == 2) {
                        //缩放
                        float x2 = event.getX(1);
                        float y2 = event.getY(1);

                        handlerScale(x1, y1, x2, y2);
                        handlerRotate(x1, y1, x2, y2);
                        updataRecordPoint(x1, y1, x2, y2);
                        updataLayoutUI();
                        callControlListener();
                    }
                    break;
            }
        }

        return isHanlderTouchEvent;
    }

    /**
     * 移动处理
     */
    private void handlerMove(float x, float y) {

        if (isValidPoint(mOneDownPointF)) {

            float dx = x - mOneDownPointF.x;
            float dy = y - mOneDownPointF.y;
            //限制贴纸移动范围，防止操作贴纸操作区域，无法继续操作
            float cX = getTranslationX() + getWidth() / 2;
            float cY = getTranslationY() + getHeight() / 2;
            float maxWidth = ((ViewGroup) getParent()).getWidth();
            float maxHeight = ((ViewGroup) getParent()).getHeight();
            float newX = cX + dx;
            float newY = cY + dy;
            if (newX < 0) {
                dx = -cX;
            } else if (newX > maxWidth) {
                dx = maxWidth - cX;
            }
            if (newY < 0) {
                dy = -cY;
            } else if (newY > maxHeight) {
                dy = maxHeight - cY;
            }
            mSticker.setPositionBy(dx, dy);
        }
    }

    /**
     * 缩放处理
     */
    private void handlerScale(float x1, float y1, float x2, float y2) {

        if (isValidPoint(mTwoDownPointF)) {
            float oldLength = DensityUtils.getLength(mOneDownPointF.x, mOneDownPointF.y, mTwoDownPointF.x, mTwoDownPointF.y);
            float newLength = DensityUtils.getLength(x1, y1, x2, y2);
            float scale = newLength / oldLength * mSticker.getTransformationInfo().getScale();
            if (scale > maxScale) {
                scale = maxScale;
            } else if (scale < minScale) {
                scale = minScale;
            }
            mSticker.setScale(scale);
        }
    }

    /**
     * 旋转处理
     */
    private void handlerRotate(float x1, float y1, float x2, float y2) {

        if (isValidPoint(mTwoDownPointF)) {
            float angle1 = getRotation(mOneDownPointF.x, mOneDownPointF.y, mTwoDownPointF.x, mTwoDownPointF.y);
            float angle2 = getRotation(x1, y1, x2, y2);
            mSticker.setAngleBy(angle2 - angle1);
        }
    }

    /**
     * 刷新记录点
     */
    private void updataRecordPoint(float x1, float y1, float x2, float y2) {

        mOneDownPointF.set(x1, y1);
        mTwoDownPointF.set(x2, y2);
    }

    /**
     * Desc: 单击事件处理
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-08-06
     *
     * @param x
     * @param y
     */
    private void handlerSingleClick(float x, float y) {

        double length = DensityUtils.getLength(mDownPointF.x, mDownPointF.y, x, y);
        if (length < 5 && mControlListener != null) {
            mControlListener.onClickSticker(mSticker.getTransformationInfo().getId(),
                    mSticker.getTransformationInfo().getType());
        }
    }

    /**
     * Desc:  处理手势回调
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-30
     */
    private void callControlListener() {

        if (mControlListener != null) {
            StickerTransformInfoBean stickerTransformInfoBean = mSticker.getTransformationInfo();
            mControlListener.onChange(stickerTransformInfoBean.getId(),
                    stickerTransformInfoBean.getX(), stickerTransformInfoBean.getY(),
                    stickerTransformInfoBean.getScale(), stickerTransformInfoBean.getAngle());
        }
    }

    /**
     * Desc: 点是否有效
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-20
     *
     * @param pointF
     * @return boolean
     */
    private boolean isValidPoint(PointF pointF) {
        if (pointF.x != -1 && pointF.y != -1) {
            return true;
        } else {
            return false;
        }
    }

    private void setTranslation(float x, float y) {
        setTranslationX(x);
        setTranslationY(y);
    }

    /**
     * Desc: 刷新UI布局
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-20
     */
    private void updataLayoutUI() {
        StickerTransformInfoBean transformationInfo = mSticker.getTransformationInfo();
        //贴纸内容大小
        LayoutParams contentLp = (LayoutParams) mContentView.getLayoutParams();
        contentLp.width = transformationInfo.getWidth();
        contentLp.height = transformationInfo.getHeight();
        mContentView.setLayoutParams(contentLp);
        mContentView.setScaleX(transformationInfo.getScale());
        mContentView.setScaleY(transformationInfo.getScale());
        //缩放后实际内容大小，
        float realContentW = transformationInfo.getWidth() * transformationInfo.getScale();
        float realContentH = transformationInfo.getHeight() * transformationInfo.getScale();
        float offValX = (realContentW - transformationInfo.getWidth()) / 2;
        float offValY = (realContentH - transformationInfo.getHeight()) / 2;
        float margin = CONTEN_MARGIN;//间隔
        //位置
        int x = Math.round(transformationInfo.getX() - offValX - margin);
        int y = Math.round(transformationInfo.getY() - offValY - margin);
        setTranslation(x, y);
        //角度
        setRotation(transformationInfo.getAngle());
        //总大小
        LayoutParams rootLp = (LayoutParams) getLayoutParams();
        rootLp.width = Math.round(realContentW + margin * 2);
        rootLp.height = Math.round(realContentH + margin * 2);
        setLayoutParams(rootLp);
    }

    @Override
    public void setControlListener(ControlListener listener) {

        mControlListener = listener;
    }

    @Override
    public void setStickerTouchListener(StickerTouchListener touchListener) {

        mStickerTouchListener = touchListener;
    }

    /**
     * Desc:开启编辑
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-23
     *
     * @param sticker
     */
    @Override
    public void openControl(ISticker sticker) {

        if(mSticker != null && mSticker instanceof ITextSticker){
            ((ITextSticker) mSticker).setShowDraw(false);
        }
        mSticker = sticker;
        if (mSticker instanceof ITextSticker) {
            ((ITextSticker) mSticker).setShowDraw(true);
        }
//        initMinMaxScale();
        setVisibility(View.VISIBLE);
        updataLayoutUI();
    }

    /**
     * 初始化缩最大值，最小值
     */
    private void initMinMaxScale() {

        int parentWidth = ((ViewGroup) getParent()).getWidth();
        int parentHieght = ((ViewGroup) getParent()).getHeight();
        if (parentWidth != 0) {
            float minSide = Math.min(parentWidth, parentHieght);
            minScale = (minSide / 4f) / mSticker.getTransformationInfo().getWidth();
            maxScale = minSide / mSticker.getTransformationInfo().getWidth();
        } else {
            //第一次，view宽高还未初始化的时候
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    initMinMaxScale();
                }
            });
        }
    }

    /**
     * Desc:关闭贴纸操控
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-19
     */
    @Override
    public void closeControl() {
        this.setVisibility(View.GONE);
        if (mSticker instanceof ITextSticker) {
            ((ITextSticker) mSticker).setShowDraw(false);
        }
        mSticker = null;
    }

    @Override
    public void uptateControlArea() {

        updataLayoutUI();

    }

    @Override
    public ISticker getCurrentSticker() {
        return mSticker;
    }

    @Override
    public boolean isClose() {

        return this.getVisibility() != View.VISIBLE;
    }

    /**
     * Desc: 计算角度
     * <p>
     * Author: wenzhiyuan
     * Date: 2019-07-22
     *
     * @param cx 中心点
     * @param cy
     * @param x1 第一触碰点
     * @param y1
     * @param x2 第二触碰点
     * @param y2
     * @return float
     */
    private float caculatiotAngle(float cx, float cy, float x1, float y1, float x2, float y2) {
        float dx1, dx2, dy1, dy2;
        dx1 = x1 - cx;
        dy1 = y1 - cy;
        dx2 = x2 - cx;
        dy2 = y2 - cy;
        // 计算三边的平方
        float ab2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        float oa2 = dx1 * dx1 + dy1 * dy1;
        float ob2 = dx2 * dx2 + dy2 * dy2;
        // 根据两向量的叉乘来判断顺逆时针
        boolean isClockwise = ((x1 - cx) * (y2 - cy) - (y1 - cy) * (x2 - cx)) > 0;
        // 根据余弦定理计算旋转角的余弦值
        double cosDegree = (oa2 + ob2 - ab2) / (2 * Math.sqrt(oa2) * Math.sqrt(ob2));
        // 异常处理，因为算出来会有误差绝对值可能会超过一，所以需要处理一下
        if (cosDegree > 1) {
            cosDegree = 1;
        } else if (cosDegree < -1) {
            cosDegree = -1;
        }
        // 计算弧度
        double radian = Math.acos(cosDegree);
        // 计算旋转过的角度，顺时针为正，逆时针为负
        return (float) (isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));
    }

    /**
     * 计算角度
     */
    private float getRotation(float x1, float y1, float x2, float y2) {
        double delta_x = (x1 - x2);
        double delta_y = (y1 - y2);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
