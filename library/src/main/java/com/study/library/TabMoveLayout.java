package com.study.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengzhaoxuan on 2017/3/30.
 */

public class TabMoveLayout extends ViewGroup {
    private Context mContext;
    private float mItemScale;
    //触摸的View
    private View mTouchChildView;
    //动画时间
    private int mDuration = 100;
    //触摸View的index
    private int mTouchIndex = -1;
    private int mOldIndex = 0;
    /**
     * 标签个数 4
     * |Magin|View|Magin|Magin|View|Magin|Magin|View|Magin|Magin|View|Magin|
     * 总宽度：4*（标签宽度+2*margin）  按照比例 （总份数）：4*（ITEM_WIDTH+2*MARGIN_WIDTH）
     * 则一个比例占的宽度为：组件宽度/总份数
     * 一个标签的宽度为：组件宽度/总份数 * ITEM_WIDTH(宽度占的比例)
     * 一个标签的MARGIN为：组件宽度/总份数 * MARGIN_WIDTH(MARGIN占的比例)
     * 行高=(ITEN_HEIGHT+2*MARGIN_HEIGHT)*mItemScale
     * 一个组件占的宽度=(ITEM_WIDTH + 2*MARGIN_WIDTH)*mItemScale
     */
    //一行的标签个数
    private int ITEM_NUM = 4;
    //标签margin-width-比例
    private int MARGIN_WIDTH = 5;
    //标签width-比例
    private int ITEM_WIDTH = 26;
    //标签margin-height-比例
    private int MARGIN_HEIGHT = 4;
    //标签height-比例
    private int ITEM_HEIGHT = 10;

    public TabMoveLayout(Context context) {
        this(context, null);
    }

    public TabMoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabMoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //注意要变成float型，不然int强转会导致误差，导致组件位置偏移
        mItemScale = getScreenWidth(context) * 1.0f / (ITEM_NUM * (ITEM_WIDTH + 2 * MARGIN_WIDTH));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left;
        int top;
        int right;
        int bottom;
        for (int i = 0; i < childCount; i++) {
            int row = i / 4;
            int column = i % 4;
            View child = getChildAt(i);
            left = (int) ((MARGIN_WIDTH + column * (ITEM_WIDTH + 2 * MARGIN_WIDTH)) * mItemScale);
            top = (int) ((MARGIN_HEIGHT + row * (ITEM_HEIGHT + 2 * MARGIN_HEIGHT)) * mItemScale);
            right = (int) (left + ITEM_WIDTH * mItemScale);
            bottom = (int) (top + ITEM_HEIGHT * mItemScale);
            child.layout(left, top, right, bottom);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        int height;
        int childCount = getChildCount();
        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        } else {
            width = Math.min(sizeWidth, getScreenWidth(mContext));
        }

        if (modeHeight == MeasureSpec.EXACTLY) {
            height = sizeHeight;
        } else {
            int rowNum = childCount / ITEM_NUM;
            if (childCount % ITEM_NUM != 0) {
                height = (int) Math.min(sizeHeight, (rowNum + 1) * (ITEM_HEIGHT + 2 * MARGIN_HEIGHT) * mItemScale);
            } else {
                height = (int) Math.min(sizeHeight, rowNum * (ITEM_HEIGHT + 2 * MARGIN_HEIGHT) * mItemScale);
            }
        }

        measureChildren(
                MeasureSpec.makeMeasureSpec((int) (mItemScale * ITEM_WIDTH), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (mItemScale * ITEM_HEIGHT), MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);
    }

    /**
     * 获取屏幕宽度
     */
    private static int getScreenWidth(Context activity) {
        int screenWidth;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchIndex = findChildIndex(x, y);
                if (mTouchIndex != -1) {
                    mTouchChildView = getChildAt(mTouchIndex);
                    mTouchChildView.clearAnimation();
                    //mTouchChildView.bringToFront();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchIndex != -1 && mTouchChildView !=null) {
                    moveTouchView(x, y);
                    //拖动过程中的View的index
                    int resultIndex = findChildIndex(x, y);
                    if (resultIndex != mOldIndex || mOldIndex == mTouchIndex) {
                        beginAnimation(Math.min(mOldIndex, resultIndex)
                                , Math.max(mOldIndex, resultIndex)
                                , mOldIndex < resultIndex);
                        mOldIndex =resultIndex;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                mTouchIndex = -1;
                mTouchChildView = null;
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 移动动画
     *
     * @param forward 拖动组件与经过的index的前后顺序 touchindex < resultindex
     *                true-拖动的组件在经过的index前
     *                false-拖动的组件在经过的index后
     */
    private void beginAnimation(int startIndex, int endIndex, boolean forward) {
        TranslateAnimation animation;
        List<TranslateAnimation> animList = new ArrayList<>();
        int startI = forward ? startIndex + 1 : startIndex;
        int endI = forward ? endIndex + 1 : endIndex;//for循环用的是<，取不到最后一个
        //X轴的单位移动距离
        float moveX = (ITEM_WIDTH + 2 * MARGIN_WIDTH) * mItemScale;
        //y轴的单位移动距离
        float moveY = (ITEM_HEIGHT + 2 * MARGIN_HEIGHT)*mItemScale;
        //x轴移动方向
        int direct = forward ? -1 : 1;
        for (int i = startI; i < endI; i++) {
            View child = getChildAt(i);
            child.clearAnimation();
            if(i%ITEM_NUM == 0 && forward){
                //y轴需要移动：第一个组件需要上移到上一行最后一个
                animation = new TranslateAnimation(0, (ITEM_NUM - 1) * moveX, 0, direct * moveY);
            } else if (i % ITEM_NUM == (ITEM_NUM - 1) && !forward) {
                //y轴需要移动：最后一个组件需要下移到下一行第一个
                animation = new TranslateAnimation(0, -(ITEM_NUM - 1) * moveX, 0, moveY);
            }else{//y轴不动，仅x轴移动
                animation = new TranslateAnimation(0, direct * moveX, 0, 0);
            }
            animation.setDuration(mDuration);
            animation.setFillAfter(true);
            child.setAnimation(animation);
            animList.add(animation);
        }
        for (TranslateAnimation anim : animList) {
            anim.startNow();
        }


    }

    private void moveTouchView(float x, float y) {
        int left = (int) (x - mTouchChildView.getWidth() / 2);
        int top = (int) (y - mTouchChildView.getHeight() / 2);
        mTouchChildView.layout(left, top
                , (left + mTouchChildView.getWidth())
                , (top + mTouchChildView.getHeight()));
        mTouchChildView.invalidate();
    }

    /**
     * 通过触摸位置确定触摸位置的View
     */
    private int findChildIndex(float x, float y) {
        int row = (int) (y / ((ITEM_HEIGHT + 2 * MARGIN_HEIGHT) * mItemScale));
        int column = (int) (x / ((ITEM_WIDTH + 2 * MARGIN_WIDTH) * mItemScale));
        int index = row * ITEM_NUM + column;
        if (index > getChildCount() - 1) {
            return -1;
        }
        return index;
    }
}
