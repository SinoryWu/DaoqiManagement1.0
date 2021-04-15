package com.example.daoqimanagement.utils;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.hawk.Hawk;

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

    }


}
