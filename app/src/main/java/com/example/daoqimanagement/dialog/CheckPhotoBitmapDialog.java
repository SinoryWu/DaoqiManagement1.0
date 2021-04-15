package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.daoqimanagement.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CheckPhotoBitmapDialog extends Dialog {
    private PhotoView photoView;
    private Bitmap bitmap;
    public CheckPhotoBitmapDialog(@NonNull Context context) {
        super(context);
    }

    public CheckPhotoBitmapDialog(@NonNull Context context, int themeResId, Bitmap bitmap) {
        super(context, themeResId);
        this.bitmap = bitmap;
    }

    protected CheckPhotoBitmapDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_photo_uri_dialog);
        photoView = findViewById(R.id.check_photo_dialog_uri);
        photoView.setImageBitmap(bitmap);
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
