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

import com.example.daoqimanagement.AddPrepareActivity;
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
    public void onInAppMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onInAppMessageArrived:");
        super.onInAppMessageArrived(context, notificationMessage);
    }

    @Override
    public void onInAppMessageClick(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onInAppMessageClick:");
        super.onInAppMessageClick(context, notificationMessage);
    }

    @Override
    public void onPullInAppResult(Context context, JPushMessage jPushMessage) {
        Log.d("jpushReceiver", "onPullInAppResult:");
        super.onPullInAppResult(context, jPushMessage);
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean b, int i) {
        Log.d("jpushReceiver", "onNotificationSettingsCheck:");
        super.onNotificationSettingsCheck(context, b, i);
    }

    @Override
    public boolean isNeedShowNotification(Context context, NotificationMessage notificationMessage, String s) {
        Log.d("jpushReceiver", "onNotificationSettingsCheck:");
        return super.isNeedShowNotification(context, notificationMessage, s);
    }

    @Override
    public boolean onSspNotificationWillShow(Context context, NotificationMessage notificationMessage, String s) {
        Log.d("jpushReceiver", "onSspNotificationWillShow:");
        return super.onSspNotificationWillShow(context, notificationMessage, s);
    }

    @Override
    public void onInAppMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onInAppMessageDismiss:");
        super.onInAppMessageDismiss(context, notificationMessage);
    }

    @Override
    public void onInAppMessageUnShow(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onInAppMessageUnShow:");
        super.onInAppMessageUnShow(context, notificationMessage);
    }

    @Override
    public boolean isNeedShowInAppMessage(Context context, NotificationMessage notificationMessage, String s) {
        Log.d("jpushReceiver", "isNeedShowInAppMessage:");
        return super.isNeedShowInAppMessage(context, notificationMessage, s);
    }



    @Override
    public void onNotifyMessageUnShow(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onNotifyMessageUnShow:");
        Log.d("jpushReceiver", String.valueOf(notificationMessage));

        super.onNotifyMessageUnShow(context, notificationMessage);
    }

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "getNotification");
        return super.getNotification(context, notificationMessage);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.d("jpushReceiver", "onMessage");
        super.onMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {

        try{
            //打开自定义的Activity
            Intent i = new Intent(context, UserInterFaceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,notificationMessage.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT,notificationMessage.notificationContent);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);

        }catch (Throwable throwable){

        }
//        Log.d("jpushReceiver", "onNotifyMessageOpened");
//
//
//
//        super.onNotifyMessageOpened(context, notificationMessage);
    }
    //接收到推送消息
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onNotifyMessageArrived");

        super.onNotifyMessageArrived(context, notificationMessage);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d("jpushReceiver", "onNotifyMessageDismiss");

        super.onNotifyMessageDismiss(context, notificationMessage);

    }

    @Override
    public void onRegister(Context context, String s) {
        Log.d("jpushReceiver", "onRegister");
        super.onRegister(context, s);
    }
    //连接极光
    @Override
    public void onConnected(Context context, boolean b) {
        Log.d("jpushReceiver", "onConnected");
        super.onConnected(context, b);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.d("jpushReceiver", "onCommandResult");
        super.onCommandResult(context, cmdMessage);
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.d("jpushReceiver", "onMultiActionClicked");
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
