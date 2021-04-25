package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.dialog.DeleteCacheDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ClearCacheUtils;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;

import dmax.dialog.SpotsDialog;

public class FitActivity extends AppCompatActivity {

    private TextView mTvDeleteCache,mTvToast;
    private RelativeLayout mRlFinish;
    AlertDialog spotDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit);
        ActivityCollector.addActivity(this);

        mRlFinish = findViewById(R.id.fit_rl_finish);
        mTvDeleteCache =  findViewById(R.id.fit_tv_delete_cache);
        mTvToast = findViewById(R.id.fit_tv_toast);
        mTvToast.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {

                gotoNotificationSetting();

            }
        });
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(FitActivity.this);
                finish();
            }
        });

        mTvDeleteCache.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                final DeleteCacheDialog deleteCacheDialog = new DeleteCacheDialog(FitActivity.this,R.style.CustomDialog);
                deleteCacheDialog.setConfirm("是的", new DeleteCacheDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(DeleteCacheDialog dialog) {
//                        ClearCacheUtils.cleanFiles(getApplicationContext());
                        ClearCacheUtils.cleanExternalCache(getApplicationContext());
                        ClearCacheUtils.cleanCustomCache(getExternalFilesDir(null)+"/download");
                        deleteCacheDialog.dismiss();
                        spotDialog = new SpotsDialog.Builder().setContext(FitActivity.this).setTheme(R.style.SpotDialogCustom)
                                .setCancelable(false).setMessage("清除中，请稍后...").build();
                        spotDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                /**
                                 * 延时执行的代码
                                 */
                                spotDialog.dismiss();
                                ToastUtils.showTextToast2(FitActivity.this, "清除完成");


                            }
                        },1500); // 延时1.5秒

                    }
                }).show();

            }
        });
    }


    //跳转到通知管理
    public void gotoNotificationSetting() {
        try {
            // 根据通知栏开启权限判断结果，判断是否需要提醒用户跳转系统通知管理页面
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "普通");
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}