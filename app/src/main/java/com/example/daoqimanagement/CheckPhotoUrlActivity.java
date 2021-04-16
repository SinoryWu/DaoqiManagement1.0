package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CheckPhotoUrlActivity extends AppCompatActivity {

    private PhotoView photoView;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_check_photo_url_activity);
        photoView = findViewById(R.id.check_photo_url);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        /**
         * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
         */
        Glide.with(CheckPhotoUrlActivity.this).load(path)

                .into(photoView);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ActivityCollector.removeActivity(CheckPhotoUrlActivity.this);
                finish();

            }

            @Override
            public void onOutsidePhotoTap() {
                ActivityCollector.removeActivity(CheckPhotoUrlActivity.this);
                finish();

            }
        });

    }
}