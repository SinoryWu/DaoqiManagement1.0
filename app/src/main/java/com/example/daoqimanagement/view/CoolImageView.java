package com.example.daoqimanagement.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;


import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class CoolImageView extends ImageView {
    private int mLeft = 0;
    private int mTop = 0;
    private Handler mHandler;
    private Bitmap bitmap;
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();
    private int imgWidth;
    private int imgHeight;
    private boolean flag;
    private boolean istart;

    public CoolImageView(Context context) {
        super(context);
    }

    public CoolImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context, attrs);
    }

    public CoolImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context, attrs);
    }


    private void setUp(Context context, AttributeSet attrs) {
        mHandler = new MoveHandler();
        mHandler.sendEmptyMessageDelayed(1, 220L);
        istart = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        //获取图片资源
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        bitmap = drawable.getBitmap();
        dstRect.left = 0;
        dstRect.top = 0;
        dstRect.right = width;
        dstRect.bottom = height;
        if (bitmap != null) {
            if (istart) {
//        获取图片的宽高
                imgWidth = bitmap.getWidth();
                imgHeight = bitmap.getHeight();
                srcRect.left = 0 + mLeft;
                srcRect.right = imgWidth - mLeft;
                srcRect.top = 0 + mTop;
                srcRect.bottom = imgHeight - mTop;
                canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            } else {
                canvas.drawBitmap(bitmap, null, dstRect, null);
            }
        }

    }

    private class MoveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (imgHeight != 0) {
                        if (mTop == 0) {
                            mTop += 5;
                            mLeft += 5;
                        } else if (mTop == 120) {
                            mTop -= 5;
                            mLeft -= 5;
                        }
                    }
                    postInvalidate();
                    mHandler.sendEmptyMessageDelayed(1, 250);
                    break;
            }
        }
    }

    public void start() {
        mTop = 0;
        mLeft = 0;
        istart = true;
        mHandler.sendEmptyMessageDelayed(1, 220L);
    }

    public void stop() {
        istart = false;
    }
}
