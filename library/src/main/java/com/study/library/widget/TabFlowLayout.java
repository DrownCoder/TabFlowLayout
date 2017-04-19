package com.study.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.study.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengzhaoxuan on 2017/3/23.
 */

public class TabFlowLayout extends ViewGroup {
    private Context mContext;
    private View mMoveChildView;
    private View mChildTouchView;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.tab_shake);
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).startAnimation(animation);
                }
                return true;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).clearAnimation();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float beginx = 0;
        float beginy = 0;
        viewindex oldView = null;//触摸的view
        viewindex resultview;//落点view
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beginx = x;
                beginy = y;
                oldView = findChildIndex(beginx, beginy);
                if (oldView != null) {
                    mChildTouchView = findChild(oldView);
                    mChildTouchView.clearAnimation();
                    mChildTouchView.bringToFront();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mChildTouchView != null) {
                    moveTouchView(x, y);

                    resultview = findChildIndex(x, y);
                    if (compare(resultview, oldView) != 0) {
                        beginAnimation(compare(oldView, resultview) < 0 ? oldView : resultview
                                , compare(oldView, resultview) < 0 ? resultview : oldView
                                , compare(oldView, resultview) < 0 ? true : false);
                    }
                }


                break;
        }
        return super.onTouchEvent(event);
    }

    private void beginAnimation(viewindex oldindex, viewindex newindex, boolean b) {
        viewindex start = b ? nextView(oldindex) : oldindex;
        viewindex end = b ? nextView(newindex) : newindex;
        for(int i = start.x;i<=end.x;i++){
            for(int j = 0;j<mAllViews.get(i).size();j++){

            }
        }
    }

    /**
     * 移动触摸的View
     *
     * @param x
     * @param y
     */
    private void moveTouchView(float x, float y) {
        int left = (int) (x - mChildTouchView.getWidth() / 2);
        int top = (int) (y - mChildTouchView.getHeight() / 2);
        mChildTouchView.layout(left, top
                , (left + mChildTouchView.getWidth())
                , (top + mChildTouchView.getHeight()));
        mChildTouchView.invalidate();
    }

    /**
     * 根据触摸的坐标点确定触摸的View的坐标
     *
     * @param x
     * @param y
     * @return
     */
    private viewindex findChildIndex(float x, float y) {
        int row = 0;
        int column = 0;
        int height = 0;
        int width = 0;
        for (int i = 0; i < mLineHeight.size(); i++) {
            height += mLineHeight.get(i);
            Log.e("y", y + "");
            Log.e("height", height + "");
            if (y < height) {
                row = i;
                break;
            }
        }
        if (y > height) return null;
        List<View> lineView = mAllViews.get(row);
        for (int i = 0; i < lineView.size(); i++) {
            View child = lineView.get(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            width += child.getWidth() + lp.leftMargin + lp.rightMargin;
            if (x < width) {
                column = i;
                break;
            }
        }
        return new viewindex(row, column);
    }

    private View findChild(viewindex index) {
        return mAllViews.get(index.x).get(index.y);
    }

    private viewindex nextView(viewindex index) {
        if (index.y + 1 >= mAllViews.get(index.x).size()) {
            index.x++;
            index.y = 0;
        } else {
            index.y++;
        }
        return index;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width
                , modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineWidth = 0;
            }
            lineWidth += childWidth + lp.rightMargin + lp.leftMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0;
        int top = 0;
        int linenum = mAllViews.size();
        for (int i = 0; i < linenum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            top += lineHeight;
            left = 0;
        }

    }

    private class viewindex {
        int x;
        int y;

        public viewindex(int row, int column) {
            x = row;
            y = column;
        }
    }

    public int compare(viewindex o1, viewindex o2) {
        if (o1.x < o2.x) {
            return -1;
        } else if (o1.x == o1.x) {
            if (o1.y < o2.y) {
                return -1;
            } else if (o1.y == o2.y) {
                return 0;
            }
        }
        return 1;
    }
}
