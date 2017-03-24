package com.study.tabflowlayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.tabflowlayout.R;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

/**
 * Created by dengzhaoxuan on 2017/3/24.
 */

public class CommonTitleBar extends RelativeLayout {
    private Context mContext;

    private ImageView mLeftImg;
    private TextView mLeftBtn;
    private FrameLayout mCenterContainer;
    private ImageView mRightImg;
    private TextView mRightBtn;

    //左边icon
    private int mLeftImgIcon;
    //左边text
    private String mLeftText;
    /**
     * 中间显示样式
     * ---enum
     * 0-文字
     * 1-搜索框
     */
    private int mCenterType;
    //右边icon
    private int mRightImgIcon;
    //右边text
    private String mRightText;
    //标题栏颜色
    private int mTitleColor;
    //标题栏文字
    private String mTitleStr;
    //标题栏搜索框hiht
    private String mTitleSearchHiht;
    private EditText mSearchView;

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        // 如果后续有文字按钮，可使用该模式设置
        mLeftText = arr.getString(R.styleable.CommonTitleBar_leftBtnTxt);
        mLeftImgIcon = arr.getResourceId(R.styleable.CommonTitleBar_leftBtnIcon, 0);
        mRightText = arr.getString(R.styleable.CommonTitleBar_rightBtnTxt);
        mRightImgIcon = arr.getResourceId(R.styleable.CommonTitleBar_rightBtnIcon, 0);
        mTitleColor = arr.getInt(R.styleable.CommonTitleBar_titlecolor, R.color.white);
        mCenterType = arr.getInt(R.styleable.CommonTitleBar_CenterType, 0);
        if (isInEditMode()) {
            LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
            return;
        }

        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this);
        arr.recycle();
    }

    @Override
    protected void onFinishInflate() {
        if (isInEditMode()) {
            return;
        }
        mLeftImg = (ImageView) findViewById(R.id.normal_title_left_ig);
        mLeftBtn = (TextView) findViewById(R.id.normal_title_left_tv);
        mCenterContainer = (FrameLayout) findViewById(R.id.normal_Base_Title_Center_Container);
        mRightBtn = (TextView) findViewById(R.id.normal_title_right_tv);
        mRightImg = (ImageView) findViewById(R.id.normal_title_right_ig);

        if (mLeftImgIcon != 0) {
            mLeftImg.setImageResource(mLeftImgIcon);
            mLeftImg.setVisibility(VISIBLE);
        } else {
            mLeftImg.setVisibility(GONE);
        }
        if (mRightImgIcon != 0) {
            mRightImg.setImageResource(mRightImgIcon);
            mRightImg.setVisibility(VISIBLE);
        } else {
            mRightImg.setVisibility(GONE);
        }

        setLeftTxtBtn(mLeftText);
        setCenterType();
        setRightTxtBtn(mRightText);
        setBackgroundColor(mTitleColor);
    }

    /**
     * 设置左边按钮文字
     */
    private void setLeftTxtBtn(String mLeftText) {
        if (!TextUtils.isEmpty(mLeftText)) {
            mLeftBtn.setText(mLeftText);
            mLeftBtn.setVisibility(VISIBLE);
        } else {
            mLeftBtn.setVisibility(GONE);
        }
    }

    /**
     * 设置中间样式
     */
    private void setCenterType() {
        switch (mCenterType) {
            case 0://文字
                addViewToCenter(getCenterTextView());
                break;
            case 1://搜索框
                mSearchView= getCenterSearchView();
                addViewToCenter(mSearchView);
                break;
        }
    }

    /**
     * 中间搜索框样式
     */
    private EditText getCenterSearchView() {
        EditText edittext = new EditText(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        edittext.setLayoutParams(layoutParams);
        edittext.setBackgroundResource(R.mipmap.search_box_bg);
        edittext.setGravity(Gravity.CENTER_VERTICAL);
        edittext.setHint(mTitleSearchHiht);
        edittext.setImeOptions(IME_ACTION_SEARCH);
        edittext.setPadding(60, 0, 0, 0);
        edittext.setTextAppearance(mContext, R.style.Fuxk_Base_Tip_Black_Style);
        return edittext;
    }

    /**
     * 中间TextView样式
     */
    @NonNull
    private TextView getCenterTextView() {
        TextView textView = new TextView(mContext);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(mTitleStr);
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        textView.setTextAppearance(mContext, R.style.Fuxk_Base_Title_Style);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 添加View到中间FrameLayout
     */
    private void addViewToCenter(View view) {
        mCenterContainer.addView(view);
    }

    /**
     * 设置标题
     */
    public void setTitle(String str) {
        this.mTitleStr = str;
    }
    /**
     * 设置Hiht
     */
    public void setHint(String str){
        this.mTitleSearchHiht = str;
    }


    /**
     * 设置右边按钮文字
     */
    private void setRightTxtBtn(String mRightText) {
        if (!TextUtils.isEmpty(mRightText)) {
            mRightBtn.setText(mRightText);
            mRightBtn.setVisibility(VISIBLE);
        } else {
            mRightBtn.setVisibility(GONE);
        }
    }
    public void setLeftBtnOnclickListener(OnClickListener listener) {
        findViewById(R.id.normal_title_left_layout).setOnClickListener(listener);
    }
    public void setRightBtnOnclickListener(OnClickListener listener) {
        findViewById(R.id.normal_title_right_layout).setOnClickListener(listener);
    }
    public void setOnTextChangeListener(TextWatcher watcher){
        if(mSearchView!=null){
            mSearchView.addTextChangedListener(watcher);
        }
    }

}
