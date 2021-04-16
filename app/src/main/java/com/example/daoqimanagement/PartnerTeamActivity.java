package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.PartnerTeamAdapter;
import com.example.daoqimanagement.bean.EditPartnerResponse;
import com.example.daoqimanagement.bean.PartnerTeamResponse;
import com.example.daoqimanagement.dialog.DeletePartnerDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PartnerTeamActivity extends AppCompatActivity {

    private RelativeLayout mRlFinish,mRlSearch;
    private CircleImageView mIvHeadPic;
    private TextView mTvName;
    String headPic,trueName, uid;
    int uid1;
    private RecyclerView mRcTeamList;
    PartnerTeamAdapter partnerTeamAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    String delete = "no";
    private static final int REQUEST_CODE = 0x00000013;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_partner_team);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        headPic = intent.getStringExtra("headPic");
        trueName = intent.getStringExtra("trueName");
        uid1 = Integer.parseInt(uid);
        mIvHeadPic = findViewById(R.id.partner_team_iv_head_pic);
        mRlFinish = findViewById(R.id.partner_team_rl_finish);
        mRlSearch = findViewById(R.id.partner_team_rl_search);
        mTvName = findViewById(R.id.partner_team_tv_trueName);
        mRcTeamList = findViewById(R.id.partner_team_rc_team_list);




        /**
         * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
         */
        Glide.with(PartnerTeamActivity.this).load(Api.URL+headPic)
                .error(R.mipmap.partner_detail_head_icon)//异常时候显示的图片
                .fallback(R.mipmap.partner_detail_head_icon)//url为空的时候,显示的图片
                .placeholder(R.mipmap.partner_detail_head_icon)//加载成功前显示的图片
                .into(mIvHeadPic);
        mTvName.setText(trueName);
        getPartnerTeamListRes(Api.URL+"/v1/team/listByUid?uid="+ uid);

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(PartnerTeamActivity.this);
                finish();
            }
        });

        mRlSearch.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                Intent intent = new Intent(PartnerTeamActivity.this,SearchPartnerActivity.class);
                intent.putExtra("uid",uid);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    int teamUid = data.getIntExtra("uid",0);
//                    Log.d("uid1231312", uidte);
//                    Log.d("uid1231312", String.valueOf(uid1));
//                    Log.d("uid1231312", String.valueOf(uid));
                    postResPartnerAdd(Api.URL+"/v1/team/edit",uid1,1, teamUid);

                }
        }
    }

    private void getPartnerTeamListRes(String url) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

//        JSONObject jsonObject = new JSONObject();
//        String jsonStr = jsonObject.toString();
//
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

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
                        ToastUtils.showTextToast2(PartnerTeamActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);



                final PartnerTeamResponse partnerTeamResponse = new Gson().fromJson(res,PartnerTeamResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (partnerTeamResponse.getCode() == 0) {
                            partnerTeamAdapter = new PartnerTeamAdapter(PartnerTeamActivity.this,partnerTeamResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PartnerTeamActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            mRcTeamList.setLayoutManager(linearLayoutManager);
                            mRcTeamList.setAdapter(partnerTeamAdapter);
                            partnerTeamAdapter.setOnItemClickListener(new PartnerTeamAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(final int uid) {
                                     DeletePartnerDialog deletePartnerDialog = new DeletePartnerDialog(PartnerTeamActivity.this,R.style.CustomDialog);
                                    deletePartnerDialog.setConfirm("是的", new DeletePartnerDialog.IOnConfirmListener() {
                                        @Override
                                        public void onConfirm(DeletePartnerDialog dialog) {

                                            postResPartnerDelete(Api.URL+"/v1/team/edit",uid1,2, uid);





                                        }
                                    }).show();
                                }
                            });

                        } else if (partnerTeamResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PartnerTeamActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PartnerTeamActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = partnerTeamResponse.getMsg();
                            ToastUtils.showTextToast2(PartnerTeamActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    private void getPartnerTeamListRes2(String url) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

//        JSONObject jsonObject = new JSONObject();
//        String jsonStr = jsonObject.toString();
//
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

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
                        ToastUtils.showTextToast2(PartnerTeamActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);



                final PartnerTeamResponse partnerTeamResponse = new Gson().fromJson(res,PartnerTeamResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (partnerTeamResponse.getCode() == 0) {
                            partnerTeamAdapter = new PartnerTeamAdapter(PartnerTeamActivity.this,partnerTeamResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PartnerTeamActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            mRcTeamList.setLayoutManager(linearLayoutManager);
                            mRcTeamList.setAdapter(partnerTeamAdapter);
                            partnerTeamAdapter.setOnItemClickListener(new PartnerTeamAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(final int uid) {
                                     DeletePartnerDialog deletePartnerDialog = new DeletePartnerDialog(PartnerTeamActivity.this,R.style.CustomDialog);
                                    deletePartnerDialog.setConfirm("是的", new DeletePartnerDialog.IOnConfirmListener() {
                                        @Override
                                        public void onConfirm(DeletePartnerDialog dialog) {
                                            partnerTeamAdapter.notifyDataSetChanged();
                                            postResPartnerDelete(Api.URL+"/v1/team/edit", uid,2,uid1);

                                            getPartnerTeamListRes(Api.URL+"/v1/team/listByUid?uid="+ uid1);


                                        }
                                    }).show();
                                }
                            });

                        } else if (partnerTeamResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PartnerTeamActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PartnerTeamActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = partnerTeamResponse.getMsg();
                            ToastUtils.showTextToast2(PartnerTeamActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    protected void postResPartnerDelete(String url,  int uid, int type, int teamUid ) {
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
            jsonObject.put("uid",uid);
            jsonObject.put("type",type);
            jsonObject.put("teamUid",teamUid);
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

                        ToastUtils.showTextToast2(PartnerTeamActivity.this, "网络请求失败");
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
                        EditPartnerResponse editPartnerResponse = gson.fromJson(res,EditPartnerResponse.class);
                        if (editPartnerResponse.getCode() == 0){
                            ToastUtils.showTextToast2(PartnerTeamActivity.this,"操作成功");
                            getPartnerTeamListRes(Api.URL+"/v1/team/listByUid?uid="+ uid1);

                        } else if (editPartnerResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PartnerTeamActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PartnerTeamActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            ToastUtils.showTextToast2(PartnerTeamActivity.this,editPartnerResponse.getMsg());
                        }

                    }
                });


            }
        });


    }

    protected void postResPartnerAdd(String url, final int uid, int type, int teamUid ) {
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
            jsonObject.put("uid",uid);
            jsonObject.put("type",type);
            jsonObject.put("teamUid",teamUid);
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

                        ToastUtils.showTextToast2(PartnerTeamActivity.this, "网络请求失败");
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
                        EditPartnerResponse editPartnerResponse = gson.fromJson(res,EditPartnerResponse.class);
                        if (editPartnerResponse.getCode() == 0){
                            getPartnerTeamListRes(Api.URL+"/v1/team/listByUid?uid="+ uid);
                            ToastUtils.showTextToast2(PartnerTeamActivity.this,"操作成功");

                        } else if (editPartnerResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PartnerTeamActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PartnerTeamActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(PartnerTeamActivity.this,editPartnerResponse.getMsg());
                        }

                    }
                });


            }
        });


    }



}