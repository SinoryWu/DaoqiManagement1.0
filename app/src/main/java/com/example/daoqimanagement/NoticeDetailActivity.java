package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.utils.ActivityCollector;

public class NoticeDetailActivity extends AppCompatActivity {

    private TextView mTvType,mTvCreateTime,mTvContent;
    private RelativeLayout mRlFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_notice_detail);

        Intent intent =getIntent();
        String createTime = intent.getStringExtra("createTime");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");


        mRlFinish  = findViewById(R.id.notice_detail_rl_finish);
        mTvType = findViewById(R.id.notice_detail_tv_type);
        mTvCreateTime = findViewById(R.id.notice_detail_tv_time);
        mTvContent = findViewById(R.id.notice_detail_tv_content);


        mTvType.setText(title);
        mTvCreateTime.setText(createTime);
        mTvContent.setText(content);

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(NoticeDetailActivity.this);
                finish();
            }
        });
    }
}