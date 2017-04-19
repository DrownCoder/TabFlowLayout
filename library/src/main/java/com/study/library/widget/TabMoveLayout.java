package com.study.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.study.library.R;
import com.study.library.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuan on 2017/3/30.
 */

public class TabMoveLayout extends ViewGroup {
    private Context mContext;
    private Animation mSnake;
    //是否允许拖动
    private boolean isMove = false;
    private float mItemScale;
    //触摸的View
    private View mTouchChildView;
    //动画时间
    private int mDuration = 100;
    //触摸View的index
    private int mTouchIndex = -1;
    private int mOldIndex = -1;

    private float mBeginX = 0;
    private float mBeginY = 0;

    //是否处于触摸状态
    private boolean mOnHover = false;

    private List<String> mData;
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
    private int MARGIN_WIDTH;
    private final int MARGIN_WIDTH_DEFAULT = 5;
    //标签width-比例
    private int ITEM_WIDTH;
    private final int ITEM_WIDTH_DEFAULT = 26;
    //标签margin-height-比例
    private int MARGIN_HEIGHT;
    private int MARGIN_HEIGHT_DEFAULT = 4;
    //标签height-比例
    private int ITEM_HEIGHT;
    private int ITEM_HEIGHT_DEFAULT = 10;
    //标签背景
    private int ITEM_BACKGROUND;
    private final int ITEM_BACKGROUD_DEFAULT = R.drawable.flag_01;
    //文字颜色
    private int TEXT_COLOR;
    private final int TEXT_COLOR_DEDAULT = R.color.text_color;
    //文字大小
    private int TEXT_SIZE;

    public TabMoveLayout(Context context) {
        this(context, null);
    }

    public TabMoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabMoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
        initEvent();
    }

    private void initEvent() {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isMove = true;
                mSnake = AnimationUtils.loadAnimation(mContext, R.anim.tab_shake);
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).startAnimation(mSnake);
                }
                return true;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isMove = false;
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).clearAnimation();
                }
            }
        });
    }

    private void init() {
        //注意要变成float型，不然int强转会导致误差，导致组件位置偏移
        mItemScale = getScreenWidth(mContext) * 1.0f / (ITEM_NUM * (ITEM_WIDTH + 2 * MARGIN_WIDTH));
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.TabMoveLayout);
        ITEM_NUM = array.getInt(R.styleable.TabMoveLayout_itemNum, 4);
        ITEM_WIDTH = array.getInt(R.styleable.TabMoveLayout_itemWidth, 26);
        ITEM_HEIGHT = array.getInt(R.styleable.TabMoveLayout_itemHeight, 10);
        MARGIN_WIDTH = array.getInt(R.styleable.TabMoveLayout_widthMargin, 5);
        MARGIN_HEIGHT = array.getInt(R.styleable.TabMoveLayout_heightMargin, 4);
        ITEM_BACKGROUND = array.getResourceId(R.styleable.TabMoveLayout_tabBac, R.drawable.flag_01);
        TEXT_COLOR = array.getColor(R.styleable.TabMoveLayout_textColor, getResources().getColor(TEXT_COLOR_DEDAULT));
        TEXT_SIZE = (int) DensityUtils.sp2px(mContext,array.getDimensionPixelSize(R.styleable.TabMoveLayout_textSize, 16));
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left;
        int top;
        int right;
        int bottom;
        for (int i = 0; i < childCount; i++) {
            int row = i / ITEM_NUM;
            int column = i % ITEM_NUM;
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mOnHover;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(isMove){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBeginX = x;
                    mBeginY = y;
                    mTouchIndex = findChildIndex(x, y);
                    mOldIndex = mTouchIndex;
                    if (mTouchIndex != -1) {
                        mTouchChildView = getChildAt(mTouchIndex);
                        mTouchChildView.clearAnimation();
                        //mTouchChildView.bringToFront();
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mTouchIndex != -1 && mTouchChildView != null) {
                        moveTouchView(x, y);
                        //拖动过程中的View的index
                        int resultIndex = findChildIndex(x, y);
                        if (resultIndex != -1 && (resultIndex != mOldIndex)
                                && ((Math.abs(x - mBeginX) > mItemScale * 2 * MARGIN_WIDTH)
                                || (Math.abs(y - mBeginY) > mItemScale * 2 * MARGIN_HEIGHT))
                                ) {
                            beginAnimation(Math.min(mOldIndex, resultIndex)
                                    , Math.max(mOldIndex, resultIndex)
                                    , mOldIndex < resultIndex);
                            mOldIndex = resultIndex;
                            mOnHover = true;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    setTouchIndex(x, y);
                    mOnHover = false;
                    mTouchIndex = -1;
                    mTouchChildView = null;
                    return  true;
            }
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
    private void beginAnimation(int startIndex, int endIndex, final boolean forward) {
        TranslateAnimation animation;
        ViewHolder holder;
        List<TranslateAnimation> animList = new ArrayList<>();
        int startI = forward ? startIndex + 1 : startIndex;
        int endI = forward ? endIndex + 1 : endIndex;//for循环用的是<，取不到最后一个
        if (mOnHover) {//拖动没有释放情况
            if (mTouchIndex > startIndex) {
                if (mTouchIndex < endIndex) {
                    startI = startIndex;
                    endI = endIndex + 1;
                } else {
                    startI = startIndex;
                    endI = endIndex;
                }
            } else {
                startI = startIndex + 1;
                endI = endIndex + 1;
            }
        }

        //X轴的单位移动距离
        final float moveX = (ITEM_WIDTH + 2 * MARGIN_WIDTH) * mItemScale;
        //y轴的单位移动距离
        final float moveY = (ITEM_HEIGHT + 2 * MARGIN_HEIGHT) * mItemScale;
        //x轴移动方向
        final int directX = forward ? -1 : 1;
        final int directY = forward ? 1 : -1;
        boolean isMoveY = false;
        for (int i = startI; i < endI; i++) {
            if (i == mTouchIndex) {
                continue;
            }
            final View child = getChildAt(i);
            holder = (ViewHolder) child.getTag();
            child.clearAnimation();
            if (i % ITEM_NUM == (ITEM_NUM - 1) && !forward
                    && holder.row == i / ITEM_NUM && holder.column == i % ITEM_NUM) {
                //下移
                holder.row++;
                isMoveY = true;
                animation = new TranslateAnimation(0, directY * (ITEM_NUM - 1) * moveX, 0, directX * moveY);
            } else if (i % ITEM_NUM == 0 && forward
                    && holder.row == i / ITEM_NUM && holder.column == i % ITEM_NUM) {
                //上移
                holder.row--;
                isMoveY = true;
                animation = new TranslateAnimation(0, directY * (ITEM_NUM - 1) * moveX, 0, directX * moveY);
            } else if (mOnHover && holder.row < i / ITEM_NUM) {
                //onHover 下移
                holder.row++;
                isMoveY = true;
                animation = new TranslateAnimation(0, -(ITEM_NUM - 1) * moveX, 0, moveY);
            } else if (mOnHover && holder.row > i / ITEM_NUM) {
                //onHover 上移
                holder.row--;
                isMoveY = true;
                animation = new TranslateAnimation(0, (ITEM_NUM - 1) * moveX, 0, -moveY);
            } else {//y轴不动，仅x轴移动
                holder.column += directX;
                isMoveY = false;
                animation = new TranslateAnimation(0, directX * moveX, 0, 0);
            }
            animation.setDuration(mDuration);
            animation.setFillAfter(true);
            final boolean finalIsMoveY = isMoveY;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    child.clearAnimation();
                    if (finalIsMoveY) {
                        child.offsetLeftAndRight((int) (directY * (ITEM_NUM - 1) * moveX));
                        child.offsetTopAndBottom((int) (directX * moveY));
                    } else {
                        child.offsetLeftAndRight((int) (directX * moveX));
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
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

    public void setChildView(List<String> data) {
        setChildView(data, ITEM_BACKGROUD_DEFAULT);
    }

    public void setChildView(List<String> data, int bacID) {
        ITEM_BACKGROUND = bacID;
        this.removeAllViews();
        Log.e("Count", "" + getChildCount());
        if(data == null || data.size() == 0){
            return;
        }
        mData = data;
        TextView tv = null;
        LayoutParams params = new ViewGroup.LayoutParams((int) (mItemScale * ITEM_WIDTH),
                (int) (mItemScale * ITEM_HEIGHT));
        for(int i = 0;i<data.size();i++){
            tv = new AutoFitTextView(mContext);
            tv.setText(data.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_SIZE);
            tv.setGravity(Gravity.CENTER);
            tv.setTag(new ViewHolder(i / ITEM_NUM, i % ITEM_NUM));
            tv.setTextColor(TEXT_COLOR);
            tv.setBackgroundResource(ITEM_BACKGROUND);
            addView(tv,params);
        }
        this.postInvalidate();
    }

    /**
     * ---up事件触发
     * 设置拖动的View的位置
     * @param x
     * @param y
     */
    private void setTouchIndex(float x,float y){
        if(mTouchChildView!= null){
            int resultIndex = findChildIndex(x, y);
            Log.e("resultindex", "" + resultIndex);
            if(resultIndex == mTouchIndex||resultIndex == -1){
                refreshView(mTouchIndex);
            }else{
                swapView(mTouchIndex, resultIndex);
            }
        }
    }

    /**
     *刷新View
     * ------------------------------重要------------------------------
     * 移除前需要先移除View的动画效果，不然无法移除，可看源码
     */
    private void refreshView(int index) {
        //移除原来的View
        getChildAt(index).clearAnimation();
        removeViewAt(index);
        //添加一个View
        TextView tv = new AutoFitTextView(mContext);
        LayoutParams params = new ViewGroup.LayoutParams((int) (mItemScale * ITEM_WIDTH),
                (int) (mItemScale * ITEM_HEIGHT));
        tv.setText(mData.get(index));
        tv.setTextColor(TEXT_COLOR);
        tv.setBackgroundResource(ITEM_BACKGROUND);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_SIZE);
        tv.setTag(new ViewHolder(index / ITEM_NUM, index % ITEM_NUM));
        this.addView(tv,index ,params);
        tv.startAnimation(mSnake);
    }

    private void swapView(int fromIndex, int toIndex) {
        if(fromIndex < toIndex){
            mData.add(toIndex+1,mData.get(fromIndex));
            mData.remove(fromIndex);
        }else{
            mData.add(toIndex,mData.get(fromIndex));
            mData.remove(fromIndex+1);
        }

        for (int i = Math.min(fromIndex, toIndex); i <= Math.max(fromIndex, toIndex); i++) {
            refreshView(i);
        }
    }

    class ViewHolder {
        int row;
        int column;

        public ViewHolder(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
