package com.example.daoqimanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.daoqimanagement.adapter.ScheduleListAdapter;
import com.example.daoqimanagement.bean.ApprovalDetailResponse;
import com.example.daoqimanagement.bean.ApprovalOpinionResponse;
import com.example.daoqimanagement.bean.DelayApplyResponse;
import com.example.daoqimanagement.bean.LoginDataResponse;
import com.example.daoqimanagement.bean.LoginResponse;
import com.example.daoqimanagement.bean.PrepareDetailForUserResponse;
import com.example.daoqimanagement.dialog.ApprovalOpinionDialog;
import com.example.daoqimanagement.dialog.CheckPhotoBitmapDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApprovalDetailActivity extends AppCompatActivity {

    private TextView mTvName,mTvCreateTime,mTvHospitalName,mTvType,mTvContent,mTvOpinion;
    private RelativeLayout mRlProgressLog,mRlFinish;
    private LinearLayout mLlButton;
    private ImageView mIvType;
    private Button mBtnPass,mBtnNoPass;
    private SimpleDraweeView mIvPicture;
    int approvalId;
    Bitmap bitmap;

    String opinionEt;
    DialogTokenIntent dialogTokenIntent  = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_approval_detail);
        mTvName = findViewById(R.id.approval_detail_tv_name);
        mTvCreateTime = findViewById(R.id.approval_detail_tv_createTime);
        mTvHospitalName = findViewById(R.id.approval_detail_tv_hospitalName);
        mTvType = findViewById(R.id.approval_detail_tv_type);
        mTvContent = findViewById(R.id.approval_detail_tv_content);
        mTvOpinion = findViewById(R.id.approval_detail_tv_opinion);
        mRlProgressLog = findViewById(R.id.approval_detail_rl_progress_log);
        mRlFinish = findViewById(R.id.approval_detail_rl_finish);
        mBtnPass = findViewById(R.id.approval_detail_btn_pass);
        mBtnNoPass = findViewById(R.id.approval_detail_btn_no_pass);
        mIvType= findViewById(R.id.approval_detail_iv_type);
        mIvPicture = findViewById(R.id.approval_detail_iv_picture);
        mLlButton  =findViewById(R.id.approval_detail_ll_btn);
        mRlProgressLog.setVisibility(View.GONE);
        mIvType.setVisibility(View.GONE);
        mLlButton.setVisibility(View.GONE);
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(ApprovalDetailActivity.this);
                finish();

            }
        });
        if (GetSharePerfenceSP.getUserType(this).equals("2")){
            mRlProgressLog.setVisibility(View.VISIBLE);
        }else if (GetSharePerfenceSP.getUserType(this).equals("1")){
            mRlProgressLog.setVisibility(View.GONE);
        }
        Intent intent = getIntent();

        approvalId = intent.getIntExtra("approvalId",0);

        getApprovalDetailRes(Api.URL+"/v1/approval/detail?approvalId="+String.valueOf(approvalId));


        Log.d("approvaldetail", String.valueOf(approvalId));
        Log.d("approvaldetail", GetSharePerfenceSP.getToken(this));
        Log.d("approvaldetail", GetSharePerfenceSP.getUid(this));





        mIvPicture.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(ApprovalDetailActivity.this,R.style.CustomDialogPhoto,bitmap);
                checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                checkPhotoBitmapDialog.show();
                checkPhotoBitmapDialog.setCancelable(true);
                if (checkPhotoBitmapDialog.isShowing()){
                    final Window window=getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(Color.parseColor("#000000"));
                    }
                }
                checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        final Window window=getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setStatusBarColor(Color.parseColor("#ffffff"));
                        }

                    }
                });


                Window dialogWindow = checkPhotoBitmapDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                dialogWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        });




    }


    private void getApprovalDetailRes(String url) {



        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid", GetSharePerfenceSP.getUid(this))
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
                        ToastUtils.showTextToast2(ApprovalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("TAGq23123eqweq", res);



                final ApprovalDetailResponse approvalDetailResponse = new Gson().fromJson(res,ApprovalDetailResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (approvalDetailResponse.getCode() == 0) {

                            if (approvalDetailResponse.getData() != null) {
                                if (!TextUtils.isEmpty(approvalDetailResponse.getData().getOpinion())) {
                                    mTvOpinion.setText(approvalDetailResponse.getData().getOpinion());
                                }

                                if (approvalDetailResponse.getData().getUserType() == 1) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "星火合伙人");
                                } else if (approvalDetailResponse.getData().getUserType() == 2) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "直营团队");
                                } else if (approvalDetailResponse.getData().getUserType() == 3) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "生态链合伙人");
                                } else if (approvalDetailResponse.getData().getUserType() == 4) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "财务");
                                } else if (approvalDetailResponse.getData().getUserType() == 5) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "初审");
                                } else if (approvalDetailResponse.getData().getUserType() == 6) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "复审");
                                } else if (approvalDetailResponse.getData().getUserType() == 7) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "终审");
                                } else if (approvalDetailResponse.getData().getUserType() == 8) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename() + "  " + "运维");
                                } else if (approvalDetailResponse.getData().getUserType() == 0) {
                                    mTvName.setText(approvalDetailResponse.getData().getTruename());
                                }

                                if (!TextUtils.isEmpty(approvalDetailResponse.getData().getCreatedAt())) {
                                    mTvCreateTime.setText(approvalDetailResponse.getData().getCreatedAt());
                                }

                                if (!TextUtils.isEmpty(approvalDetailResponse.getData().getHospitalName())) {
                                    mTvHospitalName.setText(approvalDetailResponse.getData().getHospitalName());
                                }

                                if (approvalDetailResponse.getData().getType() == 0) {
                                    mTvType.setText("");
                                } else if (approvalDetailResponse.getData().getType() == 1) {
                                    mTvType.setText("报备审批");
                                } else if (approvalDetailResponse.getData().getType() == 2) {
                                    if (approvalDetailResponse.getData().getProtectTime() != 0){
                                        mTvType.setText("延期审批"+approvalDetailResponse.getData().getProtectTime()+"个月");
                                    }else {
                                        mTvType.setText("延期审批");
                                    }

                                } else if (approvalDetailResponse.getData().getType() == 3) {
                                    mTvType.setText("支付加盟费审批");
                                } else if (approvalDetailResponse.getData().getType() == 4) {
                                    mTvType.setText("退款");
                                }

                                if (!TextUtils.isEmpty(approvalDetailResponse.getData().getReason())) {
                                    mTvContent.setText(approvalDetailResponse.getData().getReason());
                                }

                                if (approvalDetailResponse.getData().getStatus() == 1) {
                                    mIvType.setVisibility(View.GONE);
                                    mLlButton.setVisibility(View.VISIBLE);
                                } else if (approvalDetailResponse.getData().getStatus() == 2) {
                                    mIvType.setVisibility(View.VISIBLE);
                                    mIvType.setImageResource(R.mipmap.approval_pass_true_icon);
                                    mLlButton.setVisibility(View.GONE);
                                } else if (approvalDetailResponse.getData().getStatus() == 3) {
                                    mIvType.setVisibility(View.VISIBLE);
                                    mIvType.setImageResource(R.mipmap.approval_pass_false_icon);
                                    mLlButton.setVisibility(View.GONE);
                                }


                                Glide.with(ApprovalDetailActivity.this).asBitmap()
                                        .load(Api.URL+approvalDetailResponse.getData().getPath())
                                        .error(R.drawable.progress_detail_picture_background)//异常时候显示的图片
                                        .fallback(R.drawable.progress_detail_picture_background)//url为空的时候,显示的图片
                                        .placeholder(R.drawable.progress_detail_picture_background)//加载成功前显示的图片

                                        .listener(new RequestListener<Bitmap>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                mIvPicture.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        })
                                        .skipMemoryCache(true).into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        float width = resource.getWidth();
                                        float height=  resource.getHeight();
                                        float scale = height/width;
                                        int scaledW  = dip2px(ApprovalDetailActivity.this,346);
                                        int scaledH  = (int) (scaledW*scale);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(scaledW,scaledH);
                                        lp.gravity = Gravity.CENTER_HORIZONTAL;
                                        mIvPicture.setLayoutParams(lp);
                                        mIvPicture.setImageBitmap(resource);
                                        bitmap = resource;



                                    }
                                });


                                mRlProgressLog.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        String prepareId  = String.valueOf(approvalDetailResponse.getData().getPrepareid());
//                                        String prepareId  = String.valueOf("0");
                                        Intent intent = new Intent(ApprovalDetailActivity.this,ApprovalLogActivity.class);
                                        intent.putExtra("prepareId",prepareId);
                                        startActivity(intent);
                                    }
                                });

                                mBtnPass.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(final View view) {
                                        ApprovalOpinionDialog approvalOpinionDialog = new ApprovalOpinionDialog(ApprovalDetailActivity.this,R.style.CustomDialog);
                                        approvalOpinionDialog.setConfirm("提交", new ApprovalOpinionDialog.IOnConfirmListener() {
                                            @Override
                                            public void onConfirm(ApprovalOpinionDialog dialog, String opinion) {
                                                Log.d("opinion", opinion);
                                                Log.d("opinion", String.valueOf(approvalId));

                                                opinionEt = opinion;
                                                Log.d("opinion", opinionEt);
                                                if (approvalDetailResponse.getData().getType() ==1){
                                                    approvalVerifyPostPass( String.valueOf(approvalId),"2",opinion);
                                                }else if (approvalDetailResponse.getData().getType() == 2){
                                                    if (GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("4")){
//                                                        Log.d("approvaldetail", String.valueOf(approvalId));
//                                                        Log.d("approvaldetail", String.valueOf(opinion));
                                                        approvalPostAccountPass(String.valueOf(approvalId),"2",opinion);
                                                    }else if (GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("5")){
                                                        if (approvalDetailResponse.getData().getProtectTime() != 0){
                                                            postApprovalResponsible(Api.URL+"/v1/delay/responsible",approvalId,opinion,2,approvalDetailResponse.getData().getProtectTime());
                                                        }
                                                    }
                                                }else if (approvalDetailResponse.getData().getType() == 3){
                                                    postApprovalJoin(Api.URL + "/v1/approval/verifyFranchiseFee",approvalId,2,opinion);
                                                }else if (approvalDetailResponse.getData().getType() == 4){
                                                    approvalPostRefundPass(String.valueOf(approvalId),"2",opinion);
                                                }


                                            }
                                        }).show();
                                    }
                                });

                                mBtnNoPass.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        ApprovalOpinionDialog approvalOpinionDialog = new ApprovalOpinionDialog(ApprovalDetailActivity.this,R.style.CustomDialog);
                                        approvalOpinionDialog.setConfirm("提交", new ApprovalOpinionDialog.IOnConfirmListener() {
                                            @Override
                                            public void onConfirm(ApprovalOpinionDialog dialog, String opinion) {
                                                Log.d("opinion", opinion);
                                                Log.d("opinion", String.valueOf(approvalId));

                                                opinionEt = opinion;
                                                Log.d("opinion", opinionEt);
                                                if (approvalDetailResponse.getData().getType() ==1){
                                                    approvalVerifyPostNoPass( String.valueOf(approvalId),"3",opinion);
                                                }else if (approvalDetailResponse.getData().getType() == 2){
                                                    if (GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("4")){
                                                        approvalPostAccountNoPass(String.valueOf(approvalId),"3",opinion);
                                                    }else if (GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("5") || GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("6") || GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("7") || GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("8") || GetSharePerfenceSP.getType(ApprovalDetailActivity.this).equals("8")){
                                                        if (approvalDetailResponse.getData().getProtectTime() != 0){
                                                            postApprovalResponsible(Api.URL+"/v1/delay/responsible",approvalId,opinion,2,approvalDetailResponse.getData().getProtectTime());
                                                        }
                                                    }
                                                }else if (approvalDetailResponse.getData().getType() == 3){
                                                    postApprovalJoin(Api.URL + "/v1/approval/verifyFranchiseFee",approvalId,3,opinion);
                                                }else if (approvalDetailResponse.getData().getType() == 4){
                                                    approvalPostRefundNoPass(String.valueOf(approvalId),"3",opinion);
                                                }


                                            }
                                        }).show();
                                    }
                                });


//
                            }


                        } else if (approvalDetailResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(ApprovalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = approvalDetailResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });


    }


    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void postApprovalResponsible(String url,  int approvalId, String opinion,int status,int delayTime) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("approvalId",approvalId);
            jsonObject.put("opinion",opinion);
            jsonObject.put("status",status);
            jsonObject.put("delayTime",delayTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();
        Log.d("applyres", jsonStr);
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                        ToastUtils.showTextToast2(ApprovalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Log.d("applyres", res);

                final ApprovalOpinionResponse approvalOpinionResponse  = new Gson().fromJson(res,ApprovalOpinionResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (approvalOpinionResponse.getCode() == 0) {


                            ToastUtils.showTextToast2(ApprovalDetailActivity.this,"提交成功");
                            mTvOpinion.setText(opinionEt);
                            mLlButton.setVisibility(View.GONE);
                            mTvOpinion.setVisibility(View.VISIBLE);
                            mIvType.setVisibility(View.VISIBLE);
                            mIvType.setImageResource(R.mipmap.approval_pass_true_icon);
                            saveStringToSp("refresh","refreshApprovalList");

                        } else if (approvalOpinionResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ApprovalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();

                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = approvalOpinionResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void postApprovalJoin(String url,  int approvalId, int status,String opinion) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("approvalId",approvalId);
            jsonObject.put("status",status);
            jsonObject.put("opinion",opinion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                        ToastUtils.showTextToast2(ApprovalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Log.d("applyres", res);

                final ApprovalOpinionResponse approvalOpinionResponse  = new Gson().fromJson(res,ApprovalOpinionResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (approvalOpinionResponse.getCode() == 0) {


                            ToastUtils.showTextToast2(ApprovalDetailActivity.this,"提交成功");
                            mTvOpinion.setText(opinionEt);
                            mLlButton.setVisibility(View.GONE);
                            mTvOpinion.setVisibility(View.VISIBLE);
                            mIvType.setVisibility(View.VISIBLE);
                            mIvType.setImageResource(R.mipmap.approval_pass_true_icon);
                            saveStringToSp("refresh","refreshApprovalList");

                        } else if (approvalOpinionResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ApprovalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = approvalOpinionResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void approvalVerifyPostPass(String approvalId, String status, String opinion) {


        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/approval/verify";
        postResApprovalVerifyPass(url, map);


    }

    private void approvalPostAccountPass(String approvalId, String status, String opinion) {

        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/delay/accounting";

        postResApprovalVerifyPass(url, map);


    }

    private void approvalPostRefundPass(String approvalId, String status, String opinion) {

        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/approval/refund";
        postResApprovalVerifyPass(url, map);


    }

    protected void postResApprovalVerifyPass(String url, HashMap<String, String> map) {
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
                        ToastUtils.showTextToast2(ApprovalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);



                Gson gson = new Gson();

                final ApprovalOpinionResponse approvalOpinionResponse = gson.fromJson(res, ApprovalOpinionResponse.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);


                        if (approvalOpinionResponse.getCode() == 0) {

                            ToastUtils.showTextToast2(ApprovalDetailActivity.this,"提交成功");
                            mTvOpinion.setText(opinionEt);
                            mLlButton.setVisibility(View.GONE);
                            mTvOpinion.setVisibility(View.VISIBLE);
                            mIvType.setVisibility(View.VISIBLE);
                            mIvType.setImageResource(R.mipmap.approval_pass_true_icon);
                            saveStringToSp("refresh","refreshApprovalList");
                        } else if (approvalOpinionResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ApprovalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            String msg = approvalOpinionResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    private void approvalVerifyPostNoPass(String approvalId, String status, String opinion) {


        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/approval/verify";
        postResApprovalVerifyNoPass(url, map);


    }

    private void approvalPostAccountNoPass(String approvalId, String status, String opinion) {

        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/delay/accounting";
        postResApprovalVerifyNoPass(url, map);


    }

    private void approvalPostRefundNoPass(String approvalId, String status, String opinion) {

        HashMap<String, String> map = new HashMap<>();
        map.put("approvalId", approvalId);
        map.put("status", status);//18158188052
        map.put("opinion", opinion);//111



//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/approval/refund";
        postResApprovalVerifyNoPass(url, map);


    }

    protected void postResApprovalVerifyNoPass(String url, HashMap<String, String> map) {
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
                        ToastUtils.showTextToast2(ApprovalDetailActivity.this, "网络请求失败");
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

                        ApprovalOpinionResponse approvalOpinionResponse = gson.fromJson(res, ApprovalOpinionResponse.class);

                        if (approvalOpinionResponse.getCode() == 0) {

                            ToastUtils.showTextToast2(ApprovalDetailActivity.this,"提交成功");
                            mTvOpinion.setText(opinionEt);
                            mLlButton.setVisibility(View.GONE);
                            mTvOpinion.setVisibility(View.VISIBLE);
                            mIvType.setVisibility(View.VISIBLE);
                            mIvType.setImageResource(R.mipmap.approval_pass_false_icon);

                            saveStringToSp("refresh","refreshApprovalList");
                        } else if (approvalOpinionResponse.getCode() == 10001) {
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, "请先注册账户");
                        }  else if (approvalOpinionResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ApprovalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ApprovalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            String msg = approvalOpinionResponse.getMsg();
                            ToastUtils.showTextToast2(ApprovalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideSoftKeyboard(View view) {
        //这里获取view为参数 之前试过用context,LoginActivity.this会造成闪退
        //view不会闪退
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}