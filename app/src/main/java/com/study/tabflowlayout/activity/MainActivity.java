package com.study.tabflowlayout.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.study.library.TabMoveLayout;
import com.study.tabflowlayout.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabMoveLayout tab_move_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab_move_layout = (TabMoveLayout) findViewById(R.id.tab_move_layout);
        List<String> data = new ArrayList<>();
        for(int i = 0;i<10;i++) {
            data.add("标签"+i);
        }
        tab_move_layout.setChildView(data, com.study.library.R.drawable.flag_01);
    }
}
