package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.adapter.ScheduleListAdapter;
import com.example.daoqimanagement.bean.ApplyFormalResponse;
import com.example.daoqimanagement.bean.ChangeStatusResponse;
import com.example.daoqimanagement.bean.DelayApplyResponse;
import com.example.daoqimanagement.bean.LoginDataResponse;
import com.example.daoqimanagement.bean.LoginResponse;
import com.example.daoqimanagement.bean.PrepareDetailForUserResponse;
import com.example.daoqimanagement.dialog.DelayApplyDialog;
import com.example.daoqimanagement.dialog.DelayProtectDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.dialog.FormalReportDialog;
import com.example.daoqimanagement.dialog.ModifyStatusDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrepareDetailForUserActivity extends AppCompatActivity {
    DialogTokenIntent dialogTokenIntent =null;
    private TextView mTvHospitalName,mTvLevel,mTvAreaName,mTvNature,mTvProtectTime,mTvProductName,mTvDepartment,mTvReason
            ,mTvDelay, mTvSubmitPayJoin,mTvSubmitPayDelay, mTvApplyFormal,mTvProtect,mTvStatus,mTvHospital;
    ScheduleListAdapter scheduleListAdapter;
    private RecyclerView mRcScheduleList;
    private RelativeLayout mRlFinish,mRlProtect,mRlStatus,mRlHospital,mRlUpdateProgress,mRlHospitalDetail;

    public static final int PAYMENT_REQUEST = 0x00000015;

    String type;
    int hospitalId ;
    String prepareid,productId;
    int payProtect = 0;
    String payAmount = "";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        saveStringToSp("refresh","refreshHospitalList");
        getScheduleListRes(Api.URL+"/v1/prepare/detail?prepareId="+prepareid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        String hospitalName = intent.getStringExtra("hospitalName");
         prepareid = intent.getStringExtra("prepareId");


        setContentView(R.layout.activity_prepare_detail_for_user);
        mTvHospitalName = findViewById(R.id.prepare_detail_user_tv_hospitalName);
        mTvLevel = findViewById(R.id.prepare_detail_user_tv_level);
        mTvAreaName = findViewById(R.id.prepare_detail_user_tv_areaName);
        mTvNature = findViewById(R.id.prepare_detail_user_tv_nature);
        mRcScheduleList = findViewById(R.id.prepare_detail_user_rc_scheduleList);
        mTvProtectTime= findViewById(R.id.prepare_detail_user_tv_protectTime);
        mTvProductName= findViewById(R.id.prepare_detail_user_tv_productName);
        mTvDepartment= findViewById(R.id.prepare_detail_user_tv_department);
        mTvReason= findViewById(R.id.prepare_detail_user_tv_reason);
        mRlFinish = findViewById(R.id.prepare_detail_user_rl_finish);
        mTvDelay = findViewById(R.id.prepare_detail_user_tv_delay);
        mRlProtect = findViewById(R.id.prepare_detail_user_rl_protect);
        mRlStatus = findViewById(R.id.prepare_detail_user_rl_status);
        mRlHospital = findViewById(R.id.prepare_detail_user_rl_hospital);
        mRlUpdateProgress = findViewById(R.id.prepare_detail_user_rl_update_progress);
        mTvSubmitPayJoin = findViewById(R.id.prepare_detail_user_tv_submit_pay_join);
        mTvSubmitPayDelay = findViewById(R.id.prepare_detail_user_tv_submit_pay_delay);
        mTvApplyFormal = findViewById(R.id.prepare_detail_user_tv_apply_formal);
        mRlHospitalDetail = findViewById(R.id.prepare_detail_user_rl_hospitalDetail);
        mTvProtect= findViewById(R.id.prepare_detail_user_tv_protect);
        mTvStatus= findViewById(R.id.prepare_detail_user_tv_status);
        mTvHospital= findViewById(R.id.prepare_detail_user_tv_hospital);
        mTvDelay.setVisibility(View.GONE);
        mTvSubmitPayDelay.setVisibility(View.GONE);
        mTvSubmitPayJoin.setVisibility(View.GONE);
        mTvApplyFormal.setVisibility(View.GONE);
        mRlProtect.setVisibility(View.GONE);
        mRlStatus.setVisibility(View.GONE);
        mRlHospital.setVisibility(View.GONE);
        mTvProtect.setVisibility(View.GONE);
        mTvStatus.setVisibility(View.GONE);
        mTvHospital.setVisibility(View.GONE);
        mRlUpdateProgress.setVisibility(View.GONE);
        if (GetSharePerfenceSP.getUserType(this).equals("1")){
            mRlProtect.setVisibility(View.GONE);
            mRlStatus.setVisibility(View.GONE);
            mRlHospital.setVisibility(View.GONE);
            mTvProtect.setVisibility(View.GONE);
            mTvStatus.setVisibility(View.GONE);
            mTvHospital.setVisibility(View.GONE);
            mRlUpdateProgress.setVisibility(View.VISIBLE);

        }else if (GetSharePerfenceSP.getUserType(this).equals("2")){
            mRlProtect.setVisibility(View.VISIBLE);
            mRlStatus.setVisibility(View.VISIBLE);
            mRlHospital.setVisibility(View.VISIBLE);
            mTvProtect.setVisibility(View.VISIBLE);
            mTvStatus.setVisibility(View.VISIBLE);
            mTvHospital.setVisibility(View.VISIBLE);
            mRlUpdateProgress.setVisibility(View.GONE);


        }

        Log.d("preparedetail", GetSharePerfenceSP.getToken(PrepareDetailForUserActivity.this));
        Log.d("preparedetail", GetSharePerfenceSP.getUid(PrepareDetailForUserActivity.this));
        Log.d("preparedetail", prepareid);


        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(PrepareDetailForUserActivity.this);
                finish();
            }
        });






        mTvDelay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {

                DelayApplyDialog delayApplyDialog = new DelayApplyDialog(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                delayApplyDialog.setConfirm("提交", new DelayApplyDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(DelayApplyDialog dialog, String reason) {

                        postDelayApply(Api.URL+"/v1/delay/add", Integer.parseInt(prepareid),reason);
                    }
                }).show();
            }
        });

        mRlProtect.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                DelayProtectDialog delayProtectDialog = new DelayProtectDialog(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                delayProtectDialog.setConfirm("提交", new DelayProtectDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(DelayProtectDialog dialog, String month) {
//                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this,month);

                        if (month.equals("一个月")){
                            postDelayTime(Api.URL+"/v1/delay/addForAdmin",Integer.parseInt(prepareid),1);
                        }else if (month.equals("三个月")){
                            postDelayTime(Api.URL+"/v1/delay/addForAdmin",Integer.parseInt(prepareid),3);
                        }else if (month.equals("六个月")){
                            postDelayTime(Api.URL+"/v1/delay/addForAdmin",Integer.parseInt(prepareid),6);
                        }

                    }
                }).show();
            }
        });

        mRlStatus.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                ModifyStatusDialog modifyStatusDialog = new ModifyStatusDialog(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                modifyStatusDialog.setConfirm("提交", new ModifyStatusDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(ModifyStatusDialog dialog, String status) {
//                        Log.d("hospitalid", String.valueOf(hospitalId));
                        if (status.equals("取消报备")){
//                            postChangeStatus(Api.URL+"/v1/prepare/edit",Integer.parseInt(prepareid),1);
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this,"该功能暂未开放");
                        }
                    }
                }).show();
            }
        });

        mTvApplyFormal.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                FormalReportDialog formalReportDialog = new FormalReportDialog(PrepareDetailForUserActivity.this, R.style.CustomDialog,payProtect,payAmount);
                formalReportDialog.setConfirm("是的", new FormalReportDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(FormalReportDialog dialog, String yesOrNo) {
                        if (yesOrNo.equals("是的")){
                            Log.d("hospitalId", String.valueOf(hospitalId));
                            applyFormalPost(String.valueOf(hospitalId),productId);

                        }
                    }
                }).show();


            }
        });

        mTvSubmitPayJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrepareDetailForUserActivity.this,PayEarnestMoneyActivity.class);
                intent.putExtra("hospitalId",hospitalId);
                intent.putExtra("productId",productId);
                intent.putExtra("type","join");
                startActivityForResult(intent,PAYMENT_REQUEST);
            }
        });

        mTvSubmitPayDelay.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                Intent intent = new Intent(PrepareDetailForUserActivity.this,PayEarnestMoneyActivity.class);
                intent.putExtra("hospitalId",hospitalId);
                intent.putExtra("type","delay");
                intent.putExtra("prepareId",prepareid);
                startActivityForResult(intent,PAYMENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PAYMENT_REQUEST:
                if (resultCode == RESULT_OK){

                    Log.d("asdjalksd", "onStart: ");
//                    getScheduleListRes(Api.URL+"/v1/prepare/detail?prepareId="+prepareid);
                }
                break;
        }
    }

    private void postDelayTime(String url, int prepareId, int delayTime) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prepareId",prepareId);
            jsonObject.put("delayTime",delayTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        Log.d("sada2eq", jsonStr);
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
                .post(requestBodyJson)
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Log.d("applyres", res);

                final DelayApplyResponse delayApplyResponse  = new Gson().fromJson(res,DelayApplyResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (delayApplyResponse.getCode() == 0) {


                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "操作成功");
//
                            getScheduleListRes(Api.URL+"/v1/prepare/detail?prepareId="+prepareid);
                            saveStringToSp("refresh","refreshHospitalList");
                        } else if (delayApplyResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PrepareDetailForUserActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = delayApplyResponse.getMsg();
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, msg);
                        }
                    }
                });


            }
        });

    }

    private void postChangeStatus(String url,  int prepareId, int status) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prepareId",prepareId);
            jsonObject.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
                .post(requestBodyJson)
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Log.d("applyres", res);

                final ChangeStatusResponse changeStatusResponse  = new Gson().fromJson(res,ChangeStatusResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (changeStatusResponse.getCode() == 0) {


                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "操作成功");
//
                            getScheduleListRes(Api.URL+"/v1/prepare/detail?prepareId="+prepareid);
                            saveStringToSp("refresh","refreshHospitalList");
                        } else if (changeStatusResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PrepareDetailForUserActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = changeStatusResponse.getMsg();
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void postDelayApply(String url,  int prepareId, String reason) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prepareId",prepareId);
            jsonObject.put("reason",reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
                .post(requestBodyJson)
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Log.d("applyres", res);

                final DelayApplyResponse delayApplyResponse  = new Gson().fromJson(res,DelayApplyResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (delayApplyResponse.getCode() == 0) {


                            Intent intent = new Intent(PrepareDetailForUserActivity.this,PayEarnestMoneyActivity.class);
                           intent.putExtra("type","delay");
                           intent.putExtra("hospitalId",hospitalId);
                            startActivityForResult(intent,PAYMENT_REQUEST);

//

                            getScheduleListRes(Api.URL+"/v1/prepare/detail?prepareId="+prepareid);
                            saveStringToSp("refresh","refreshHospitalList");

                        } else if (delayApplyResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PrepareDetailForUserActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = delayApplyResponse.getMsg();
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    private void getScheduleListRes(String url) {



        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
                .get()
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("TAGq23123eqweq", res);



                final PrepareDetailForUserResponse prepareDetailForUserResponse = new Gson().fromJson(res,PrepareDetailForUserResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (prepareDetailForUserResponse.getCode() == 0) {
                            hospitalId = prepareDetailForUserResponse.getData().getHospitalId();
                            payAmount = prepareDetailForUserResponse.getData().getPayAmount();
                            payProtect = prepareDetailForUserResponse.getData().getPayProtect();
                            productId = String.valueOf(prepareDetailForUserResponse.getData().getProductId());
                            if (prepareDetailForUserResponse.getData() != null){


                                if (prepareDetailForUserResponse.getData().getProductName() != null){
                                    mTvProductName.setText(prepareDetailForUserResponse.getData().getProductName());
                                }

                                if (prepareDetailForUserResponse.getData().getProductName() != null){
                                    mTvDepartment.setText(prepareDetailForUserResponse.getData().getDepartment());
                                }
                                if (prepareDetailForUserResponse.getData().getReason() != null){
                                    mTvReason.setText(prepareDetailForUserResponse.getData().getReason());
                                }


                                if (prepareDetailForUserResponse.getData().getScheduleList() != null){
                                    scheduleListAdapter = new ScheduleListAdapter(PrepareDetailForUserActivity.this,prepareDetailForUserResponse.getData().getScheduleList());
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PrepareDetailForUserActivity.this){
                                        @Override
                                        public boolean canScrollVertically() {
                                            return false;
                                        }
                                    };
                                    mRcScheduleList.setLayoutManager(linearLayoutManager);
                                    mRcScheduleList.setAdapter(scheduleListAdapter);

                                    scheduleListAdapter.setOnItemClickListener(new ScheduleListAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int scheduleId, int position) {
                                            String s = String.valueOf(scheduleId);
//                                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, String.valueOf(scheduleId));

                                            Intent intent = new Intent(PrepareDetailForUserActivity.this,ProgressDetailActivity.class);
                                            intent.putExtra("scheduleId",s);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                mTvHospitalName.setText(prepareDetailForUserResponse.getData().getHospitalName());
                                mTvAreaName.setText(prepareDetailForUserResponse.getData().getAreaName());
                                mTvLevel.setText(prepareDetailForUserResponse.getData().getLevel());
                                mTvNature.setText(prepareDetailForUserResponse.getData().getNature());

                                mRlHospital.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {

                                    }
                                });

                                mRlHospitalDetail.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        Intent intent = new Intent(PrepareDetailForUserActivity.this,HospitalDetailActivity.class);
                                        intent.putExtra("hospitalId",prepareDetailForUserResponse.getData().getHospitalId());
                                        Log.d("hosdetail", GetSharePerfenceSP.getToken(PrepareDetailForUserActivity.this));
                                        Log.d("hosdetail", GetSharePerfenceSP.getUid(PrepareDetailForUserActivity.this));
                                        Log.d("hosdetail", String.valueOf(prepareDetailForUserResponse.getData().getHospitalId()));
                                        startActivity(intent);
                                    }
                                });



                                if (prepareDetailForUserResponse.getData().getStatus()==90){

                                    if (prepareDetailForUserResponse.getData().getDelay() == 0){
                                        if (prepareDetailForUserResponse.getData().getProtectTime() != null){
                                            String createAt1 = prepareDetailForUserResponse.getData().getProtectTime();
                                            String createAt= createAt1.substring(0,11);
                                            StringBuffer buffer = new StringBuffer(createAt);
                                            buffer.replace(4,5,"年");
                                            buffer.replace(7,8,"月");
                                            buffer.replace(10,11,"日");


                                            mTvProtectTime.setText("保护期至："+buffer);
                                        }
                                        if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("1")){
                                            mTvDelay.setVisibility(View.VISIBLE);
                                            mTvSubmitPayJoin.setVisibility(View.GONE);
                                            mTvSubmitPayDelay.setVisibility(View.GONE);
                                            mTvApplyFormal.setVisibility(View.GONE);
                                        }else if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("2")){
                                            mTvDelay.setVisibility(View.GONE);
                                            mTvSubmitPayJoin.setVisibility(View.GONE);
                                            mTvSubmitPayDelay.setVisibility(View.GONE);
                                            mTvApplyFormal.setVisibility(View.GONE);
                                        }

                                    }else if (prepareDetailForUserResponse.getData().getDelay() == 1){
                                        mTvProtectTime.setText("待提交支付凭证");
                                        if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("1")){
                                            mTvDelay.setVisibility(View.GONE);
                                            mTvSubmitPayJoin.setVisibility(View.GONE);
                                            mTvSubmitPayDelay.setVisibility(View.VISIBLE);
                                            mTvApplyFormal.setVisibility(View.GONE);
                                        }else if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("2")){
                                            mTvDelay.setVisibility(View.GONE);
                                            mTvSubmitPayJoin.setVisibility(View.GONE);
                                            mTvSubmitPayDelay.setVisibility(View.GONE);
                                            mTvApplyFormal.setVisibility(View.GONE);
                                        }

                                    }else if (prepareDetailForUserResponse.getData().getDelay() == 2){
                                        mTvProtectTime.setText("待财务审核");

                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.GONE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.GONE);
                                    }else if (prepareDetailForUserResponse.getData().getDelay() == 3){
                                        mTvProtectTime.setText("待管理员审核");
                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.GONE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.GONE);
                                    }
                                }else if(prepareDetailForUserResponse.getData().getStatus()==0){
                                    mTvProtectTime.setText("已关闭");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvSubmitPayDelay.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                } else if(prepareDetailForUserResponse.getData().getStatus()==1){
                                    mTvProtectTime.setText("预报备");
                                    if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("1")){
                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.GONE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.VISIBLE);
                                    }else if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("2")){
                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.GONE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.GONE);
                                    }

                                }else if(prepareDetailForUserResponse.getData().getStatus()==10){
                                    mTvProtectTime.setText("待上传支付凭证");
                                    if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("1")){
                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.VISIBLE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.GONE);
                                    }else if (GetSharePerfenceSP.getUserType(PrepareDetailForUserActivity.this).equals("2")){
                                        mTvDelay.setVisibility(View.GONE);
                                        mTvSubmitPayJoin.setVisibility(View.GONE);
                                        mTvSubmitPayDelay.setVisibility(View.GONE);
                                        mTvApplyFormal.setVisibility(View.GONE);
                                    }

                                }else if (prepareDetailForUserResponse.getData().getStatus()==20){
                                    mTvProtectTime.setText("待财务审核");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }else if (prepareDetailForUserResponse.getData().getStatus()==30 || prepareDetailForUserResponse.getData().getStatus()==40 || prepareDetailForUserResponse.getData().getStatus()==50){
                                    mTvProtectTime.setText("待审核");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }else if (prepareDetailForUserResponse.getData().getStatus()==60){
                                    mTvProtectTime.setText("待退款");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }else if (prepareDetailForUserResponse.getData().getStatus()==70){
                                    mTvProtectTime.setText("已退款");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }else if (prepareDetailForUserResponse.getData().getStatus()==80){
                                    mTvProtectTime.setText("已取消");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }else if (prepareDetailForUserResponse.getData().getStatus()==95){
                                    mTvProtectTime.setText("开发完成");
                                    mTvDelay.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                    mTvApplyFormal.setVisibility(View.GONE);
                                    mTvSubmitPayJoin.setVisibility(View.GONE);
                                }

                            }

                            mRlUpdateProgress.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(PrepareDetailForUserActivity.this,UpDateProgressActivity.class);
                                    intent.putExtra("hospitalname",prepareDetailForUserResponse.getData().getHospitalName());
                                    intent.putExtra("prepareId",prepareid);
                                    startActivity(intent);
                                }
                            });

//


                        } else if (prepareDetailForUserResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PrepareDetailForUserActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PrepareDetailForUserActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = prepareDetailForUserResponse.getMsg();
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, msg);
                        }
                    }
                });

            }
        });


    }


    private void applyFormalPost(String hospitalid, String productid) {


        HashMap<String, String> map = new HashMap<>();
        map.put("hospitalid", hospitalid);
        map.put("productid", productid);//18158188052



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/prepare/applyFormal";
        postResApplyFormal(url, map);


    }

    protected void postResApplyFormal(String url, HashMap<String, String> map) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:", String.valueOf(params));
        Set<String> keys = map.keySet();
        for (String key : keys) {
            params.put(key, map.get(key));

        }
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
                .post(requestBodyJson)
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        ApplyFormalResponse applyFormalResponse = gson.fromJson(res, ApplyFormalResponse.class);

                        if (applyFormalResponse.getCode() == 0) {
                            Intent intent = new Intent(PrepareDetailForUserActivity.this,PayEarnestMoneyActivity.class);
                            intent.putExtra("hospitalId",hospitalId);
                            intent.putExtra("productId",productId);
                            intent.putExtra("type","join");
                            startActivityForResult(intent,PAYMENT_REQUEST);

                        } else if (applyFormalResponse.getCode() == 10001) {
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, "请先注册账户");
                        } else {
                            String msg = applyFormalResponse.getMsg();
                            ToastUtils.showTextToast2(PrepareDetailForUserActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    public String getTokenToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String token = sp.getString("token", "");
        return token;
    }

    public String getUidToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        return uid;
    }

    public String getUserTypeToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype", "");
        return userType;
    }

    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

}