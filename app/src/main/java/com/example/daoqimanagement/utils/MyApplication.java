package com.example.daoqimanagement.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.multidex.MultiDex;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.UserInterFaceActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.hawk.Hawk;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Fresco.initialize(this);
        MultiDex.install(this);
        //Hawk存储初始化
        Hawk.init(this).build();
        // 不耗时，做一些简单初始化准备工作，不会启动下载进程
        FileDownloader.setupOnApplicationOnCreate(application);
        initJPush();

    }


    /**
     * 初始化极光推送
     */
    private void initJPush() {
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        //只需要在应用程序启动时调用一次该 API 即可
        JPushInterface.init(this);
    }


//    /**
//     * 申请定位、存储和通知栏的权限
//     *
//     * @param activity
//     */
//    public static void requestPermission(Activity activity) {
//        //打开通知栏的权限
//        if (JPushInterface.isNotificationEnabled(activity) == 0) {
//            new AlertDialog.Builder(activity)
//                    .setCancelable(false)
//                    .setMessage("通知权限未打开，是否前去打开？")
//                    .setPositiveButton("是", (d, w) -> JPushInterface.goToAppNotificationSettings(activity))
//                    .setNegativeButton("否", null)
//                    .show();
//        }
//        //申请定位、存储权限
//        JPushInterface.requestPermission(activity);
//    }


    private void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, UserInterFaceActivity.class);
            PendingIntent pi = PendingIntent.getActivities(this,0, new Intent[]{intent},0);
            if (nm != null){
                NotificationChannelGroup notificationChannelGroup = new NotificationChannelGroup("MyGroupId", "自定义通知组");
                nm.createNotificationChannelGroup(notificationChannelGroup);

                NotificationChannel notificationChannel = new NotificationChannel("channel_1", "自定义通知", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationChannel.setGroup("MyGroupId");
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
//                notificationChannel.setSound("android.resource://包名/raw/铃声文件", null);    //设置自定义铃声
                nm.createNotificationChannel(notificationChannel);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(),"channel_1")
                        .setContentTitle("通知标题")
                        .setContentText("通知内容")
//                            .setContentText("Learn how to build notification,send and sync data,and use voice actions.Get the official Android IDE and developer tools to build apps for Android")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.daoqi_icon)
//                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
//                            .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
//                            .setVibrate(new long[]{0,1000,1000,1000})
//                            .setLights(Color.GREEN,1000,1000)
//                            .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Learn how to build notification,send and sync data,and use voice actions.Get the official Android IDE and developer tools to build apps for Android"))
//                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                nm.notify(1, notification);
            }
        }
    }


}
