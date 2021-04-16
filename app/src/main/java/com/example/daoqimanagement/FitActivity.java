package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.dialog.DeleteCacheDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ClearCacheUtils;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;

import dmax.dialog.SpotsDialog;

public class FitActivity extends AppCompatActivity {

    private TextView mTvDeleteCache;
    private RelativeLayout mRlFinish;
    AlertDialog spotDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit);
        ActivityCollector.addActivity(this);

        mRlFinish = findViewById(R.id.fit_rl_finish);
        mTvDeleteCache =  findViewById(R.id.fit_tv_delete_cache);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}