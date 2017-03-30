package com.study.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dengzhaoxuan on 2017/3/30.
 */

public class TabMoveLayout extends ViewGroup{
    private Context mContext;
    private float mItemScale;
    /**
     * 标签个数 4
     * |Magin|View|Magin|Magin|View|Magin|Magin|View|Magin|Magin|View|Magin|
     * 总宽度：4*（标签宽度+2*margin）  按照比例 （总份数）：4*（ITEM_WIDTH+2*MARGIN_WIDTH）
     * 则一个比例占的宽度为：组件宽度/总份数
     * 一个标签的宽度为：组件宽度/总份数 * ITEM_WIDTH(宽度占的比例)
     * 一个标签的MARGIN为：组件宽度/总份数 * MARGIN_WIDTH(MARGIN占的比例)
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
        this(context , null);
    }

    public TabMoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public TabMoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //注意要变成float型，不然int强转会导致误差，导致组件位置偏移
        mItemScale = getScreenWidth(context) * 1.0f / (ITEM_NUM*(ITEM_WIDTH+2*MARGIN_WIDTH));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left;
        int top;
        int right;
        int bottom;
        for(int i = 0;i<childCount;i++) {
            int row = i / 4;
            int column = i % 4;
            View child = getChildAt(i);
            left = (int) ((MARGIN_WIDTH + column*(ITEM_WIDTH + 2*MARGIN_WIDTH)) * mItemScale);
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
        }else{
            width = Math.min(sizeWidth,getScreenWidth(mContext));
        }

        if (modeHeight == MeasureSpec.EXACTLY) {
            height = sizeHeight;
        }else{
            int rowNum = childCount/ITEM_NUM;
            height = (int) Math.min(sizeHeight,rowNum*(ITEM_HEIGHT+2*MARGIN_HEIGHT)*mItemScale);
        }

        measureChildren(
                MeasureSpec.makeMeasureSpec((int) (mItemScale * ITEM_WIDTH), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (mItemScale * ITEM_HEIGHT), MeasureSpec.EXACTLY));
        setMeasuredDimension(width,height);
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
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

        }
        return super.onTouchEvent(event);
    }
}
