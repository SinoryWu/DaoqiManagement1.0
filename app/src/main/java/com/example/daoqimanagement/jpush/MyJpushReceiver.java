package com.example.daoqimanagement.jpush;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.UserInterFaceActivity;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJpushReceiver  extends JPushMessageReceiver {

    public MyJpushReceiver() {
        super();


    }



    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Log.d("asdas232ds", "getNotification");
        return super.getNotification(context, notificationMessage);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.d("asdas232ds", "onMessage");
        super.onMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        Log.d("asdas232ds", "onNotifyMessageOpened");



        super.onNotifyMessageOpened(context, notificationMessage);
    }
    //接收到推送消息
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d("asdas232ds", "onNotifyMessageArrived");
        super.onNotifyMessageArrived(context, notificationMessage);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d("asdas232ds", "onNotifyMessageDismiss");

        super.onNotifyMessageDismiss(context, notificationMessage);
        SharedPreferences sp = context.getSharedPreferences("jpush", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("jpush", "1");
        editor.apply();
    }

    @Override
    public void onRegister(Context context, String s) {
        Log.d("asdas232ds", "onRegister");
        super.onRegister(context, s);
    }
    //连接极光
    @Override
    public void onConnected(Context context, boolean b) {
        Log.d("asdas232ds", "onConnected");
        super.onConnected(context, b);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.d("asdas232ds", "onCommandResult");
        super.onCommandResult(context, cmdMessage);
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.d("asdas232ds", "onMultiActionClicked");
        super.onMultiActionClicked(context, intent);

    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("asdas232ds", "onTagOperatorResult");
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("asdas232ds", "onCheckTagOperatorResult");
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("asdas232ds", "onAliasOperatorResult");
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d("asdas232ds", "onMobileNumberOperatorResult");
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }









}
