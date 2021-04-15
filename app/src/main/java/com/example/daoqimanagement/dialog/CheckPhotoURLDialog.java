package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.CheckPhotoUrlActivity;
import com.example.daoqimanagement.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CheckPhotoURLDialog extends Dialog {
    private PhotoView photoView;
    private String url;
    public CheckPhotoURLDialog(@NonNull Context context) {
        super(context);
    }

    public CheckPhotoURLDialog(@NonNull Context context, int themeResId, String url) {
        super(context, themeResId);
        this.url = url;
    }

    protected CheckPhotoURLDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_photo_uri_dialog);




        photoView = findViewById(R.id.check_photo_dialog_uri);
        /**
         * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
         */
        Glide.with(getContext()).load(url)

                .into(photoView);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }

            @Override
            public void onOutsidePhotoTap() {
                dismiss();
            }
        });
    }



}
