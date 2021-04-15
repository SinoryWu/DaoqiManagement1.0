package com.example.daoqimanagement.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.daoqimanagement.LoginActivity;
import com.example.daoqimanagement.PartnerDetailActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.ResourceDetailActivity;
import com.example.daoqimanagement.ResourceDetailWebActivity;
import com.example.daoqimanagement.adapter.MentorFragmentAdminTeamListAdapter;
import com.example.daoqimanagement.adapter.MentorFragmentUserTeamListAdapter;
import com.example.daoqimanagement.adapter.ResourcesListAdapter;
import com.example.daoqimanagement.bean.HomeFragmentHospitalPrepareListResponse;
import com.example.daoqimanagement.bean.MentorsFragmentAdminTeamListResponse;
import com.example.daoqimanagement.bean.MentorsFragmentUserTeamListResponse;
import com.example.daoqimanagement.bean.ResourceListResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MentorsFragment extends Fragment implements View.OnClickListener {

    private Button mBtnTeam, mBtnResources;
    private PullLoadMoreRecyclerView mRcTeamList, mRcResourcesList;
    private RelativeLayout mRlTeamList, mRlResourcesList;
    private MentorFragmentUserTeamListAdapter mentorFragmentUserTeamListAdapter;
    private MentorFragmentAdminTeamListAdapter mentorFragmentAdminTeamListAdapter;
    private ResourcesListAdapter resourcesListAdapter;
    DialogTokenIntent dialogTokenIntent = null;

    String mobile = "100";

    MentorsFragmentAdminTeamListResponse.DataBeanX adminTeamListDateBeanX = new MentorsFragmentAdminTeamListResponse.DataBeanX();

    List<MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean> adminTeamListDateBeans = new ArrayList<>();
    ResourceListResponse.DataBeanX resourceDataBeanX = new ResourceListResponse.DataBeanX();
    List<ResourceListResponse.DataBeanX.DataBean> resourceListDataBeans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mentors, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(getRefreshToSp("refresh",""))){
            Log.d("refreshresource","onStart");
        }else {
            resourceListDataBeans.clear();
            getResResourceList(Api.URL+"/v1/encyclopedia/list");
            Log.d("refreshresource", getRefreshToSp("refresh",""));
        }

//        Log.d("refreshresource", getRefreshToSp("refresh",""));

    }

    public void initView(View view) {
        mBtnTeam = view.findViewById(R.id.fragment_mentors_tab_btn_team);
        mBtnResources = view.findViewById(R.id.fragment_mentors_tab_btn_resources);
        mRcTeamList = view.findViewById(R.id.fragment_mentors_rc_team_list);
        mRcResourcesList = view.findViewById(R.id.fragment_mentors_rc_resources_list);
        mRlTeamList = view.findViewById(R.id.fragment_mentors_rl_team_list);
        mRlResourcesList = view.findViewById(R.id.fragment_mentors_rl_resources_list);
        mRlTeamList.setVisibility(View.GONE);
        mRlResourcesList.setVisibility(View.GONE);
        mBtnTeam.setOnClickListener(this);
        mBtnResources.setOnClickListener(this);
        mBtnTeam.performClick();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_mentors_tab_btn_team:
                mBtnTeam.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnResources.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnTeam.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnResources.setTextColor(Color.parseColor("#A6BCD0"));
                mRlTeamList.setVisibility(View.VISIBLE);
                mRlResourcesList.setVisibility(View.GONE);
                Log.d("usertype", getUserTypeToSp("usertype", ""));
//                getResUserProgress(Api.URL+"/v1/team/teamListForUser");
//                getResAdminTeamList(Api.URL + "/v1/team/teamList");
                adminTeamListDateBeans.clear();
                if (getUserTypeToSp("usertype", "").equals("1")) {
                    getResUserProgress(Api.URL + "/v1/team/teamListForUser");
                } else if (getUserTypeToSp("usertype", "").equals("2")) {
                    if (!GetSharePerfenceSP.getType(getContext()).equals("4")){
                        getResAdminTeamList(Api.URL + "/v1/team/teamList");
                    }

                }
                break;
            case R.id.fragment_mentors_tab_btn_resources:
                mBtnResources.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnTeam.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnTeam.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnResources.setTextColor(Color.parseColor("#FFFFFF"));
                mRlTeamList.setVisibility(View.GONE);
                mRlResourcesList.setVisibility(View.VISIBLE);
                resourceListDataBeans.clear();
                getResResourceList(Api.URL+"/v1/encyclopedia/list");
                break;
        }
    }

    protected void getResResourceList(String url) {
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
//        JSONObject jsonObject = new JSONObject(params);
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toString();


        final Request request = new Request.Builder()
                .url(url)
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                //封装java对象

                cleanRefreshToSp("refresh","");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);


                       final ResourceListResponse resourceListResponse = new ResourceListResponse();


//                        final ResourceListResponse resourceListResponse = new ResourceListResponse();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            //第一层解析
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("msg");
                            JSONObject datax = jsonObject.optJSONObject("data");
                            resourceListResponse.setCode(code);
                            resourceListResponse.setMsg(msg);

                            if (datax != null) {
                                //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                                resourceListResponse.setData(resourceDataBeanX);
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

                                resourceDataBeanX.setCurrent_page(current_page);
                                resourceDataBeanX.setFirst_page_url(first_page_url);
                                resourceDataBeanX.setFrom(from);
                                resourceDataBeanX.setLast_page(last_page);
                                resourceDataBeanX.setLast_page_url(last_page_url);
                                resourceDataBeanX.setNext_page_url(next_page_url);
                                resourceDataBeanX.setPath(path);
                                resourceDataBeanX.setPer_page(per_page);
                                resourceDataBeanX.setPrev_page_url(prev_page_url);
                                resourceDataBeanX.setTo(to);
                                resourceDataBeanX.setTotal(total);
                                resourceDataBeanX.setData(resourceListDataBeans);

                                if (data !=null){

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject jsonObject1 = data.getJSONObject(i);
                                        if (jsonObject1 != null) {
                                            String title = jsonObject1.optString("title");
                                            int titleId = jsonObject1.optInt("titleId");
                                            String updatedAt = jsonObject1.optString("updatedAt");
                                            int readNum = jsonObject1.optInt("readNum");
                                            int type = jsonObject1.optInt("type");
                                            String url = jsonObject1.optString("url");


                                            ResourceListResponse.DataBeanX.DataBean dataBean = new ResourceListResponse.DataBeanX.DataBean();
                                            dataBean.setTitle(title);
                                            dataBean.setTitleId(titleId);
                                            dataBean.setUpdatedAt(updatedAt);
                                            dataBean.setReadNum(readNum);
                                            dataBean.setType(type);
                                            dataBean.setUrl(url);
                                            resourceListDataBeans.add(dataBean);

                                        }

                                    }
                                }
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (resourceListResponse.getCode() == 0) {
                            resourcesListAdapter = new ResourcesListAdapter(getContext(),resourceListDataBeans);
                            mRcResourcesList.setPullRefreshEnable(false);
                            mRcResourcesList.setLinearLayout();
                            mRcResourcesList.setAdapter(resourcesListAdapter);
                            mRcResourcesList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {

                                }

                                @Override
                                public void onLoadMore() {
                                    String nextPage = String.valueOf(resourceDataBeanX.getCurrent_page()+1);
                                    getResResourceListLoadMore(Api.URL+"/v1/encyclopedia/list?page="+nextPage);
                                }
                            });

                            resourcesListAdapter.setOnItemClickListener(new ResourcesListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick( String url, int type,int titleId) {
                                    Log.d("13dasdasd", GetSharePerfenceSP.getToken(getContext()));
                                    Log.d("13dasdasd", GetSharePerfenceSP.getUid(getContext()));
                                    String titleId1 = String.valueOf(titleId);
                                    if (type == 1){
                                        Intent intent = new Intent(getContext(), ResourceDetailActivity.class);
                                        intent.putExtra("titleId",titleId1);
                                        startActivity(intent);
                                    }else if (type == 2){
                                        Intent intent = new Intent(getContext(), ResourceDetailWebActivity.class);
                                        intent.putExtra("url",url);
                                        intent.putExtra("titleId",titleId1);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else if (resourceListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = resourceListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }

    protected void getResResourceListLoadMore(String url) {
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
//        JSONObject jsonObject = new JSONObject(params);
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toString();


        final Request request = new Request.Builder()
                .url(url)
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                //封装java对象


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);


                        final ResourceListResponse resourceListResponse = new ResourceListResponse();


//                        final ResourceListResponse resourceListResponse = new ResourceListResponse();

                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            //第一层解析
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("msg");
                            JSONObject datax = jsonObject.optJSONObject("data");
                            resourceListResponse.setCode(code);
                            resourceListResponse.setMsg(msg);

                            if (datax != null) {
                                //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                                resourceListResponse.setData(resourceDataBeanX);
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

                                resourceDataBeanX.setCurrent_page(current_page);
                                resourceDataBeanX.setFirst_page_url(first_page_url);
                                resourceDataBeanX.setFrom(from);
                                resourceDataBeanX.setLast_page(last_page);
                                resourceDataBeanX.setLast_page_url(last_page_url);
                                resourceDataBeanX.setNext_page_url(next_page_url);
                                resourceDataBeanX.setPath(path);
                                resourceDataBeanX.setPer_page(per_page);
                                resourceDataBeanX.setPrev_page_url(prev_page_url);
                                resourceDataBeanX.setTo(to);
                                resourceDataBeanX.setTotal(total);
                                resourceDataBeanX.setData(resourceListDataBeans);

                                if (data !=null){

                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject jsonObject1 = data.getJSONObject(i);
                                        if (jsonObject1 != null) {
                                            String title = jsonObject1.optString("title");
                                            int titleId = jsonObject1.optInt("titleId");
                                            String updatedAt = jsonObject1.optString("updatedAt");
                                            int readNum = jsonObject1.optInt("readNum");
                                            int type = jsonObject1.optInt("type");
                                            String url = jsonObject1.optString("url");


                                            ResourceListResponse.DataBeanX.DataBean dataBean = new ResourceListResponse.DataBeanX.DataBean();
                                            dataBean.setTitle(title);
                                            dataBean.setTitleId(titleId);
                                            dataBean.setUpdatedAt(updatedAt);
                                            dataBean.setReadNum(readNum);
                                            dataBean.setType(type);
                                            dataBean.setUrl(url);
                                            resourceListDataBeans.add(dataBean);

                                        }

                                    }
                                }
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (resourceListResponse.getCode() == 0) {
                           mRcResourcesList.setPullLoadMoreCompleted();
                           resourcesListAdapter.notifyDataSetChanged();

                        } else if (resourceListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = resourceListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }

    protected void getResUserProgress(String url) {
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
//        JSONObject jsonObject = new JSONObject(params);
        JSONObject jsonObject = new JSONObject();
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                //封装java对象


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);


                        MentorsFragmentUserTeamListResponse mentorsFragmentUserApprovalListResponse = new Gson().fromJson(res, MentorsFragmentUserTeamListResponse.class);

                        if (mentorsFragmentUserApprovalListResponse.getCode() == 0) {
                            if (mentorsFragmentUserApprovalListResponse.getData() != null) {
                                mentorFragmentUserTeamListAdapter = new MentorFragmentUserTeamListAdapter(getContext(), mentorsFragmentUserApprovalListResponse.getData());
                                mRcTeamList.setLinearLayout();
                                mRcTeamList.setPullRefreshEnable(false);
                                mRcTeamList.setPushRefreshEnable(false);
                                mentorFragmentUserTeamListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer, null));
                                mRcTeamList.setAdapter(mentorFragmentUserTeamListAdapter);
                                mentorFragmentUserTeamListAdapter.setOnItemClickListener(new MentorFragmentUserTeamListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, String phoneNumber, int position) {
                                        mobile = phoneNumber;

                                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】没有授权，申请权限");
                                            MentorsFragment.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

                                        } else {
                                            Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】有权限");
                                            call(mobile);
                                        }
                                    }

                                    @Override
                                    public void onItemLongClick(View v) {

                                    }
                                });

                            }

                        } else if (mentorsFragmentUserApprovalListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = mentorsFragmentUserApprovalListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }


    public void getResAdminTeamList(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "e4229c2155c3571ba260446f1ffda8bd")
//                .addHeader("uid", "12")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);
                Log.d("adminteam", res);
                final MentorsFragmentAdminTeamListResponse mentorsFragmentAdminTeamListResponse = new MentorsFragmentAdminTeamListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");

                    mentorsFragmentAdminTeamListResponse.setCode(code);
                    mentorsFragmentAdminTeamListResponse.setMsg(msg);
                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        mentorsFragmentAdminTeamListResponse.setData(adminTeamListDateBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from = datax.optInt("from");
                        int last_page = datax.optInt("last_page");
                        String last_page_url = datax.optString("last_page_url");
                        String next_page_url = datax.optString("next_page_url");
                        String path = datax.optString("path");
                        int per_page = datax.optInt("per_page");
                        String prev_page_url = datax.optString("prev_page_url");
                        int to = datax.optInt("to");
                        int total = datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        adminTeamListDateBeanX.setCurrent_page(current_page);
                        adminTeamListDateBeanX.setFirst_page_url(first_page_url);
                        adminTeamListDateBeanX.setFrom(from);
                        adminTeamListDateBeanX.setLast_page(last_page);
                        adminTeamListDateBeanX.setLast_page_url(last_page_url);
                        adminTeamListDateBeanX.setNext_page_url(next_page_url);
                        adminTeamListDateBeanX.setPath(path);
                        adminTeamListDateBeanX.setPer_page(per_page);
                        adminTeamListDateBeanX.setPrev_page_url(prev_page_url);
                        adminTeamListDateBeanX.setTo(to);
                        adminTeamListDateBeanX.setTotal(total);
                        adminTeamListDateBeanX.setData(adminTeamListDateBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = adminTeamListDateBeanX.getCurrent_page();
                                    int lastPage = adminTeamListDateBeanX.getLast_page();
                                    String truename = jsonObject1.optString("truename");
                                    int uid = jsonObject1.optInt("uid");
                                    String mobile = jsonObject1.optString("mobile");
                                    int userType = jsonObject1.optInt("userType");
                                    String createTime = jsonObject1.optString("createTime");
                                    String headPic = jsonObject1.optString("headPic");


                                    MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean dataBean = new MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setTruename(truename);
                                    dataBean.setUid(uid);
                                    dataBean.setMobile(mobile);
                                    dataBean.setUserType(userType);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setHeadPic(headPic);
                                    adminTeamListDateBeans.add(dataBean);

                                }

                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (mentorsFragmentAdminTeamListResponse.getCode() == 0) {


                            mentorFragmentAdminTeamListAdapter = new MentorFragmentAdminTeamListAdapter(getContext(), adminTeamListDateBeans);
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                mRcHospitalList.setLayoutManager(linearLayoutManager);
                            mRcTeamList.setLinearLayout();
                            mentorFragmentAdminTeamListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer, null));

                            mRcTeamList.setAdapter(mentorFragmentAdminTeamListAdapter);
                            mRcTeamList.setPullRefreshEnable(false);
                            mRcTeamList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {
                                    adminTeamListDateBeans.clear();
                                    getResAdminTeamListRefresh(Api.URL + "/v1/team/teamList");

                                }

                                @Override
                                public void onLoadMore() {
                                    int nextPage = adminTeamListDateBeanX.getCurrent_page() + 1;
                                    getResAdminTeamListLoadMore(Api.URL + "/v1/team/teamList?page=" + nextPage);

                                }
                            });

                            mentorFragmentAdminTeamListAdapter.setOnItemClickListener(new MentorFragmentAdminTeamListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, String phoneNumber, int position) {
                                    mobile = phoneNumber;

                                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】没有授权，申请权限");
                                        MentorsFragment.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

                                    } else {
                                        Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】有权限");
                                        call(mobile);
                                    }
                                }

                                @Override
                                public void onItemLongClick(View v) {

                                }
                            });

                            mentorFragmentAdminTeamListAdapter.setOnItem1ClickListener(new MentorFragmentAdminTeamListAdapter.OnItem1ClickListener() {
                                @Override
                                public void onItem1Click(String phoneNumber, int userType, String createTime, String headPic, String trueName,int uid, int position) {
                                    Intent intent = new Intent(getContext(), PartnerDetailActivity.class);
                                    intent.putExtra("phoneNumber",phoneNumber);
                                    intent.putExtra("userType",userType);
                                    intent.putExtra("headPic",headPic);
                                    intent.putExtra("createTime",createTime);
                                    intent.putExtra("trueName",trueName);
                                    intent.putExtra("uid",uid);
                                    startActivity(intent);

                                }
                            });


                        } else if (mentorsFragmentAdminTeamListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            ToastUtils.showTextToast2(getContext(), mentorsFragmentAdminTeamListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResAdminTeamListLoadMore(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
//                .addHeader("token",getTokenToSp("token",""))
//                .addHeader("uid",getUidToSp("uid",""))
                .addHeader("token", "e4229c2155c3571ba260446f1ffda8bd")
                .addHeader("uid", "12")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);

                final MentorsFragmentAdminTeamListResponse mentorsFragmentAdminTeamListResponse = new MentorsFragmentAdminTeamListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    mentorsFragmentAdminTeamListResponse.setCode(code);
                    mentorsFragmentAdminTeamListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        mentorsFragmentAdminTeamListResponse.setData(adminTeamListDateBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from = datax.optInt("from");
                        int last_page = datax.optInt("last_page");
                        String last_page_url = datax.optString("last_page_url");
                        String next_page_url = datax.optString("next_page_url");
                        String path = datax.optString("path");
                        int per_page = datax.optInt("per_page");
                        String prev_page_url = datax.optString("prev_page_url");
                        int to = datax.optInt("to");
                        int total = datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        adminTeamListDateBeanX.setCurrent_page(current_page);
                        adminTeamListDateBeanX.setFirst_page_url(first_page_url);
                        adminTeamListDateBeanX.setFrom(from);
                        adminTeamListDateBeanX.setLast_page(last_page);
                        adminTeamListDateBeanX.setLast_page_url(last_page_url);
                        adminTeamListDateBeanX.setNext_page_url(next_page_url);
                        adminTeamListDateBeanX.setPath(path);
                        adminTeamListDateBeanX.setPer_page(per_page);
                        adminTeamListDateBeanX.setPrev_page_url(prev_page_url);
                        adminTeamListDateBeanX.setTo(to);
                        adminTeamListDateBeanX.setTotal(total);
                        adminTeamListDateBeanX.setData(adminTeamListDateBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = adminTeamListDateBeanX.getCurrent_page();
                                    int lastPage = adminTeamListDateBeanX.getLast_page();
                                    String truename = jsonObject1.optString("truename");
                                    int uid = jsonObject1.optInt("uid");
                                    String mobile = jsonObject1.optString("mobile");
                                    int userType = jsonObject1.optInt("userType");
                                    String createTime = jsonObject1.optString("createTime");
                                    String headPic = jsonObject1.optString("headPic");

                                    MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean dataBean = new MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setTruename(truename);
                                    dataBean.setUid(uid);
                                    dataBean.setMobile(mobile);
                                    dataBean.setUserType(userType);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setHeadPic(headPic);
                                    adminTeamListDateBeans.add(dataBean);

                                }

                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mentorsFragmentAdminTeamListResponse.getCode() == 0) {
                            mentorFragmentAdminTeamListAdapter.notifyDataSetChanged();
                            mRcTeamList.setPullLoadMoreCompleted();


                        } else if (mentorsFragmentAdminTeamListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            ToastUtils.showTextToast2(getContext(), mentorsFragmentAdminTeamListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResAdminTeamListRefresh(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
//                .addHeader("token",getTokenToSp("token",""))
//                .addHeader("uid",getUidToSp("uid",""))
                .addHeader("token", "e4229c2155c3571ba260446f1ffda8bd")
                .addHeader("uid", "12")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);

                final MentorsFragmentAdminTeamListResponse mentorsFragmentAdminTeamListResponse = new MentorsFragmentAdminTeamListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    mentorsFragmentAdminTeamListResponse.setCode(code);
                    mentorsFragmentAdminTeamListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        mentorsFragmentAdminTeamListResponse.setData(adminTeamListDateBeanX);
                        //第二层解析


                        int current_page = datax.optInt("current_page");
                        String first_page_url = datax.optString("first_page_url");
                        int from = datax.optInt("from");
                        int last_page = datax.optInt("last_page");
                        String last_page_url = datax.optString("last_page_url");
                        String next_page_url = datax.optString("next_page_url");
                        String path = datax.optString("path");
                        int per_page = datax.optInt("per_page");
                        String prev_page_url = datax.optString("prev_page_url");
                        int to = datax.optInt("to");
                        int total = datax.optInt("total");
                        JSONArray data = datax.optJSONArray("data");

                        adminTeamListDateBeanX.setCurrent_page(current_page);
                        adminTeamListDateBeanX.setFirst_page_url(first_page_url);
                        adminTeamListDateBeanX.setFrom(from);
                        adminTeamListDateBeanX.setLast_page(last_page);
                        adminTeamListDateBeanX.setLast_page_url(last_page_url);
                        adminTeamListDateBeanX.setNext_page_url(next_page_url);
                        adminTeamListDateBeanX.setPath(path);
                        adminTeamListDateBeanX.setPer_page(per_page);
                        adminTeamListDateBeanX.setPrev_page_url(prev_page_url);
                        adminTeamListDateBeanX.setTo(to);
                        adminTeamListDateBeanX.setTotal(total);
                        adminTeamListDateBeanX.setData(adminTeamListDateBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = adminTeamListDateBeanX.getCurrent_page();
                                    int lastPage = adminTeamListDateBeanX.getLast_page();
                                    String truename = jsonObject1.optString("truename");
                                    int uid = jsonObject1.optInt("uid");
                                    String mobile = jsonObject1.optString("mobile");
                                    int userType = jsonObject1.optInt("userType");
                                    String createTime = jsonObject1.optString("createTime");
                                    String headPic = jsonObject1.optString("headPic");


                                    MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean dataBean = new MentorsFragmentAdminTeamListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setTruename(truename);
                                    dataBean.setUid(uid);
                                    dataBean.setMobile(mobile);
                                    dataBean.setUserType(userType);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setHeadPic(headPic);
                                    adminTeamListDateBeans.add(dataBean);

                                }

                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mentorsFragmentAdminTeamListResponse.getCode() == 0) {
                            mentorFragmentAdminTeamListAdapter.notifyDataSetChanged();
                            mRcTeamList.setPullLoadMoreCompleted();


                        } else if (mentorsFragmentAdminTeamListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            ToastUtils.showTextToast2(getContext(), mentorsFragmentAdminTeamListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(mobile);
                } else {
                    Toast.makeText(getContext(), "你拒绝了权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
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


    public String getTokenToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String token = sp.getString("token", "");
        return token;
    }

    public String getUidToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        return uid;
    }

    public String getUserTypeToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype", "");
        return userType;
    }

    public void cleanRefreshToSp(String key, String val){
        SharedPreferences sp = getActivity().getSharedPreferences("refresh", MODE_PRIVATE);
        sp.edit().clear().commit();
    }
    public String getRefreshToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("refresh", MODE_PRIVATE);
        String refresh = sp.getString("refresh", "");
        return refresh;
    }


}
