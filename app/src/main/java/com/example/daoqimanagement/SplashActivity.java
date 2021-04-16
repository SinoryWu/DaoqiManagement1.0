package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.view.CoolImageView;
import com.facebook.drawee.view.SimpleDraweeView;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActivityCollector.addActivity(this);
        imageView   = findViewById(R.id.splash_image);

        addAnimation();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(800);//使程序休眠一秒
                    Intent intent = new Intent(SplashActivity.this,UserInterFaceActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();



    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.d("splashactivity1", "onPause: ");
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    private void addAnimation(){
        imageView.startAnimation(scaleAnimation());
        // img_show.clearAnimation(); //停止动画
    }
    /**
     * 图片自动循环缩放
     * @return
     */
    public static ScaleAnimation scaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(1200);                        //动画执行时间
        scaleAnimation.setRepeatCount(-1);                    //-1表示重复执行动画
        scaleAnimation.setRepeatMode(Animation.REVERSE);    //重复 缩小和放大效果
        return scaleAnimation;

    }





}