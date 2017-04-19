package com.study.library.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dengzhaoxuan on 2017/4/11.
 */

public interface TabAdapter {
    /**
     * 返回Tab数量
     */
    int getCount();
    /**
     * 返回数据对象
     */
    Object getItem(int position);
    /**
     * 返回Tab的layout
     */
    View getView(int position, View convertView, ViewGroup parent);
}
