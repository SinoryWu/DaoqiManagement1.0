package com.example.daoqimanagement.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.daoqimanagement.ApprovalDetailActivity;
import com.example.daoqimanagement.LoginActivity;
import com.example.daoqimanagement.NoticeDetailActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.adapter.ApprovalListAdapter;
import com.example.daoqimanagement.adapter.MentorFragmentAdminTeamListAdapter;
import com.example.daoqimanagement.adapter.NoticeListAdapter;
import com.example.daoqimanagement.bean.ApprovalListResponse;
import com.example.daoqimanagement.bean.MentorsFragmentAdminTeamListResponse;
import com.example.daoqimanagement.bean.NoticeDetailResponse;
import com.example.daoqimanagement.bean.NoticeListResponse;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class ApprovalFragment extends Fragment implements View.OnClickListener {
    private Button mBtnApproval, mBtnNotice;
    private RelativeLayout mRlApprovalList,mRlNoticeList,mRlApprovalListGone;
    private PullLoadMoreRecyclerView mRcApprovalList,mRcNoticeList;
    ApprovalListResponse.DataBeanX approvalDataBeanX = new ApprovalListResponse.DataBeanX();
    List<ApprovalListResponse.DataBeanX.DataBean> approvalDataBeans = new ArrayList<>();
    NoticeListResponse.DataBeanX noticeDataBeanX = new NoticeListResponse.DataBeanX();
    List<NoticeListResponse.DataBeanX.DataBean> noticeDataBeans = new ArrayList<>();
    ApprovalListAdapter approvalListAdapter;
    NoticeListAdapter noticeListAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);
        initView(view);
        return view;
    }



    public void initView(View view) {
        mBtnApproval = view.findViewById(R.id.fragment_approval_tab_btn_approval);
        mBtnNotice = view.findViewById(R.id.fragment_approval_tab_btn_notice);
        mRlApprovalList = view.findViewById(R.id.fragment_approval_rl_approval_list);
        mRlApprovalListGone = view.findViewById(R.id.fragment_approval_rl_approval_list_gone);
        mRlNoticeList = view.findViewById(R.id.fragment_approval_rl_notice_list);
        mRcApprovalList = view.findViewById(R.id.fragment_approval_rc_approval_list);
        mRcNoticeList = view.findViewById(R.id.fragment_approval_rc_notice_list);


        mBtnApproval.setOnClickListener(this);
        mBtnNotice.setOnClickListener(this);
        mRlApprovalList.setVisibility(View.GONE);
        mRlApprovalListGone.setVisibility(View.GONE);
        mRlNoticeList.setVisibility(View.GONE);
        mBtnApproval.performClick();
        intent = new Intent(getContext(), NoticeDetailActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getRefreshToSp("refresh","").length() >0 || getRefreshToSp("refresh","")!= null || !TextUtils.isEmpty(getRefreshToSp("refresh",""))){
            String refresh = getRefreshToSp("refresh","");
            if (refresh.equals("refreshNoticeList")){
                noticeDataBeans.clear();
                getResNoticeList(Api.URL+"/v1/notice/list");
            }else if (refresh.equals("refreshApprovalList")){
                if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                    approvalDataBeans.clear();
                    getResApprovalList(Api.URL+"/v1/approval/list");
                }
            }


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_approval_tab_btn_approval:
                mBtnApproval.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnNotice.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnApproval.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnNotice.setTextColor(Color.parseColor("#A6BCD0"));
                approvalDataBeans.clear();
                if (GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                    mRlApprovalList.setVisibility(View.GONE);
                    mRlApprovalListGone.setVisibility(View.VISIBLE);
                }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){

                    mRlApprovalList.setVisibility(View.VISIBLE);
                    mRlApprovalListGone.setVisibility(View.GONE);
                    approvalDataBeans.clear();
                    getResApprovalList(Api.URL+"/v1/approval/list");
                }

                mRlNoticeList.setVisibility(View.GONE);

                break;
            case R.id.fragment_approval_tab_btn_notice:
                mBtnApproval.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnNotice.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnApproval.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnNotice.setTextColor(Color.parseColor("#FFFFFF"));
                mRlApprovalList.setVisibility(View.GONE);
                mRlApprovalListGone.setVisibility(View.GONE);
                mRlNoticeList.setVisibility(View.VISIBLE);
                noticeDataBeans.clear();
                getResNoticeList(Api.URL+"/v1/notice/list");
                break;
        }
    }


    public void getResApprovalList(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                final ApprovalListResponse approvalListResponse = new ApprovalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    approvalListResponse.setCode(code);
                    approvalListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        approvalListResponse.setData(approvalDataBeanX);
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

                        approvalDataBeanX.setCurrent_page(current_page);
                        approvalDataBeanX.setFirst_page_url(first_page_url);
                        approvalDataBeanX.setFrom(from);
                        approvalDataBeanX.setLast_page(last_page);
                        approvalDataBeanX.setLast_page_url(last_page_url);
                        approvalDataBeanX.setNext_page_url(next_page_url);
                        approvalDataBeanX.setPath(path);
                        approvalDataBeanX.setPer_page(per_page);
                        approvalDataBeanX.setPrev_page_url(prev_page_url);
                        approvalDataBeanX.setTo(to);
                        approvalDataBeanX.setTotal(total);
                        approvalDataBeanX.setData(approvalDataBeans);

                        if (data != null) {
                            int currentPage = approvalDataBeanX.getCurrent_page();
                            int lastPage = approvalDataBeanX.getLast_page();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {


                                    int approvalId = jsonObject1.optInt("approvalId");
                                    int status = jsonObject1.optInt("status");
                                    String createdAt = jsonObject1.optString("createdAt");
                                    String title = jsonObject1.optString("title");
                                    String truename = jsonObject1.optString("truename");
                                    String reason = jsonObject1.optString("reason");


                                    ApprovalListResponse.DataBeanX.DataBean dataBean = new ApprovalListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);

                                    dataBean.setApprovalId(approvalId);
                                    dataBean.setStatus(status);
                                    dataBean.setCreatedAt(createdAt);
                                    dataBean.setTitle(title);
                                    dataBean.setTruename(truename);
                                    dataBean.setReason(reason);

                                    approvalDataBeans.add(dataBean);

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

                        if (approvalListResponse.getCode() == 0) {


                            Log.d("approvalDataBeans", String.valueOf(approvalDataBeans));
                            if (approvalDataBeans == null){
                                approvalListAdapter = null;
                            }else {
                                approvalListAdapter = new ApprovalListAdapter(getContext(), approvalDataBeans);
                            }

//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                mRcHospitalList.setLayoutManager(linearLayoutManager);
                            mRcApprovalList.setLinearLayout();

                            approvalListAdapter.setOnItemClickListener(new ApprovalListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int approvalId) {
                                    Intent intent = new Intent(getContext(), ApprovalDetailActivity.class);


                                    intent.putExtra("approvalId",approvalId);

                                    startActivity(intent);
                                }
                            });
                            approvalListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer, null));

                            mRcApprovalList.setAdapter(approvalListAdapter);

                            mRcApprovalList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {
                                    approvalDataBeans.clear();
                                    getResApprovalListRefresh(Api.URL + "/v1/approval/list");

                                }

                                @Override
                                public void onLoadMore() {
                                    int nextPage = approvalDataBeanX.getCurrent_page() + 1;
                                    getResApprovalListLoadMore(Api.URL + "/v1/approval/list?page=" + nextPage);

                                }
                            });




                        } else if (approvalListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), approvalListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResApprovalListLoadMore(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                final ApprovalListResponse approvalListResponse = new ApprovalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    approvalListResponse.setCode(code);
                    approvalListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        approvalListResponse.setData(approvalDataBeanX);
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

                        approvalDataBeanX.setCurrent_page(current_page);
                        approvalDataBeanX.setFirst_page_url(first_page_url);
                        approvalDataBeanX.setFrom(from);
                        approvalDataBeanX.setLast_page(last_page);
                        approvalDataBeanX.setLast_page_url(last_page_url);
                        approvalDataBeanX.setNext_page_url(next_page_url);
                        approvalDataBeanX.setPath(path);
                        approvalDataBeanX.setPer_page(per_page);
                        approvalDataBeanX.setPrev_page_url(prev_page_url);
                        approvalDataBeanX.setTo(to);
                        approvalDataBeanX.setTotal(total);
                        approvalDataBeanX.setData(approvalDataBeans);

                        if (data != null) {
                            int currentPage = approvalDataBeanX.getCurrent_page();
                            int lastPage = approvalDataBeanX.getLast_page();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {




                                    int approvalId = jsonObject1.optInt("approvalId");
                                    int status = jsonObject1.optInt("status");
                                    String createdAt = jsonObject1.optString("createdAt");
                                    String title = jsonObject1.optString("title");
                                    String truename = jsonObject1.optString("truename");

                                    String reason = jsonObject1.optString("reason");




                                    ApprovalListResponse.DataBeanX.DataBean dataBean = new ApprovalListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setApprovalId(approvalId);
                                    dataBean.setStatus(status);
                                    dataBean.setCreatedAt(createdAt);
                                    dataBean.setTitle(title);
                                    dataBean.setTruename(truename);
                                    dataBean.setReason(reason);
                                    approvalDataBeans.add(dataBean);

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

                        if (approvalListResponse.getCode() == 0) {

                            approvalListAdapter.notifyDataSetChanged();
                            mRcApprovalList.setPullLoadMoreCompleted();

                        } else if (approvalListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), approvalListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResApprovalListRefresh(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                final ApprovalListResponse approvalListResponse = new ApprovalListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    approvalListResponse.setCode(code);
                    approvalListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        approvalListResponse.setData(approvalDataBeanX);
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

                        approvalDataBeanX.setCurrent_page(current_page);
                        approvalDataBeanX.setFirst_page_url(first_page_url);
                        approvalDataBeanX.setFrom(from);
                        approvalDataBeanX.setLast_page(last_page);
                        approvalDataBeanX.setLast_page_url(last_page_url);
                        approvalDataBeanX.setNext_page_url(next_page_url);
                        approvalDataBeanX.setPath(path);
                        approvalDataBeanX.setPer_page(per_page);
                        approvalDataBeanX.setPrev_page_url(prev_page_url);
                        approvalDataBeanX.setTo(to);
                        approvalDataBeanX.setTotal(total);
                        approvalDataBeanX.setData(approvalDataBeans);

                        if (data != null) {
                            int currentPage = approvalDataBeanX.getCurrent_page();
                            int lastPage = approvalDataBeanX.getLast_page();

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {


                                    int approvalId = jsonObject1.optInt("approvalId");
                                    int status = jsonObject1.optInt("status");
                                    String createdAt = jsonObject1.optString("createdAt");
                                    String title = jsonObject1.optString("title");
                                    String truename = jsonObject1.optString("truename");

                                    String reason = jsonObject1.optString("reason");




                                    ApprovalListResponse.DataBeanX.DataBean dataBean = new ApprovalListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setApprovalId(approvalId);
                                    dataBean.setStatus(status);
                                    dataBean.setCreatedAt(createdAt);
                                    dataBean.setTitle(title);
                                    dataBean.setTruename(truename);
                                    dataBean.setReason(reason);
                                    approvalDataBeans.add(dataBean);


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

                        if (approvalListResponse.getCode() == 0) {

                            approvalListAdapter.notifyDataSetChanged();
                            mRcApprovalList.setPullLoadMoreCompleted();

                        } else if (approvalListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), approvalListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }



    public void getResNoticeList(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                cleanRefreshToSp("refresh","");
                final NoticeListResponse noticeListResponse = new NoticeListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    noticeListResponse.setCode(code);
                    noticeListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        noticeListResponse.setData(noticeDataBeanX);
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

                        noticeDataBeanX.setCurrent_page(current_page);
                        noticeDataBeanX.setFirst_page_url(first_page_url);
                        noticeDataBeanX.setFrom(from);
                        noticeDataBeanX.setLast_page(last_page);
                        noticeDataBeanX.setLast_page_url(last_page_url);
                        noticeDataBeanX.setNext_page_url(next_page_url);
                        noticeDataBeanX.setPath(path);
                        noticeDataBeanX.setPer_page(per_page);
                        noticeDataBeanX.setPrev_page_url(prev_page_url);
                        noticeDataBeanX.setTo(to);
                        noticeDataBeanX.setTotal(total);
                        noticeDataBeanX.setData(noticeDataBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = noticeDataBeanX.getCurrent_page();
                                    int lastPage = noticeDataBeanX.getLast_page();

                                    int noticeId = jsonObject1.optInt("noticeId");
                                    String createTime = jsonObject1.optString("createTime");
                                    String title = jsonObject1.optString("title");
                                    String content = jsonObject1.optString("content");
                                    int isRead = jsonObject1.optInt("isRead");



                                    NoticeListResponse.DataBeanX.DataBean dataBean = new NoticeListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setNoticeId(noticeId);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setTitle(title);
                                    dataBean.setContent(content);
                                    dataBean.setIsRead(isRead);

                                    noticeDataBeans.add(dataBean);

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

                        if (noticeListResponse.getCode() == 0) {


                            Log.d("approvalDataBeans", String.valueOf(approvalDataBeans));
                            if (noticeDataBeans == null){
                                noticeListAdapter = null;
                            }else {
                                noticeListAdapter = new NoticeListAdapter(getContext(), noticeDataBeans);
                            }

//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                mRcHospitalList.setLayoutManager(linearLayoutManager);
                            mRcNoticeList.setLinearLayout();
                            noticeListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer, null));

                            mRcNoticeList.setAdapter(noticeListAdapter);
                            noticeListAdapter.setOnItemClickListener(new NoticeListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String createTime, String title, String content,int isRead,int noticeId) {
                                    if (isRead == 2){

                                        intent.putExtra("createTime",createTime);
                                        intent.putExtra("title",title);
                                        intent.putExtra("content",content);
                                        startActivity(intent);
                                    }else if (isRead == 1){
                                        intent.putExtra("createTime",createTime);
                                        intent.putExtra("title",title);
                                        intent.putExtra("content",content);
                                        getResNoticeDetail(Api.URL+"/v1/notice/detail?noticeId="+String.valueOf(noticeId));
                                    }

                                }
                            });
                            mRcNoticeList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                @Override
                                public void onRefresh() {
                                    noticeDataBeans.clear();
                                    getResNoticeListRefresh(Api.URL + "/v1/notice/list");

                                }

                                @Override
                                public void onLoadMore() {
                                    int nextPage = noticeDataBeanX.getCurrent_page() + 1;
                                    getResNoticeListLoadMore(Api.URL + "/v1/notice/list?page=" + nextPage);
                                }
                            });




                        } else if (noticeListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), noticeListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResNoticeListLoadMore(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                cleanRefreshToSp("refresh","");
                final NoticeListResponse noticeListResponse = new NoticeListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    noticeListResponse.setCode(code);
                    noticeListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        noticeListResponse.setData(noticeDataBeanX);
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

                        noticeDataBeanX.setCurrent_page(current_page);
                        noticeDataBeanX.setFirst_page_url(first_page_url);
                        noticeDataBeanX.setFrom(from);
                        noticeDataBeanX.setLast_page(last_page);
                        noticeDataBeanX.setLast_page_url(last_page_url);
                        noticeDataBeanX.setNext_page_url(next_page_url);
                        noticeDataBeanX.setPath(path);
                        noticeDataBeanX.setPer_page(per_page);
                        noticeDataBeanX.setPrev_page_url(prev_page_url);
                        noticeDataBeanX.setTo(to);
                        noticeDataBeanX.setTotal(total);
                        noticeDataBeanX.setData(noticeDataBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = noticeDataBeanX.getCurrent_page();
                                    int lastPage = noticeDataBeanX.getLast_page();

                                    int noticeId = jsonObject1.optInt("noticeId");
                                    String createTime = jsonObject1.optString("createTime");
                                    String title = jsonObject1.optString("title");
                                    String content = jsonObject1.optString("content");
                                    int isRead = jsonObject1.optInt("isRead");



                                    NoticeListResponse.DataBeanX.DataBean dataBean = new NoticeListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setNoticeId(noticeId);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setTitle(title);
                                    dataBean.setContent(content);
                                    dataBean.setIsRead(isRead);

                                    noticeDataBeans.add(dataBean);

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

                        if (noticeListResponse.getCode() == 0) {

                            noticeListAdapter.notifyDataSetChanged();
                            mRcNoticeList.setPullLoadMoreCompleted();






                        } else if (noticeListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), noticeListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    public void getResNoticeListRefresh(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                cleanRefreshToSp("refresh","");
                final NoticeListResponse noticeListResponse = new NoticeListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    noticeListResponse.setCode(code);
                    noticeListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        noticeListResponse.setData(noticeDataBeanX);
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

                        noticeDataBeanX.setCurrent_page(current_page);
                        noticeDataBeanX.setFirst_page_url(first_page_url);
                        noticeDataBeanX.setFrom(from);
                        noticeDataBeanX.setLast_page(last_page);
                        noticeDataBeanX.setLast_page_url(last_page_url);
                        noticeDataBeanX.setNext_page_url(next_page_url);
                        noticeDataBeanX.setPath(path);
                        noticeDataBeanX.setPer_page(per_page);
                        noticeDataBeanX.setPrev_page_url(prev_page_url);
                        noticeDataBeanX.setTo(to);
                        noticeDataBeanX.setTotal(total);
                        noticeDataBeanX.setData(noticeDataBeans);

                        if (data != null) {


                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                if (jsonObject1 != null) {
                                    int currentPage = noticeDataBeanX.getCurrent_page();
                                    int lastPage = noticeDataBeanX.getLast_page();

                                    int noticeId = jsonObject1.optInt("noticeId");
                                    String createTime = jsonObject1.optString("createTime");
                                    String title = jsonObject1.optString("title");
                                    String content = jsonObject1.optString("content");
                                    int isRead = jsonObject1.optInt("isRead");



                                    NoticeListResponse.DataBeanX.DataBean dataBean = new NoticeListResponse.DataBeanX.DataBean();
                                    dataBean.setCurrent_page(currentPage);
                                    dataBean.setLast_page(lastPage);
                                    dataBean.setNoticeId(noticeId);
                                    dataBean.setCreateTime(createTime);
                                    dataBean.setTitle(title);
                                    dataBean.setContent(content);
                                    dataBean.setIsRead(isRead);

                                    noticeDataBeans.add(dataBean);

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

                        if (noticeListResponse.getCode() == 0) {

                            noticeListAdapter.notifyDataSetChanged();
                            mRcNoticeList.setPullLoadMoreCompleted();






                        } else if (noticeListResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), noticeListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }


    public void getResNoticeDetail(String url) {
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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

                final NoticeDetailResponse noticeDetailResponse = new Gson().fromJson(res, NoticeDetailResponse.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (noticeDetailResponse.getCode() == 0) {
                            saveStringToSp("refresh","refreshNoticeList");
                            startActivity(intent);
                        } else if (noticeDetailResponse.getCode() == 10009) {
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
                            ToastUtils.showTextToast2(getContext(), noticeDetailResponse.getMsg());
                        }
                    }
                });


            }
        });
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

    public String getRefreshToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("refresh", MODE_PRIVATE);
        String refresh = sp.getString("refresh", "");
        return refresh;
    }

    public void cleanRefreshToSp(String key, String val){
        SharedPreferences sp = getActivity().getSharedPreferences("refresh", MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getActivity().getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }
}
