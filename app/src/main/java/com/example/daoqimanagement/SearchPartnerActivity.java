package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoqimanagement.adapter.SearchHospitalListAdapter;
import com.example.daoqimanagement.adapter.TeamListAdapter;
import com.example.daoqimanagement.bean.SearchHospitalListResponse;
import com.example.daoqimanagement.bean.TeamListResponse;
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

public class SearchPartnerActivity extends AppCompatActivity {

    private RelativeLayout mRlFinish,mRlCancelSearch;
    private EditText mEtSearch;
    private PullLoadMoreRecyclerView mRcPartnerList;
    private TextView mTvSearch;
    TeamListAdapter teamListAdapter;
    DialogTokenIntent dialogTokenIntent = null;

    TeamListResponse.DataBeanX dataBeanX = new TeamListResponse.DataBeanX();
    List<TeamListResponse.DataBeanX.DataBean> dataBeans = new ArrayList<>();
    String uid = "21312";

    String chars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_search_partner);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


        mRlFinish = findViewById(R.id.search_partner_rl_finish);
        mEtSearch = findViewById(R.id.search_partner_et);
        mRcPartnerList = findViewById(R.id.search_partner_rc);
        mTvSearch= findViewById(R.id.search_partner_tv_search);
        mRlCancelSearch = findViewById(R.id.search_partner_rl_cancel);
        mTvSearch.setVisibility(View.GONE);
        mRlCancelSearch.setVisibility(View.GONE);
        getHospitalListSearchRes(Api.URL+"/v1/team/teamMembersList?uid="+uid+"&page=1");

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(SearchPartnerActivity.this);
                finish();
            }
        });
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
                mTvSearch.setVisibility(View.GONE);
                mRlCancelSearch.setVisibility(View.VISIBLE);
            }
        });
        mRlCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvSearch.setVisibility(View.GONE);
                mRlCancelSearch.setVisibility(View.GONE);
                mEtSearch.setText("");
                hideSoftKeyboard(view);
                dataBeans.clear();
                getHospitalListSearchRes(Api.URL+"/v1/team/teamMembersList?uid="+uid+"&page=1");

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
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
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
                        ToastUtils.showTextToast2(SearchPartnerActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final TeamListResponse teamListResponse = new TeamListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    teamListResponse.setCode(code);
                    teamListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        teamListResponse.setData(dataBeanX);
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

                        dataBeanX.setCurrent_page(current_page);
                        dataBeanX.setFirst_page_url(first_page_url);
                        dataBeanX.setFrom(from);
                        dataBeanX.setLast_page(last_page);
                        dataBeanX.setLast_page_url(last_page_url);
                        dataBeanX.setNext_page_url(next_page_url);
                        dataBeanX.setPath(path);
                        dataBeanX.setPer_page(per_page);
                        dataBeanX.setPrev_page_url(prev_page_url);
                        dataBeanX.setTo(to);
                        dataBeanX.setTotal(total);
                        dataBeanX.setData(dataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int uid = jsonObject1.optInt("uid");
                                String truename = jsonObject1.optString("truename");
                                int user_type = jsonObject1.optInt("user_type");
                                boolean inTeam = jsonObject1.optBoolean("inTeam");



                                TeamListResponse.DataBeanX.DataBean dataBean = new TeamListResponse.DataBeanX.DataBean();
                                dataBean.setUid(uid);
                                dataBean.setTruename(truename);
                                dataBean.setUser_type(user_type);
                                dataBean.setInTeam(inTeam);
                                dataBeans.add(dataBean);

                            }


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (teamListResponse.getCode() == 0) {

                            teamListAdapter = new TeamListAdapter(SearchPartnerActivity.this,dataBeans);
                            mRcPartnerList.setLinearLayout();
                            mRcPartnerList.setAdapter(teamListAdapter);
                            mRcPartnerList.setPullRefreshEnable(false);
                            teamListAdapter.setOnItemClickListener(new TeamListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int uid, boolean inTeam) {
                                    if (inTeam == true){
                                        ToastUtils.showTextToast2(SearchPartnerActivity.this,"该用户已被选择");
                                    }else {
                                        Intent intent = new Intent();
                                        intent.putExtra("uid",uid);
                                        setResult(RESULT_OK,intent);
                                        ActivityCollector.removeActivity(SearchPartnerActivity.this);
                                        finish();
                                    }
                                }
                            });
                            mRcPartnerList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {

                                }

                                @Override
                                public void onLoadMore() {
                                    int nextPage = dataBeanX.getCurrent_page()+1;
                                    getHospitalListSearchResOnLoad(Api.URL+"/v1/team/teamMembersList?uid="+"8"+"&page="+String.valueOf(nextPage));
                                }
                            });
                        } else if (teamListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchPartnerActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchPartnerActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = teamListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchPartnerActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    private void getHospitalListSearchResOnLoad(String url) {

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
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
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
                        ToastUtils.showTextToast2(SearchPartnerActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final TeamListResponse teamListResponse = new TeamListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    teamListResponse.setCode(code);
                    teamListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        teamListResponse.setData(dataBeanX);
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

                        dataBeanX.setCurrent_page(current_page);
                        dataBeanX.setFirst_page_url(first_page_url);
                        dataBeanX.setFrom(from);
                        dataBeanX.setLast_page(last_page);
                        dataBeanX.setLast_page_url(last_page_url);
                        dataBeanX.setNext_page_url(next_page_url);
                        dataBeanX.setPath(path);
                        dataBeanX.setPer_page(per_page);
                        dataBeanX.setPrev_page_url(prev_page_url);
                        dataBeanX.setTo(to);
                        dataBeanX.setTotal(total);
                        dataBeanX.setData(dataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {

                                int uid = jsonObject1.optInt("uid");
                                String truename = jsonObject1.optString("truename");
                                int user_type = jsonObject1.optInt("user_type");
                                boolean inTeam = jsonObject1.optBoolean("inTeam");



                                TeamListResponse.DataBeanX.DataBean dataBean = new TeamListResponse.DataBeanX.DataBean();
                                dataBean.setUid(uid);
                                dataBean.setTruename(truename);
                                dataBean.setUser_type(user_type);
                                dataBean.setInTeam(inTeam);
                                dataBeans.add(dataBean);

                            }


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (teamListResponse.getCode() == 0) {
                            teamListAdapter.notifyDataSetChanged();
                            mRcPartnerList.setPullLoadMoreCompleted();
                        } else if (teamListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(SearchPartnerActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(SearchPartnerActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = teamListResponse.getMsg();
                            ToastUtils.showTextToast2(SearchPartnerActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    public void hideSoftKeyboard(View view) {
        //这里获取view为参数 之前试过用context,LoginActivity.this会造成闪退
        //view不会闪退
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}