package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.daoqimanagement.adapter.HomeFragmentHospitalListAdapter;
import com.example.daoqimanagement.adapter.SearPrepareListAdapter;
import com.example.daoqimanagement.bean.HomeFragmentHospitalPrepareListResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPrepareListActivity extends AppCompatActivity {


    private RelativeLayout mRlFinish;
    private SearPrepareListAdapter searPrepareListAdapter;
    private PullLoadMoreRecyclerView mRcPrepareList;
    DialogTokenIntent dialogTokenIntent = null;
    HomeFragmentHospitalPrepareListResponse.DataBeanX hospitalPrepareListDataBeanX = new HomeFragmentHospitalPrepareListResponse.DataBeanX();
    List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> hospitalPrepareListDataBeans = new ArrayList<>();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_search_prepare_list);
        mRlFinish = findViewById(R.id.search_prepare_list_rl_finish);
        mRcPrepareList = findViewById(R.id.search_prepare_list_rc_list);

        getPrepareHospitalList(Api.URL+"/v1/prepare/listForUser?limit=20");

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(SearchPrepareListActivity.this);
                finish();
            }
        });

    }



    public void getPrepareHospitalList(String url){
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(SearchPrepareListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);

                final HomeFragmentHospitalPrepareListResponse homeFragmentHospitalPrepareListResponse = new HomeFragmentHospitalPrepareListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    homeFragmentHospitalPrepareListResponse.setCode(code);
                    homeFragmentHospitalPrepareListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentHospitalPrepareListResponse.setData(hospitalPrepareListDataBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from =  datax.optInt("from");
                        int last_page =  datax.optInt("last_page");
                        String last_page_url =  datax.optString("last_page_url");
                        String next_page_url =  datax.optString("next_page_url");
                        String path =  datax.optString("path");
                        int per_page =  datax.optInt("per_page");
                        String prev_page_url =  datax.optString("prev_page_url");
                        int  to =  datax.optInt("to");
                        int  total =  datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        hospitalPrepareListDataBeanX.setCurrent_page(current_page);
                        hospitalPrepareListDataBeanX.setFirst_page_url(first_page_url);
                        hospitalPrepareListDataBeanX.setFrom(from);
                        hospitalPrepareListDataBeanX.setLast_page(last_page);
                        hospitalPrepareListDataBeanX.setLast_page_url(last_page_url);
                        hospitalPrepareListDataBeanX.setNext_page_url(next_page_url);
                        hospitalPrepareListDataBeanX.setPath(path);
                        hospitalPrepareListDataBeanX.setPer_page(per_page);
                        hospitalPrepareListDataBeanX.setPrev_page_url(prev_page_url);
                        hospitalPrepareListDataBeanX.setTo(to);
                        hospitalPrepareListDataBeanX.setTotal(total);
                        hospitalPrepareListDataBeanX.setData(hospitalPrepareListDataBeans);

                        if (data !=null){


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = hospitalPrepareListDataBeanX.getCurrent_page();
                                    int lastPage = hospitalPrepareListDataBeanX.getLast_page();
                                    String protectTime = jsonObject1.optString("protectTime");
                                    int prepareId = jsonObject1.optInt("prepareId");
                                    int status = jsonObject1.optInt("status");
                                    String productName = jsonObject1.optString("productName");
                                    String hospitalName = jsonObject1.optString("hospitalName");
                                    String truename = jsonObject1.optString("truename");
                                    String hospitalHeadPic = jsonObject1.optString("hospitalHeadPic");


                                    HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean dataBean = new HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean();
                                    dataBean.setProtectTime(protectTime);
                                    dataBean.setPrepareId(prepareId);
                                    dataBean.setStatus(status);
                                    dataBean.setProductName(productName);
                                    dataBean.setHospitalName(hospitalName);
                                    dataBean.setTruename(truename);
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);

                                    dataBean.setHospitalHeadPic(hospitalHeadPic);
                                    hospitalPrepareListDataBeans.add(dataBean);

                                }

                            }
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){

                            searPrepareListAdapter = new SearPrepareListAdapter(SearchPrepareListActivity.this,hospitalPrepareListDataBeans);

                            mRcPrepareList.setLinearLayout();
//                            searPrepareListAdapter.addFooterView(LayoutInflater.from(SearchPrepareListActivity.this).inflate(R.layout.home_hopital_list_layout_footer,null));

                            mRcPrepareList.setAdapter(searPrepareListAdapter);


                            mRcPrepareList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {
                                    hospitalPrepareListDataBeans.clear();
                                    getPrepareHospitalListRefresh(Api.URL+"/v1/prepare/listForUser?limit=20");

                                }

                                @Override
                                public void onLoadMore() {
                                    int nextPage = hospitalPrepareListDataBeanX.getCurrent_page()+1;
                                    getPrepareHospitalListLoadMore(Api.URL+"/v1/prepare/listForUser?limit=20&page="+nextPage);

                                }
                            });
                            searPrepareListAdapter.setOnItemClickListener(new SearPrepareListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int prepareId, String hospitalName, int status, String hospitalHeadPic, int position) {
                                    Intent intent = new Intent();
                                    intent.putExtra("prepareId",prepareId);
                                    intent.putExtra("hospitalName",hospitalName);
                                    intent.putExtra("status",status);
                                    intent.putExtra("hospitalHeadPic",hospitalHeadPic);
                                    setResult(RESULT_OK,intent);
                                    ActivityCollector.removeActivity(SearchPrepareListActivity.this);
                                    finish();
                                }
                            });



                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchPrepareListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchPrepareListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(SearchPrepareListActivity.this,homeFragmentHospitalPrepareListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getPrepareHospitalListRefresh(String url){
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(SearchPrepareListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);

                final HomeFragmentHospitalPrepareListResponse homeFragmentHospitalPrepareListResponse = new HomeFragmentHospitalPrepareListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    homeFragmentHospitalPrepareListResponse.setCode(code);
                    homeFragmentHospitalPrepareListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentHospitalPrepareListResponse.setData(hospitalPrepareListDataBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from =  datax.optInt("from");
                        int last_page =  datax.optInt("last_page");
                        String last_page_url =  datax.optString("last_page_url");
                        String next_page_url =  datax.optString("next_page_url");
                        String path =  datax.optString("path");
                        int per_page =  datax.optInt("per_page");
                        String prev_page_url =  datax.optString("prev_page_url");
                        int  to =  datax.optInt("to");
                        int  total =  datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        hospitalPrepareListDataBeanX.setCurrent_page(current_page);
                        hospitalPrepareListDataBeanX.setFirst_page_url(first_page_url);
                        hospitalPrepareListDataBeanX.setFrom(from);
                        hospitalPrepareListDataBeanX.setLast_page(last_page);
                        hospitalPrepareListDataBeanX.setLast_page_url(last_page_url);
                        hospitalPrepareListDataBeanX.setNext_page_url(next_page_url);
                        hospitalPrepareListDataBeanX.setPath(path);
                        hospitalPrepareListDataBeanX.setPer_page(per_page);
                        hospitalPrepareListDataBeanX.setPrev_page_url(prev_page_url);
                        hospitalPrepareListDataBeanX.setTo(to);
                        hospitalPrepareListDataBeanX.setTotal(total);
                        hospitalPrepareListDataBeanX.setData(hospitalPrepareListDataBeans);

                        if (data !=null){


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = hospitalPrepareListDataBeanX.getCurrent_page();
                                    int lastPage = hospitalPrepareListDataBeanX.getLast_page();
                                    String protectTime = jsonObject1.optString("protectTime");
                                    int prepareId = jsonObject1.optInt("prepareId");
                                    int status = jsonObject1.optInt("status");
                                    String productName = jsonObject1.optString("productName");
                                    String hospitalName = jsonObject1.optString("hospitalName");
                                    String truename = jsonObject1.optString("truename");
                                    String hospitalHeadPic = jsonObject1.optString("hospitalHeadPic");

                                    HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean dataBean = new HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean();
                                    dataBean.setProtectTime(protectTime);
                                    dataBean.setPrepareId(prepareId);
                                    dataBean.setStatus(status);
                                    dataBean.setProductName(productName);
                                    dataBean.setHospitalName(hospitalName);
                                    dataBean.setTruename(truename);
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);

                                    dataBean.setHospitalHeadPic(hospitalHeadPic);
                                    hospitalPrepareListDataBeans.add(dataBean);

                                }

                            }
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){

                            searPrepareListAdapter.notifyDataSetChanged();
                            mRcPrepareList.setPullLoadMoreCompleted();




                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchPrepareListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchPrepareListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(SearchPrepareListActivity.this,homeFragmentHospitalPrepareListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getPrepareHospitalListLoadMore(String url){
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(SearchPrepareListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);

                final HomeFragmentHospitalPrepareListResponse homeFragmentHospitalPrepareListResponse = new HomeFragmentHospitalPrepareListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    homeFragmentHospitalPrepareListResponse.setCode(code);
                    homeFragmentHospitalPrepareListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentHospitalPrepareListResponse.setData(hospitalPrepareListDataBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from =  datax.optInt("from");
                        int last_page =  datax.optInt("last_page");
                        String last_page_url =  datax.optString("last_page_url");
                        String next_page_url =  datax.optString("next_page_url");
                        String path =  datax.optString("path");
                        int per_page =  datax.optInt("per_page");
                        String prev_page_url =  datax.optString("prev_page_url");
                        int  to =  datax.optInt("to");
                        int  total =  datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        hospitalPrepareListDataBeanX.setCurrent_page(current_page);
                        hospitalPrepareListDataBeanX.setFirst_page_url(first_page_url);
                        hospitalPrepareListDataBeanX.setFrom(from);
                        hospitalPrepareListDataBeanX.setLast_page(last_page);
                        hospitalPrepareListDataBeanX.setLast_page_url(last_page_url);
                        hospitalPrepareListDataBeanX.setNext_page_url(next_page_url);
                        hospitalPrepareListDataBeanX.setPath(path);
                        hospitalPrepareListDataBeanX.setPer_page(per_page);
                        hospitalPrepareListDataBeanX.setPrev_page_url(prev_page_url);
                        hospitalPrepareListDataBeanX.setTo(to);
                        hospitalPrepareListDataBeanX.setTotal(total);
                        hospitalPrepareListDataBeanX.setData(hospitalPrepareListDataBeans);

                        if (data !=null){


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = hospitalPrepareListDataBeanX.getCurrent_page();
                                    int lastPage = hospitalPrepareListDataBeanX.getLast_page();
                                    String protectTime = jsonObject1.optString("protectTime");
                                    int prepareId = jsonObject1.optInt("prepareId");
                                    int status = jsonObject1.optInt("status");
                                    String productName = jsonObject1.optString("productName");
                                    String hospitalName = jsonObject1.optString("hospitalName");
                                    String truename = jsonObject1.optString("truename");
                                    String hospitalHeadPic = jsonObject1.optString("hospitalHeadPic");


                                    HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean dataBean = new HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean();
                                    dataBean.setProtectTime(protectTime);
                                    dataBean.setPrepareId(prepareId);
                                    dataBean.setStatus(status);
                                    dataBean.setProductName(productName);
                                    dataBean.setHospitalName(hospitalName);
                                    dataBean.setTruename(truename);
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setHospitalHeadPic(hospitalHeadPic);
                                    hospitalPrepareListDataBeans.add(dataBean);

                                }

                            }
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){
                            searPrepareListAdapter.notifyDataSetChanged();
                            mRcPrepareList.setPullLoadMoreCompleted();


                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchPrepareListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchPrepareListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(SearchPrepareListActivity.this,homeFragmentHospitalPrepareListResponse.getMsg());
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

}