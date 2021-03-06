package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.dialog.DeleteCacheDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ClearCacheUtils;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.example.daoqimanagement.utils.osHelperUtils;

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
//                goToSetting();
//                gotoNotificationSetting();

               if (osHelperUtils.getOsType(getApplicationContext(),"ro.build.version.emui").length() > 0){
                   gotoNotificationSetting();
               }else {
                   goToSetting();
               }

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
                deleteCacheDialog.setConfirm("??????", new DeleteCacheDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(DeleteCacheDialog dialog) {
//                        ClearCacheUtils.cleanFiles(getApplicationContext());
                        ClearCacheUtils.cleanExternalCache(getApplicationContext());
                        ClearCacheUtils.cleanCustomCache(getExternalFilesDir(null)+"/download");
                        deleteCacheDialog.dismiss();
                        spotDialog = new SpotsDialog.Builder().setContext(FitActivity.this).setTheme(R.style.SpotDialogCustom)
                                .setCancelable(false).setMessage("?????????????????????...").build();
                        spotDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                /**
                                 * ?????????????????????
                                 */
                                spotDialog.dismiss();
                                ToastUtils.showTextToast2(FitActivity.this, "????????????");


                            }
                        },1500); // ??????1.5???

                    }
                }).show();

            }
        });
    }


    private void goToSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {// android 8.0??????
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
            intent.putExtra("android.provider.extra.EXTRA_CHANNEL_ID", "??????");
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {//??????
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //?????????????????????
    public void gotoNotificationSetting() {
        try {
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            //????????????????????? API 26, ???8.0??????8.0??????????????????
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "??????");
            //????????????????????? API21??????25?????? 5.0??????7.1 ???????????????????????????
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // ??????????????????????????????????????????
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