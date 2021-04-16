package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.adapter.SearchHospitalListAdapter;
import com.example.daoqimanagement.bean.SearchHospitalListResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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

public class SearchHospitalListActivity extends AppCompatActivity {

    private PullLoadMoreRecyclerView mRcHospitalList;
    private SearchHospitalListAdapter searchHospitalListAdapter;
    private RelativeLayout mRlFinish;
    private TextView mTvSearch;
    private RelativeLayout mRlCancel;
    DialogTokenIntent dialogTokenIntent = null;
    private EditText mEtSearch;
    String chars;


    private String province,city,region;
    SearchHospitalListResponse.DataBeanX searchDataBeanX = new SearchHospitalListResponse.DataBeanX();
    List<SearchHospitalListResponse.DataBeanX.DataBean> searchDataBeans = new ArrayList<>();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_search_hospital_list);
        mRcHospitalList = findViewById(R.id.search_hospital_rc);
        mTvSearch = findViewById(R.id.search_hospital_tv_search);
        mRlCancel = findViewById(R.id.search_hospital_rl_cancel);
        mEtSearch = findViewById(R.id.search_hospital_et);
        mRlFinish = findViewById(R.id.search_hospital_rl_finish);
        Intent intent = getIntent();
        province = intent.getStringExtra("province");
        city = intent.getStringExtra("city");
        region = intent.getStringExtra("region");


        if (province.equals("0")){
            getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15");
        }else if (city.equals("0") ){
            getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province);
        }else if (region.equals("0")){
            getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city);
        }else if (!city.equals("0") && !region.equals("0")){
            getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city+"&region="+region);
        }


        mTvSearch.setVisibility(View.GONE);
        mRlCancel.setVisibility(View.GONE);


        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chars = String.valueOf(charSequence).replace(" ","");
                if (chars.equals("")){
                    mTvSearch.setVisibility(View.GONE);
                }else {
                    mTvSearch.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDataBeans.clear();
                if (province.equals("0")){
                    getHospitalListSearchRes(Api.URL+"/v1/hospital/list?page=1&limit=15&keyword="+chars);
                }else if (city.equals("0") ){
                    getHospitalListSearchRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&keyword="+chars);
                }else if (region.equals("0")){
                    getHospitalListSearchRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city+"&keyword="+chars);
                }else if (!city.equals("0") && !region.equals("0")){
                    getHospitalListSearchRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city+"&region="+region+"&keyword="+chars);
                }
                mTvSearch.setVisibility(View.GONE);
                mRlCancel.setVisibility(View.VISIBLE);
                hideSoftKeyboard(view);
            }
        });

        mRlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDataBeans.clear();
                mRlCancel.setVisibility(View.GONE);
                mTvSearch.setVisibility(View.GONE);
                if (city.equals("0") ){
                    getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province);
                }else if (region.equals("0")){
                    getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city);
                }else if (!city.equals("0") && !region.equals("0")){
                    getHospitalListRes(Api.URL+"/v1/hospital/list?page=1&limit=15&province="+province+"&city="+city+"&region="+region);
                }
                hideSoftKeyboard(view);
                mEtSearch.setText("");
            }
        });

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(SearchHospitalListActivity.this);
                finish();
            }
        });
    }

    private void getHospitalListRes(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

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
                        ToastUtils.showTextToast2(SearchHospitalListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final SearchHospitalListResponse searchHospitalListResponse = new SearchHospitalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");

                    searchHospitalListResponse.setCode(code);
                    searchHospitalListResponse.setMsg(msg);
                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        searchHospitalListResponse.setData(searchDataBeanX);
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

                        searchDataBeanX.setCurrent_page(current_page);
                        searchDataBeanX.setFirst_page_url(first_page_url);
                        searchDataBeanX.setFrom(from);
                        searchDataBeanX.setLast_page(last_page);
                        searchDataBeanX.setLast_page_url(last_page_url);
                        searchDataBeanX.setNext_page_url(next_page_url);
                        searchDataBeanX.setPath(path);
                        searchDataBeanX.setPer_page(per_page);
                        searchDataBeanX.setPrev_page_url(prev_page_url);
                        searchDataBeanX.setTo(to);
                        searchDataBeanX.setTotal(total);
                        searchDataBeanX.setData(searchDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int status = jsonObject1.optInt("status");
                                int hospitalid = jsonObject1.optInt("hospitalid");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String detail = jsonObject1.optString("detail");
                                String level = jsonObject1.optString("level");
                                String nature = jsonObject1.optString("nature");
                                String areaName = jsonObject1.optString("areaName");


                                SearchHospitalListResponse.DataBeanX.DataBean dataBean = new SearchHospitalListResponse.DataBeanX.DataBean();
                                dataBean.setStatus(status);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setHospitalid(hospitalid);
                                dataBean.setDetail(detail);
                                dataBean.setLevel(level);
                                dataBean.setNature(nature);
                                dataBean.setAreaName(areaName);
                                searchDataBeans.add(dataBean);

                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (searchHospitalListResponse.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            searchHospitalListAdapter = new SearchHospitalListAdapter(SearchHospitalListActivity.this, searchDataBeans);
                            mRcHospitalList.setLinearLayout();
                            mRcHospitalList.setPullRefreshEnable(false);
                            mRcHospitalList.setAdapter(searchHospitalListAdapter);
                          searchHospitalListAdapter.setOnItemClickListener(new SearchHospitalListAdapter.OnItemClickListener() {
                              @Override
                              public void onItemClick(String hospitalName, String detail, String level, String nature, String areaName, int status, String headPic, int hospitalid, int position) {
                                  if (status == 1){
                                      Intent intent = new Intent();
                                      intent.putExtra("hospitalName",hospitalName);
                                      intent.putExtra("detail",detail);
                                      intent.putExtra("level",level);
                                      intent.putExtra("nature",nature);
                                      intent.putExtra("areaName",areaName);
                                      intent.putExtra("headPic",headPic);
                                      intent.putExtra("hospitalid",hospitalid);
                                      setResult(RESULT_OK,intent);
                                      ActivityCollector.removeActivity(SearchHospitalListActivity.this);
                                      finish();
                                  }else if (status == 2){
                                      ToastUtils.showTextToast2(SearchHospitalListActivity.this,"该医院已被锁定");
                                  }else if (status == 3){
                                      ToastUtils.showTextToast2(SearchHospitalListActivity.this,"该医院暂未开放");
                                  }
                              }

                          });
                            mRcHospitalList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {

                                }

                                @Override
                                public void onLoadMore() {
//                                    Log.d("pagenext", String.valueOf(searchDataBeanX.getCurrent_page()));
//                                    Log.d("pagenext", String.valueOf( searchDataBeanX.getLast_page()));
                                    int nextpage = searchDataBeanX.getCurrent_page()+1;

                                    if (province.equals("0")){
                                        getHospitalListResLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15");

                                    }else if (city.equals("0") ){
                                        getHospitalListResLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province);

                                    }else if (region.equals("0")){
                                        getHospitalListResLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province+"&city="+city);

                                    }else if (!city.equals("0") && !region.equals("0")){
                                        getHospitalListResLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province+"&city="+city+"&region="+region);

                                    }



                                }
                            });


                        } else if (searchHospitalListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchHospitalListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchHospitalListActivity.this, LoginActivity.class);
                                      ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = searchHospitalListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchHospitalListActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    private void getHospitalListResLoadMore(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

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
                        ToastUtils.showTextToast2(SearchHospitalListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final SearchHospitalListResponse searchHospitalListResponse = new SearchHospitalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");

                    searchHospitalListResponse.setCode(code);
                    searchHospitalListResponse.setMsg(msg);
                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        searchHospitalListResponse.setData(searchDataBeanX);
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

                        searchDataBeanX.setCurrent_page(current_page);
                        searchDataBeanX.setFirst_page_url(first_page_url);
                        searchDataBeanX.setFrom(from);
                        searchDataBeanX.setLast_page(last_page);
                        searchDataBeanX.setLast_page_url(last_page_url);
                        searchDataBeanX.setNext_page_url(next_page_url);
                        searchDataBeanX.setPath(path);
                        searchDataBeanX.setPer_page(per_page);
                        searchDataBeanX.setPrev_page_url(prev_page_url);
                        searchDataBeanX.setTo(to);
                        searchDataBeanX.setTotal(total);
                        searchDataBeanX.setData(searchDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int status = jsonObject1.optInt("status");
                                int hospitalid = jsonObject1.optInt("hospitalid");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String detail = jsonObject1.optString("detail");
                                String level = jsonObject1.optString("level");
                                String nature = jsonObject1.optString("nature");
                                String areaName = jsonObject1.optString("areaName");


                                SearchHospitalListResponse.DataBeanX.DataBean dataBean = new SearchHospitalListResponse.DataBeanX.DataBean();
                                dataBean.setStatus(status);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setHospitalid(hospitalid);
                                dataBean.setDetail(detail);
                                dataBean.setLevel(level);
                                dataBean.setNature(nature);
                                dataBean.setAreaName(areaName);
                                searchDataBeans.add(dataBean);

                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (searchHospitalListResponse.getCode() == 0) {

//
                            mRcHospitalList.setPullLoadMoreCompleted();
                            searchHospitalListAdapter.notifyDataSetChanged();

                        } else if (searchHospitalListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchHospitalListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchHospitalListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = searchHospitalListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchHospitalListActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void getHospitalListSearchRes(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

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
                        ToastUtils.showTextToast2(SearchHospitalListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final SearchHospitalListResponse searchHospitalListResponse = new SearchHospitalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    searchHospitalListResponse.setCode(code);
                    searchHospitalListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        searchHospitalListResponse.setData(searchDataBeanX);
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

                        searchDataBeanX.setCurrent_page(current_page);
                        searchDataBeanX.setFirst_page_url(first_page_url);
                        searchDataBeanX.setFrom(from);
                        searchDataBeanX.setLast_page(last_page);
                        searchDataBeanX.setLast_page_url(last_page_url);
                        searchDataBeanX.setNext_page_url(next_page_url);
                        searchDataBeanX.setPath(path);
                        searchDataBeanX.setPer_page(per_page);
                        searchDataBeanX.setPrev_page_url(prev_page_url);
                        searchDataBeanX.setTo(to);
                        searchDataBeanX.setTotal(total);
                        searchDataBeanX.setData(searchDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int status = jsonObject1.optInt("status");
                                int hospitalid = jsonObject1.optInt("hospitalid");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String detail = jsonObject1.optString("detail");
                                String level = jsonObject1.optString("level");
                                String nature = jsonObject1.optString("nature");
                                String areaName = jsonObject1.optString("areaName");


                                SearchHospitalListResponse.DataBeanX.DataBean dataBean = new SearchHospitalListResponse.DataBeanX.DataBean();
                                dataBean.setStatus(status);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setHospitalid(hospitalid);
                                dataBean.setDetail(detail);
                                dataBean.setLevel(level);
                                dataBean.setNature(nature);
                                dataBean.setAreaName(areaName);
                                searchDataBeans.add(dataBean);

                            }


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (searchHospitalListResponse.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            searchHospitalListAdapter = new SearchHospitalListAdapter(SearchHospitalListActivity.this, searchDataBeans);
                            mRcHospitalList.setLinearLayout();
                            mRcHospitalList.setAdapter(searchHospitalListAdapter);
                            mRcHospitalList.setPullRefreshEnable(false);
                            searchHospitalListAdapter.setOnItemClickListener(new SearchHospitalListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String hospitalName, String detail, String level, String nature, String areaName, int status, String headPic, int hospitalid, int position) {
                                    if (status == 1){
                                        Intent intent = new Intent();
                                        intent.putExtra("hospitalName",hospitalName);
                                        intent.putExtra("detail",detail);
                                        intent.putExtra("level",level);
                                        intent.putExtra("nature",nature);
                                        intent.putExtra("areaName",areaName);
                                        intent.putExtra("headPic",headPic);
                                        intent.putExtra("hospitalid",hospitalid);
                                        setResult(RESULT_OK,intent);
                                        ActivityCollector.removeActivity(SearchHospitalListActivity.this);
                                        finish();
                                    }else if (status == 2){
                                        ToastUtils.showTextToast2(SearchHospitalListActivity.this,"该医院已被锁定");
                                    }else if (status == 3){
                                        ToastUtils.showTextToast2(SearchHospitalListActivity.this,"该医院暂未开放");
                                    }
                                }

                            });

                            mRcHospitalList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {

                                }

                                @Override
                                public void onLoadMore() {

                                    int nextpage = searchDataBeanX.getCurrent_page()+1;
                                    if (province.equals("0")){
                                        getHospitalListResSearchLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15");
                                    }else if (city.equals("0") ){
                                        getHospitalListResSearchLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province);
                                    }else if (region.equals("0")){
                                        getHospitalListResSearchLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province+"&city="+city);
                                    }else if (!city.equals("0") && !region.equals("0")){
                                        getHospitalListResSearchLoadMore(Api.URL+"/v1/hospital/list?page="+nextpage+"&limit=15&province="+province+"&city="+city+"&region="+region);
                                    }
                                }
                            });

                        } else if (searchHospitalListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchHospitalListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchHospitalListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = searchHospitalListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchHospitalListActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void getHospitalListResSearchLoadMore(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

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
                        ToastUtils.showTextToast2(SearchHospitalListActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final SearchHospitalListResponse searchHospitalListResponse = new SearchHospitalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    searchHospitalListResponse.setCode(code);
                    searchHospitalListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        searchHospitalListResponse.setData(searchDataBeanX);
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

                        searchDataBeanX.setCurrent_page(current_page);
                        searchDataBeanX.setFirst_page_url(first_page_url);
                        searchDataBeanX.setFrom(from);
                        searchDataBeanX.setLast_page(last_page);
                        searchDataBeanX.setLast_page_url(last_page_url);
                        searchDataBeanX.setNext_page_url(next_page_url);
                        searchDataBeanX.setPath(path);
                        searchDataBeanX.setPer_page(per_page);
                        searchDataBeanX.setPrev_page_url(prev_page_url);
                        searchDataBeanX.setTo(to);
                        searchDataBeanX.setTotal(total);
                        searchDataBeanX.setData(searchDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int status = jsonObject1.optInt("status");
                                int hospitalid = jsonObject1.optInt("hospitalid");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String detail = jsonObject1.optString("detail");
                                String level = jsonObject1.optString("level");
                                String nature = jsonObject1.optString("nature");
                                String areaName = jsonObject1.optString("areaName");


                                SearchHospitalListResponse.DataBeanX.DataBean dataBean = new SearchHospitalListResponse.DataBeanX.DataBean();
                                dataBean.setStatus(status);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setHospitalid(hospitalid);
                                dataBean.setDetail(detail);
                                dataBean.setLevel(level);
                                dataBean.setNature(nature);
                                dataBean.setAreaName(areaName);
                                searchDataBeans.add(dataBean);

                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (searchHospitalListResponse.getCode() == 0) {

//
                            searchHospitalListAdapter.notifyDataSetChanged();
                            mRcHospitalList.setPullLoadMoreCompleted();

                        } else if (searchHospitalListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchHospitalListActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchHospitalListActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = searchHospitalListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchHospitalListActivity.this, msg);
                        }
                    }
                });

            }
        });

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