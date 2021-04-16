package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.daoqimanagement.utils.ActivityCollector;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CheckPhotoActivity extends AppCompatActivity {

    private PhotoView checkPhoto;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_check_photo_acitvity);
        checkPhoto = findViewById(R.id.check_photo);

        Intent intent =getIntent();
        String uri = intent.getStringExtra("uri");
        checkPhoto.setImageURI(Uri.parse(uri));
        checkPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ActivityCollector.removeActivity(CheckPhotoActivity.this);
                finish();

            }

            @Override
            public void onOutsidePhotoTap() {
                ActivityCollector.removeActivity(CheckPhotoActivity.this);
                finish();

            }
        });

    }
}