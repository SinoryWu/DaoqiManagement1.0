package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.ApprovalLogAdapter;
import com.example.daoqimanagement.adapter.HospitalDetailCooperateAdapter;
import com.example.daoqimanagement.adapter.ScheduleListAdapter;
import com.example.daoqimanagement.bean.ApprovalLogResponse;
import com.example.daoqimanagement.bean.HospitalDetailResponse;
import com.example.daoqimanagement.bean.PrepareDetailForUserResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApprovalLogActivity extends AppCompatActivity {

    private RelativeLayout mRlFinish;
    private RecyclerView mRcLogList;
    ApprovalLogAdapter approvalLogAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_approval_log);
        mRlFinish = findViewById(R.id.approval_log_rl_finish);
        mRcLogList = findViewById(R.id.approval_log_rc_log_list);
        Intent intent = getIntent();
        String prepareId = intent.getStringExtra("prepareId");

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(ApprovalLogActivity.this);
                finish();

            }
        });
        getProgressLogRes(Api.URL+"/v1/approval/getApprovalLogList?prepareId="+prepareId);

    }

    private void getProgressLogRes(String url) {



        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody


        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid", GetSharePerfenceSP.getUid(this))
                .get()
                .build();
        //3.将request封装为call
        Call call =  Api.ok().newCall(request);
        L.e(String.valueOf(call));
        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(ApprovalLogActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                final ApprovalLogResponse approvalLogResponse = gson.fromJson(res,ApprovalLogResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (approvalLogResponse.getCode() == 0) {
                           approvalLogAdapter = new ApprovalLogAdapter(ApprovalLogActivity.this,approvalLogResponse.getData());
                           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ApprovalLogActivity.this);
                           mRcLogList.setLayoutManager(linearLayoutManager);
                           mRcLogList.setAdapter(approvalLogAdapter);


                        } else if (approvalLogResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalLogActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ApprovalLogActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = approvalLogResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalLogActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

}