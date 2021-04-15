package com.example.daoqimanagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daoqimanagement.ChoiceHospitalActivity;
import com.example.daoqimanagement.LoginActivity;
import com.example.daoqimanagement.PrepareDetailForUserActivity;
import com.example.daoqimanagement.ProgressDetailActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.UpDateProgressActivity;
import com.example.daoqimanagement.adapter.HomeFragmentHospitalListAdapter;
import com.example.daoqimanagement.adapter.HomeFragmentProgressListAdapter;
import com.example.daoqimanagement.bean.HomeFragmentHospitalPrepareListResponse;
import com.example.daoqimanagement.bean.HomeFragmentProgressListResponse;
import com.example.daoqimanagement.bean.LoginResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout mRlAll,mRlHospital,mRlProgress,mRlBill,mRlAddReport,mRlAddProgress,mRlHospitalList,mRlHospitalListGone;
    private Button mBtnAll,mBtnHospital,mBtnProgress,mBtnBill;
    private PullLoadMoreRecyclerView mRcHospitalList;
    private PullLoadMoreRecyclerView mRcProgressList;
    private HomeFragmentHospitalListAdapter homeFragmentHospitalListAdapter;
    private HomeFragmentProgressListAdapter homeFragmentProgressListAdapter;

    private TextView mTvProgressListLoadComplete;
    HomeFragmentProgressListResponse.DataBeanX progressDataBeanX = new HomeFragmentProgressListResponse.DataBeanX();
    public List<HomeFragmentProgressListResponse.DataBeanX.DataBean> progressDataBeans = new ArrayList<>();

    HomeFragmentHospitalPrepareListResponse.DataBeanX hospitalPrepareListDataBeanX = new HomeFragmentHospitalPrepareListResponse.DataBeanX();
    public List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> hospitalPrepareListDataBeans = new ArrayList<>();
    String refresh = "0";
    DialogTokenIntent dialogTokenIntent = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("onCreateView", "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        intiView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onCreateView", "onResume: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onCreateView", "onStart: ");
        if (getRefreshToSp("refresh","").length() >0 || getRefreshToSp("refresh","")!= null || !TextUtils.isEmpty(getRefreshToSp("refresh",""))){
            String refresh = getRefreshToSp("refresh","");
            if (refresh.equals("refreshHospitalList")){
                hospitalPrepareListDataBeans.clear();
                if (GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                    getPrepareHospitalList(Api.URL+"/v1/prepare/listForUser");
                }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                    if (!GetSharePerfenceSP.getType(getContext()).equals("4")){
                        mRlHospitalListGone.setVisibility(View.GONE);
                        mRlHospitalList.setVisibility(View.VISIBLE);
                        if (GetSharePerfenceSP.getType(getContext()).equals("8")){
                            getPrepareHospitalList(Api.URL+"/v1/prepare/listForOpe");
                        }else {
                            getPrepareHospitalList(Api.URL+"/v1/prepare/listForAdmin");
                        }
                    }else {

                        mRlHospitalListGone.setVisibility(View.VISIBLE);
                        mRlHospitalList.setVisibility(View.GONE);
                    }

                }

            }else if (refresh.equals("refreshProgressList")){
                progressDataBeans.clear();
                postResProgress(Api.URL+"/v1/schedule/list");
            }
        }
    }



    public void intiView(View view){
        mBtnAll = view.findViewById(R.id.fragment_home_tab_btn_all);
        mBtnHospital = view.findViewById(R.id.fragment_home_tab_btn_hospital);
        mBtnProgress = view.findViewById(R.id.fragment_home_tab_btn_progress);
        mBtnBill = view.findViewById(R.id.fragment_home_tab_btn_bill);
        mRcHospitalList = view.findViewById(R.id.fragment_home_rc_hospital_list);
        mRcProgressList= view.findViewById(R.id.fragment_home_rc_progress_list);
        mRlAll = view.findViewById(R.id.fragment_home_rl_all);
        mRlHospital = view.findViewById(R.id.fragment_home_rl_hospital);
        mRlProgress = view.findViewById(R.id.fragment_home_rl_progress);
        mRlBill = view.findViewById(R.id.fragment_home_rl_bill);
        mRlAddReport = view.findViewById(R.id.fragment_home_hospital_rl_add_button);
        mRlAddProgress = view.findViewById(R.id.fragment_home_rl_progress_add_button);
        mRlHospitalList = view.findViewById(R.id.fragment_home_hospital_rl_hospital_list);
        mRlHospitalListGone = view.findViewById(R.id.fragment_home_hospital_rl_hospital_list_gone);



        mRlAll.setVisibility(View.GONE);
        mRlHospital.setVisibility(View.GONE);
        mRlProgress.setVisibility(View.GONE);
        mRlBill.setVisibility(View.GONE);

        mBtnAll.setOnClickListener(this);
        mBtnHospital.setOnClickListener(this);
        mBtnProgress.setOnClickListener(this);
        mBtnBill.setOnClickListener(this);
        mBtnHospital.performClick();

       mRlAddReport.setOnClickListener(this);
        mRlAddProgress.setOnClickListener(this);
        mRlAddReport.setVisibility(View.GONE);
        mRlAddProgress.setVisibility(View.GONE);
        if(GetSharePerfenceSP.getUserType(getContext()).equals("2")){
            mRlAddReport.setVisibility(View.GONE);

        }else if(GetSharePerfenceSP.getUserType(getContext()).equals("1")){
            mRlAddReport.setVisibility(View.VISIBLE);

        }




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_home_tab_btn_all:
                mRlAll.setVisibility(View.VISIBLE);
                mRlHospital.setVisibility(View.GONE);
                mRlProgress.setVisibility(View.GONE);
                mRlBill.setVisibility(View.GONE);
                mBtnAll.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnAll.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnHospital.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnHospital.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnProgress.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnProgress.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnBill.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnBill.setTextColor(Color.parseColor("#A6BCD0"));

                mBtnAll.setEnabled(false);
                mBtnHospital.setEnabled(true);
                mBtnProgress.setEnabled(true);
                mBtnBill.setEnabled(true);
                break;
            case R.id.fragment_home_tab_btn_hospital:
                mRlAll.setVisibility(View.GONE);
                mRlHospital.setVisibility(View.VISIBLE);
                mRlProgress.setVisibility(View.GONE);
                mRlBill.setVisibility(View.GONE);
                mBtnAll.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnAll.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnHospital.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnHospital.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnProgress.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnProgress.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnBill.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnBill.setTextColor(Color.parseColor("#A6BCD0"));

                Log.d("token", GetSharePerfenceSP.getToken(getContext()));
                Log.d("token", GetSharePerfenceSP.getUid(getContext()));
                Log.d("token", GetSharePerfenceSP.getUserType(getContext()));
                Log.d("token", GetSharePerfenceSP.getType(getContext()));


                hospitalPrepareListDataBeans.clear();
                if (GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                    getPrepareHospitalList(Api.URL+"/v1/prepare/listForUser");
                }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                    if (GetSharePerfenceSP.getType(getContext()).equals("4")){
                        mRlHospitalListGone.setVisibility(View.VISIBLE);
                        mRlHospitalList.setVisibility(View.GONE);
                    }else {
                        mRlHospitalListGone.setVisibility(View.GONE);
                        mRlHospitalList.setVisibility(View.VISIBLE);
                        if (GetSharePerfenceSP.getType(getContext()).equals("8")){
                            getPrepareHospitalList(Api.URL+"/v1/prepare/listForOpe");
                        }
                        getPrepareHospitalList(Api.URL+"/v1/prepare/listForAdmin");
                    }

                }

                mBtnAll.setEnabled(true);
                mBtnHospital.setEnabled(false);
                mBtnProgress.setEnabled(true);
                mBtnBill.setEnabled(true);
                mRlAddProgress.setVisibility(View.GONE);
                if(GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                    mRlAddReport.setVisibility(View.GONE);
                }else if(GetSharePerfenceSP.getUserType(getContext()).equals("1")){

                    mRlAddReport.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fragment_home_tab_btn_progress:
                mRlAll.setVisibility(View.GONE);
                mRlHospital.setVisibility(View.GONE);
                mRlProgress.setVisibility(View.VISIBLE);
                mRlBill.setVisibility(View.GONE);
                mBtnAll.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnAll.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnHospital.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnHospital.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnProgress.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnProgress.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnBill.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnBill.setTextColor(Color.parseColor("#A6BCD0"));
                progressDataBeans.clear();
                postResProgress(Api.URL+"/v1/schedule/list");
                mBtnAll.setEnabled(true);
                mBtnHospital.setEnabled(true);
                mBtnProgress.setEnabled(false);
                mBtnBill.setEnabled(true);
                if(GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                    mRlAddProgress.setVisibility(View.VISIBLE);
                }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                    mRlAddProgress.setVisibility(View.GONE);
                }

                mRlAddReport.setVisibility(View.GONE);
                break;
            case R.id.fragment_home_tab_btn_bill:
                mRlAll.setVisibility(View.GONE);
                mRlHospital.setVisibility(View.GONE);
                mRlProgress.setVisibility(View.GONE);
                mRlBill.setVisibility(View.VISIBLE);
                mBtnAll.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnAll.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnHospital.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnHospital.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnProgress.setBackgroundResource(R.drawable.fragment_home_tab_button_background_normal);
                mBtnProgress.setTextColor(Color.parseColor("#A6BCD0"));
                mBtnBill.setBackgroundResource(R.drawable.fragment_home_tab_button_background_focus);
                mBtnBill.setTextColor(Color.parseColor("#FFFFFF"));
                mBtnAll.setEnabled(true);
                mBtnHospital.setEnabled(true);
                mBtnProgress.setEnabled(true);
                mBtnBill.setEnabled(false);
                break;
            case R.id.fragment_home_hospital_rl_add_button:
                Intent intentChoiceHospital = new Intent(getContext(), ChoiceHospitalActivity.class);
                startActivity(intentChoiceHospital);
                break;
            case R.id.fragment_home_rl_progress_add_button:
                Intent intentUpDataProgress = new Intent(getContext(), UpDateProgressActivity.class);
                startActivity(intentUpDataProgress);
                break;

        }
    }



    public void getPrepareHospitalList(String url){
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//网络请求的网址
                .get()//默认是GET请求，可省略，也可以写其他的
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token","710807d5148cfe4c0ef02f50884a4b42")
//                .addHeader("uid","8")
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
                Log.d("123asdase1", res);

                cleanRefreshToSp("refresh","");
                final HomeFragmentHospitalPrepareListResponse homeFragmentHospitalPrepareListResponse = new HomeFragmentHospitalPrepareListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");




                        //第一层封装
                        homeFragmentHospitalPrepareListResponse.setCode(code);
                        homeFragmentHospitalPrepareListResponse.setMsg(msg);
//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();



                    if (datax != null){
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
                                    int delay = jsonObject1.optInt("delay");


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
                                    dataBean.setDelay(delay);
                                    hospitalPrepareListDataBeans.add(dataBean);

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



                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){


                                homeFragmentHospitalListAdapter = new HomeFragmentHospitalListAdapter(getContext(),hospitalPrepareListDataBeans);
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                mRcHospitalList.setLayoutManager(linearLayoutManager);
                                mRcHospitalList.setLinearLayout();
                                homeFragmentHospitalListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer,null));

                                mRcHospitalList.setAdapter(homeFragmentHospitalListAdapter);
                                homeFragmentHospitalListAdapter.setOnItemClickListener(new HomeFragmentHospitalListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int prepareId, String hospitalName, int position) {
                                        String prepareid = String.valueOf(prepareId);
                                        Intent intent = new Intent(getContext(), PrepareDetailForUserActivity.class);
                                        intent.putExtra("prepareId",prepareid);
                                        intent.putExtra("hospitalName",hospitalName);
                                        startActivity(intent);
                                    }
                                });
                                mRcHospitalList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                    @Override
                                    public void onRefresh() {
                                        hospitalPrepareListDataBeans.clear();
//                                    getPrepareHospitalListRefresh(Api.URL+"/v1/prepare/listForUser");
                                        if (GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                                            getPrepareHospitalListRefresh(Api.URL+"/v1/prepare/listForUser");
                                        }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                                            getPrepareHospitalListRefresh(Api.URL+"/v1/prepare/listForAdmin");
                                        }
                                    }

                                    @Override
                                    public void onLoadMore() {

                                        int nextPage = hospitalPrepareListDataBeanX.getCurrent_page()+1;

//                                    getPrepareHospitalListLoadMore(Api.URL+"/v1/prepare/listForUser?page="+nextPage);
                                        if (GetSharePerfenceSP.getUserType(getContext()).equals("1")){
                                            getPrepareHospitalListLoadMore(Api.URL+"/v1/prepare/listForUser?page="+nextPage);
                                        }else if (GetSharePerfenceSP.getUserType(getContext()).equals("2")){
                                            getPrepareHospitalListLoadMore(Api.URL+"/v1/prepare/listForAdmin?page="+nextPage);
                                        }

                                    }
                                });






                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){

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
                            }else {

                            }
                        }else {

                            ToastUtils.showTextToast2(getContext(),homeFragmentHospitalPrepareListResponse.getMsg());
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
                                    int delay = jsonObject1.optInt("delay");

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
                                    dataBean.setDelay(delay);
                                    hospitalPrepareListDataBeans.add(dataBean);

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

                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){
                            Log.d("nextpage", String.valueOf(homeFragmentHospitalPrepareListResponse));
                            homeFragmentHospitalListAdapter.notifyDataSetChanged();
                            mRcHospitalList.setPullLoadMoreCompleted();

                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
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
                        }else {
                            ToastUtils.showTextToast2(getContext(),homeFragmentHospitalPrepareListResponse.getMsg());
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
                                    int delay = jsonObject1.optInt("delay");


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
                                    dataBean.setDelay(delay);
                                    hospitalPrepareListDataBeans.add(dataBean);

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

                        if (homeFragmentHospitalPrepareListResponse.getCode() == 0){

                            homeFragmentHospitalListAdapter.notifyDataSetChanged();
                            mRcHospitalList.setPullLoadMoreCompleted();

                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
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
                        }else {
                            ToastUtils.showTextToast2(getContext(),homeFragmentHospitalPrepareListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }





    protected void postResProgress(String url) {
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
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
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

                final HomeFragmentProgressListResponse homeFragmentProgressListResponse = new HomeFragmentProgressListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");

                    homeFragmentProgressListResponse.setCode(code);
                    homeFragmentProgressListResponse.setMsg(msg);
                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentProgressListResponse.setData(progressDataBeanX);
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

                        progressDataBeanX.setCurrent_page(current_page);
                        progressDataBeanX.setFirst_page_url(first_page_url);
                        progressDataBeanX.setFrom(from);
                        progressDataBeanX.setLast_page(last_page);
                        progressDataBeanX.setLast_page_url(last_page_url);
                        progressDataBeanX.setNext_page_url(next_page_url);
                        progressDataBeanX.setPath(path);
                        progressDataBeanX.setPer_page(per_page);
                        progressDataBeanX.setPrev_page_url(prev_page_url);
                        progressDataBeanX.setTo(to);
                        progressDataBeanX.setTotal(total);
                        progressDataBeanX.setData(progressDataBeans);



                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {
                                int currentPage = progressDataBeanX.getCurrent_page();
                                int lastPage = progressDataBeanX.getLast_page();
                                int scheduleId = jsonObject1.optInt("scheduleId");
                                String created_at = jsonObject1.optString("created_at");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String truename = jsonObject1.optString("truename");
                                String desc = jsonObject1.optString("desc");




                                HomeFragmentProgressListResponse.DataBeanX.DataBean dataBean = new HomeFragmentProgressListResponse.DataBeanX.DataBean();
                                dataBean.setScheduleId(scheduleId);
                                dataBean.setCreated_at(created_at);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setTruename(truename);
                                dataBean.setDesc(desc);
                                dataBean.setCurrent_page(currentPage);
                                dataBean.setLast_page(lastPage);
                                progressDataBeans.add(dataBean);

                            }

                        }
                    }
                    Log.d("dataBeans", String.valueOf(progressDataBeans));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);



//                        homeFragmentProgressListResponse1.getData().getData().add()

                        if (homeFragmentProgressListResponse.getCode() == 0) {

                                homeFragmentProgressListAdapter = new HomeFragmentProgressListAdapter(getContext(), progressDataBeans);

//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                                mRcProgressList.setLayoutManager(linearLayoutManager);
                                mRcProgressList.setLinearLayout();
                            homeFragmentProgressListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer,null));
//                                mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer,mRcHospitalList,false);
//                                mRcProgressList.addFooterView(mFooterView);
                            mRcProgressList.setAdapter(homeFragmentProgressListAdapter);

                            homeFragmentProgressListAdapter.setOnItemClickListener(new HomeFragmentProgressListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int scheduleId, int position) {
                                    Intent intent = new Intent(getContext(), ProgressDetailActivity.class);
                                    intent.putExtra("scheduleId",String.valueOf(scheduleId));
                                    startActivity(intent);
                                }
                            });

                                mRcProgressList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                                    @Override
                                    public void onRefresh() {
                                        progressDataBeans.clear();
                                        postResProgressRefresh(Api.URL+"/v1/schedule/list");
                                    }

                                    @Override
                                    public void onLoadMore() {
                                        int nextPage = progressDataBeanX.getCurrent_page()+1;
                                        Log.d("pagenext", String.valueOf(nextPage));
                                        postResProgressLoadMore(Api.URL+"/v1/schedule/list?page="+nextPage);

                                    }
                                });




                        }else if (homeFragmentProgressListResponse.getCode() == 10009){
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
                            String msg = homeFragmentProgressListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }

//    protected void postResProgress(String url) {
//        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//
//        //2.构造request
//        //2.1构造requestbody
//
////        HashMap<String, Object> params = new HashMap<String, Object>();
////
////        Log.e("params:", String.valueOf(params));
////        Set<String> keys = map.keySet();
////        for (String key : keys) {
////            params.put(key, map.get(key));
////
////        }
////        JSONObject jsonObject = new JSONObject(params);
//        JSONObject jsonObject = new JSONObject();
//        String jsonStr = jsonObject.toString();
//
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
//                .post(requestBodyJson)
//                .build();
//        //3.将request封装为call
//        Call call = okHttpClient.newCall(request);
//        L.e(String.valueOf(call));
//        //4.执行call
////        同步执行
////        Response response = call.execute();
//
//        //异步执行
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                L.e("OnFailure   " + e.getMessage());
//                e.printStackTrace();
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                L.e("OnResponse");
//                final String res = response.body().string();
//                L.e(res);
//                //封装java对象
//
////                final HomeFragmentProgressListResponse homeFragmentProgressListResponse = new HomeFragmentProgressListResponse();
////
////                try {
////                    JSONObject jsonObject = new JSONObject(res);
////                    //第一层解析
////                    int code = jsonObject.optInt("code");
////                    String msg = jsonObject.optString("msg");
////                    JSONObject datax = jsonObject.optJSONObject("data");
////
////
////                    if (datax != null) {
////                        //第一层封装
////                        homeFragmentProgressListResponse.setCode(code);
////                        homeFragmentProgressListResponse.setMsg(msg);
//////                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();
////
////                        homeFragmentProgressListResponse.setData(progressDataBeanX);
////                        //第二层解析
////
////
////                        int current_page = datax.optInt("current_page");
////                        String first_page_url = datax.optString("first_page_url");
////                        int from =  datax.optInt("from");
////                        int last_page =  datax.optInt("last_page");
////                        String last_page_url =  datax.optString("last_page_url");
////                        String next_page_url =  datax.optString("next_page_url");
////                        String path =  datax.optString("path");
////                        int per_page =  datax.optInt("per_page");
////                        String prev_page_url =  datax.optString("prev_page_url");
////                        int  to =  datax.optInt("to");
////                        int  total =  datax.optInt("total");
////                        JSONArray data = datax.optJSONArray("data");
////
////                        progressDataBeanX.setCurrent_page(current_page);
////                        progressDataBeanX.setFirst_page_url(first_page_url);
////                        progressDataBeanX.setFrom(from);
////                        progressDataBeanX.setLast_page(last_page);
////                        progressDataBeanX.setLast_page_url(last_page_url);
////                        progressDataBeanX.setNext_page_url(next_page_url);
////                        progressDataBeanX.setPath(path);
////                        progressDataBeanX.setPer_page(per_page);
////                        progressDataBeanX.setPrev_page_url(prev_page_url);
////                        progressDataBeanX.setTo(to);
////                        progressDataBeanX.setTotal(total);
////                        progressDataBeanX.setData(progressDataBeans);
////
////                        int currentPage = progressDataBeanX.getCurrent_page();
////                        int lastPage = progressDataBeanX.getLast_page();
////
////                        for (int i = 0; i < data.length(); i++) {
////                            JSONObject jsonObject1 = data.getJSONObject(i);
////                            if (jsonObject1 != null) {
////
////                                int scheduleId = jsonObject1.optInt("scheduleId");
////                                String created_at = jsonObject1.optString("created_at");
////                                String hospitalName = jsonObject1.optString("hospitalName");
////                                String truename = jsonObject1.optString("truename");
////                                String desc = jsonObject1.optString("desc");
////
////
////
////
////                                HomeFragmentProgressListResponse.DataBeanX.DataBean dataBean = new HomeFragmentProgressListResponse.DataBeanX.DataBean();
////                                dataBean.setScheduleId(scheduleId);
////                                dataBean.setCreated_at(created_at);
////                                dataBean.setHospitalName(hospitalName);
////                                dataBean.setTruename(truename);
////                                dataBean.setDesc(desc);
////                                dataBean.setCurrent_page(currentPage);
////                                dataBean.setLast_page(lastPage);
////                                progressDataBeans.add(dataBean);
////
////                            }
////
////                        }
////                    }
////                    Log.d("dataBeans", String.valueOf(progressDataBeans));
////
////
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        L.e(res);
//
//
//                         homeFragmentProgressListResponse = gson.fromJson(res,HomeFragmentProgressListResponse.class);
//                        Log.d("res123123", String.valueOf(homeFragmentProgressListResponse.getData().getLast_page()));
////                        homeFragmentProgressListResponse1.getData().getData().add()
//                        progressDataBeans = homeFragmentProgressListResponse.getData().getData();
//                        if (homeFragmentProgressListResponse.getCode() == 0) {
//
//                            homeFragmentProgressListAdapter = new HomeFragmentProgressListAdapter(getContext(), homeFragmentProgressListResponse.getData().getData());
//
////                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
////                                mRcProgressList.setLayoutManager(linearLayoutManager);
//                            mRcProgressList.setLinearLayout();
//                            homeFragmentProgressListAdapter.addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer,null));
////                                mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.home_hopital_list_layout_footer,mRcHospitalList,false);
////                                mRcProgressList.addFooterView(mFooterView);
//                            mRcProgressList.setAdapter(homeFragmentProgressListAdapter);
//
//                            mRcProgressList.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
//                                @Override
//                                public void onRefresh() {
//                                    mRcProgressList.setPullLoadMoreCompleted();
//                                }
//
//                                @Override
//                                public void onLoadMore() {
//                                    if (homeFragmentProgressListResponse.getData().getCurrent_page() < homeFragmentProgressListResponse.getData().getLast_page()){
//                                        int nextPage = homeFragmentProgressListResponse.getData().getCurrent_page()+1;
//                                        postResProgressLoadMore(Api.URL+"/v1/schedule/list?page="+nextPage);
//                                    }else {
//                                        mRcProgressList.setPullLoadMoreCompleted();
//
//                                    }
//
//                                }
//                            });
//
//
//
//
//                        } else {
//                            String msg = homeFragmentProgressListResponse.getMsg();
//                            ToastUtils.showTextToast2(getContext(), msg);
//                        }
//                    }
//                });
//
//            }
//        });
//
//    }
//
//    protected void postResProgressLoadMore(String url) {
//        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//
//        //2.构造request
//        //2.1构造requestbody
//
////        HashMap<String, Object> params = new HashMap<String, Object>();
////
////        Log.e("params:", String.valueOf(params));
////        Set<String> keys = map.keySet();
////        for (String key : keys) {
////            params.put(key, map.get(key));
////
////        }
////        JSONObject jsonObject = new JSONObject(params);
//        JSONObject jsonObject = new JSONObject();
//        String jsonStr = jsonObject.toString();
//
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
//                .post(requestBodyJson)
//                .build();
//        //3.将request封装为call
//        Call call = okHttpClient.newCall(request);
//        L.e(String.valueOf(call));
//        //4.执行call
////        同步执行
////        Response response = call.execute();
//
//        //异步执行
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                L.e("OnFailure   " + e.getMessage());
//                e.printStackTrace();
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtils.showTextToast2(getContext(), "网络请求失败");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                L.e("OnResponse");
//                final String res = response.body().string();
//                L.e(res);
//                //封装java对象
//
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        L.e(res);
//
//
//                        homeFragmentProgressListResponse = gson.fromJson(res,HomeFragmentProgressListResponse.class);
//                        Log.d("res5232234", String.valueOf(homeFragmentProgressListResponse.getData()));
//
//
//                        if (homeFragmentProgressListResponse.getCode() == 0) {
//                            homeFragmentProgressListAdapter.notifyDataSetChanged();
//                            mRcProgressList.setPullLoadMoreCompleted();
////                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
//
//
//
//
//
//                        } else {
//                            String msg = homeFragmentProgressListResponse.getMsg();
//                            ToastUtils.showTextToast2(getContext(), msg);
//                        }
//                    }
//                });
//
//            }
//        });
//
//    }

    protected void postResProgressRefresh(String url) {
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
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
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

                final HomeFragmentProgressListResponse homeFragmentProgressListResponse = new HomeFragmentProgressListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    homeFragmentProgressListResponse.setCode(code);
                    homeFragmentProgressListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentProgressListResponse.setData(progressDataBeanX);
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

                        progressDataBeanX.setCurrent_page(current_page);
                        progressDataBeanX.setFirst_page_url(first_page_url);
                        progressDataBeanX.setFrom(from);
                        progressDataBeanX.setLast_page(last_page);
                        progressDataBeanX.setLast_page_url(last_page_url);
                        progressDataBeanX.setNext_page_url(next_page_url);
                        progressDataBeanX.setPath(path);
                        progressDataBeanX.setPer_page(per_page);
                        progressDataBeanX.setPrev_page_url(prev_page_url);
                        progressDataBeanX.setTo(to);
                        progressDataBeanX.setTotal(total);
                        progressDataBeanX.setData(progressDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {
                                int currentPage = progressDataBeanX.getCurrent_page();
                                int lastPage = progressDataBeanX.getLast_page();
                                int scheduleId = jsonObject1.optInt("scheduleId");
                                String created_at = jsonObject1.optString("created_at");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String truename = jsonObject1.optString("truename");
                                String desc = jsonObject1.optString("desc");


                                HomeFragmentProgressListResponse.DataBeanX.DataBean dataBean = new HomeFragmentProgressListResponse.DataBeanX.DataBean();
                                dataBean.setScheduleId(scheduleId);
                                dataBean.setCreated_at(created_at);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setTruename(truename);
                                dataBean.setDesc(desc);
                                dataBean.setLast_page(lastPage);
                                dataBean.setCurrent_page(currentPage);
                                progressDataBeans.add(dataBean);

                            }
                            Log.d("dataBeans", String.valueOf(progressDataBeans));

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);
                        Log.d("res", res);



                        if (homeFragmentProgressListResponse.getCode() == 0) {
                            homeFragmentProgressListAdapter.notifyDataSetChanged();
                            mRcProgressList.setPullLoadMoreCompleted();
//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);





                        }else if (homeFragmentProgressListResponse.getCode() == 10009){
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
                            String msg = homeFragmentProgressListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }
    protected void postResProgressLoadMore(String url) {
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
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
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

                final HomeFragmentProgressListResponse homeFragmentProgressListResponse = new HomeFragmentProgressListResponse();

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    //第一层解析
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    JSONObject datax = jsonObject.optJSONObject("data");
                    homeFragmentProgressListResponse.setCode(code);
                    homeFragmentProgressListResponse.setMsg(msg);

                    if (datax != null) {
                        //第一层封装

//                    List<EquipmentResponse.DataBean> dataBeans = new ArrayList<>();

                        homeFragmentProgressListResponse.setData(progressDataBeanX);
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

                        progressDataBeanX.setCurrent_page(current_page);
                        progressDataBeanX.setFirst_page_url(first_page_url);
                        progressDataBeanX.setFrom(from);
                        progressDataBeanX.setLast_page(last_page);
                        progressDataBeanX.setLast_page_url(last_page_url);
                        progressDataBeanX.setNext_page_url(next_page_url);
                        progressDataBeanX.setPath(path);
                        progressDataBeanX.setPer_page(per_page);
                        progressDataBeanX.setPrev_page_url(prev_page_url);
                        progressDataBeanX.setTo(to);
                        progressDataBeanX.setTotal(total);
                        progressDataBeanX.setData(progressDataBeans);

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            if (jsonObject1 != null) {
                                int currentPage = progressDataBeanX.getCurrent_page();
                                int lastPage = progressDataBeanX.getLast_page();
                                int scheduleId = jsonObject1.optInt("scheduleId");
                                String created_at = jsonObject1.optString("created_at");
                                String hospitalName = jsonObject1.optString("hospitalName");
                                String truename = jsonObject1.optString("truename");
                                String desc = jsonObject1.optString("desc");


                                HomeFragmentProgressListResponse.DataBeanX.DataBean dataBean = new HomeFragmentProgressListResponse.DataBeanX.DataBean();
                                dataBean.setScheduleId(scheduleId);
                                dataBean.setCreated_at(created_at);
                                dataBean.setHospitalName(hospitalName);
                                dataBean.setTruename(truename);
                                dataBean.setDesc(desc);
                                dataBean.setLast_page(lastPage);
                                dataBean.setCurrent_page(currentPage);
                                progressDataBeans.add(dataBean);

                            }
                            Log.d("dataBeans", String.valueOf(progressDataBeans));

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);
                        Log.d("res", res);



                        if (homeFragmentProgressListResponse.getCode() == 0) {
                            homeFragmentProgressListAdapter.notifyDataSetChanged();
                            mRcProgressList.setPullLoadMoreCompleted();
//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);





                        } else if (homeFragmentProgressListResponse.getCode() == 10009){
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
                        }else {
                            String msg = homeFragmentProgressListResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
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

}
