package com.example.daoqimanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.HospitalDetailContactAdapter;
import com.example.daoqimanagement.adapter.HospitalDetailCooperateAdapter;
import com.example.daoqimanagement.bean.AddContactResponse;
import com.example.daoqimanagement.bean.HospitalDetailContactResponse;
import com.example.daoqimanagement.bean.HospitalDetailResponse;
import com.example.daoqimanagement.dialog.AddContactDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.fragment.MentorsFragment;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HospitalDetailActivity extends AppCompatActivity {
    int hospitalId;
    private RelativeLayout mRlFinish;
    private ImageView mIvHeadPic;
    private TextView mTvHospitalName,mTvNature,mTvLevel,mTvAreaName,mTvDetail,mTvAddContact;
    private RecyclerView mRcCooperateList, mRcContactList;
    HospitalDetailCooperateAdapter cooperateAdapter;
    HospitalDetailContactAdapter contactAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    String phone;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_hospital_detail);
        Intent intent = getIntent();
        hospitalId = intent.getIntExtra("hospitalId",0);
        initView();

        getHospitalDetailRes(Api.URL+"/v1/hospital/detail?hospitalId="+String.valueOf(hospitalId));
        getHospitalDetailContactRes(Api.URL+"/v1/hospital/detail?hospitalId="+String.valueOf(hospitalId));
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(HospitalDetailActivity.this);
                finish();
            }
        });

        mTvAddContact.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                AddContactDialog addContactDialog = new AddContactDialog(HospitalDetailActivity.this,R.style.CustomDialog);
                addContactDialog.setConfirm("提交", new AddContactDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(AddContactDialog dialog, String name, String mobile, String department, String position) {
                        postResContactAdd(Api.URL+"/v1/hospital/addContact",name,mobile,department,position,hospitalId);
                    }
                }).show();
            }
        });
    }

    public void initView(){
        mRlFinish = findViewById(R.id.hospital_detail_rl_finish);
        mIvHeadPic = findViewById(R.id.hospital_detail_iv_headpic);
        mTvHospitalName = findViewById(R.id.hospital_detail_tv_hospitalName);
        mTvNature = findViewById(R.id.hospital_detail_tv_nature);
        mTvLevel = findViewById(R.id.hospital_detail_tv_level);
        mTvAreaName = findViewById(R.id.hospital_detail_tv_areaName);
        mTvDetail = findViewById(R.id.hospital_detail_tv_detail);
        mRcCooperateList = findViewById(R.id.hospital_detail_rc_cooperate);
        mRcContactList = findViewById(R.id.hospital_detail_rc_contact);
        mTvAddContact = findViewById(R.id.hospital_detail_tv_addcontact);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(phone);
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "拨号权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    protected void postResContactAdd(String url, String name, String mobile, String department, String position, int hospitalid ) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("mobile",mobile);
            jsonObject.put("department",department);
            jsonObject.put("position",position);
            jsonObject.put("hospitalid",hospitalid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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

                        ToastUtils.showTextToast2(HospitalDetailActivity.this, "网络请求失败");
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
                        Log.d("res", res);
                        Gson gson = new Gson();
                        AddContactResponse addContactResponse = gson.fromJson(res,AddContactResponse.class);
                        if (addContactResponse.getCode() == 0){
                            getHospitalDetailContactRes(Api.URL+"/v1/hospital/detail?hospitalId="+String.valueOf(hospitalId));

                        } else if (addContactResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(HospitalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(HospitalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(HospitalDetailActivity.this,addContactResponse.getMsg());
                        }

                    }
                });


            }
        });


    }
    private void getHospitalDetailRes(String url) {



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
                        ToastUtils.showTextToast2(HospitalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("resf3q4d", res);

                Gson gson = new Gson();
                final HospitalDetailResponse hospitalDetailResponse = gson.fromJson(res,HospitalDetailResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (hospitalDetailResponse.getCode() == 0) {
                            mTvHospitalName.setText(hospitalDetailResponse.getData().getHospitalName());
                            mTvNature.setText(hospitalDetailResponse.getData().getNature());
                            mTvLevel.setText(hospitalDetailResponse.getData().getLevel());
                            mTvAreaName.setText(hospitalDetailResponse.getData().getAreaName());
                            mTvDetail.setText(hospitalDetailResponse.getData().getDetail());
                            /**
                             * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
                             */
                            Glide.with(HospitalDetailActivity.this).load(Api.URL+hospitalDetailResponse.getData().getHeadPic())
                                    .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                                    .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                                    .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                                    .into(mIvHeadPic);

                            cooperateAdapter = new HospitalDetailCooperateAdapter(HospitalDetailActivity.this,hospitalDetailResponse.getData().getCooperate());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalDetailActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };

                            mRcCooperateList.setLayoutManager(linearLayoutManager);
                            mRcCooperateList.setAdapter(cooperateAdapter);
//


                        } else if (hospitalDetailResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(HospitalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(HospitalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = hospitalDetailResponse.getMsg();
                            ToastUtils.showTextToast2(HospitalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    private void getHospitalDetailContactRes(String url) {



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
                        ToastUtils.showTextToast2(HospitalDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                final HospitalDetailContactResponse hospitalDetailContactResponse = gson.fromJson(res,HospitalDetailContactResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (hospitalDetailContactResponse.getCode() == 0) {


                          contactAdapter = new HospitalDetailContactAdapter(HospitalDetailActivity.this,hospitalDetailContactResponse.getData().getContact());
                          LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalDetailActivity.this){
                              @Override
                              public boolean canScrollVertically() {
                                  return false;
                              }
                          };

                           mRcContactList.setLayoutManager(linearLayoutManager);
                           mRcContactList.setAdapter(contactAdapter);
                            contactAdapter.setOnItemClickItemListener(new HospitalDetailContactAdapter.OnItemClickItemListener() {
                                @Override
                                public void onItemClickItem(String mobile) {
                                    phone = mobile;
                                    Log.d("mobile", mobile);
                                    if (ContextCompat.checkSelfPermission(HospitalDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】没有授权，申请权限");
                                        ActivityCompat.requestPermissions(HospitalDetailActivity.this,new String[]{Manifest.permission.CALL_PHONE}, 1);

                                    } else {
                                        Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】有权限");
                                        call(mobile);
                                    }
                                }
                            });
//


                        } else if (hospitalDetailContactResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(HospitalDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(HospitalDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = hospitalDetailContactResponse.getMsg();
                            ToastUtils.showTextToast2(HospitalDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    public void call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}