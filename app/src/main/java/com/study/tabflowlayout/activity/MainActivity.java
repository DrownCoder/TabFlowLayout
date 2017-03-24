package com.study.tabflowlayout.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.study.tabflowlayout.R;
import com.study.tabflowlayout.view.CommonTitleBar;

public class MainActivity extends AppCompatActivity {
    private CommonTitleBar title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (CommonTitleBar) findViewById(R.id.title_layout);
        title.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
