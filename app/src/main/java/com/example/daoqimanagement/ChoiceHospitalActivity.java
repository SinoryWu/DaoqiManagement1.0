package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.AreaListAdapter;
import com.example.daoqimanagement.bean.ApprovalOpinionResponse;
import com.example.daoqimanagement.bean.AreaListResponse;
import com.example.daoqimanagement.bean.FeedBackResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.dialog.SubmitFeedbackDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ActivityCollectorPrepare;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

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

public class ChoiceHospitalActivity extends AppCompatActivity {

    private TextView mTvChoiceArea,mTvHospitalName,mTvDetail;
    private ImageView mIvChoiceArea,mIvHospitalIcon;
    private AreaListAdapter areaListAdapter;
    DialogTokenIntent dialogTokenIntent = null;

    private PopupWindow popupwindowChoiceArea;
    private View customViewChoiceArea;
    private RecyclerView mRcAreaList;
    AreaListResponse areaListResponse;
    private String provinceName, cityName, districtName;
    private String province= "0";
    private String city= "0";
    private String region = "0";
    String hospitalName = "0";
    String detail = "0";
    String headPic = "0";
    int hospitalid = 0;
    String level = "0";
    String nature = "0";
    String areaName = "0";
    private RelativeLayout mRlChoiceConfirm, mRlSubmitFeedBack,mRlChoiceHospital,mRlFinish;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorPrepare.addActivity(this);
        setContentView(R.layout.activity_choice_hospital);
        ActivityCollector.addActivity(this);
        mTvChoiceArea = findViewById(R.id.choice_hospital_tv_choice_area);
        mIvChoiceArea = findViewById(R.id.choice_hospital_iv_choice_area);
        mRlChoiceConfirm = findViewById(R.id.choice_hospital_rl_confirm);
        mRlSubmitFeedBack = findViewById(R.id.choice_hospital_rl_feedback);
        mRlChoiceHospital = findViewById(R.id.choice_hospital_rl_choice_hospital);
        mTvHospitalName= findViewById(R.id.choice_hospital_tv_hospital_name);
        mTvDetail = findViewById(R.id.choice_hospital_tv_hospital_detail);
        mRlFinish = findViewById(R.id.choice_hospital_rl_finish);
        mIvHospitalIcon = findViewById(R.id.choice_hospital_iv_hospital_icon);
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(ChoiceHospitalActivity.this);
                finish();

            }
        });
        customViewChoiceArea = getLayoutInflater().inflate(R.layout.add_prepare_choice_product_popview_item,
                null, false);
        initViewCustomView(customViewChoiceArea);

        mTvChoiceArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupwindowChoiceArea == null) {

                    getChoiceAreaProvinceRes(Api.URL + "/area/list");
                    initPopupWindowView();
                    popupwindowChoiceArea.showAsDropDown(view, 0, dip2px(ChoiceHospitalActivity.this, 5));

                    mIvChoiceArea.setImageResource(R.mipmap.login_arrow_right_up_icon);
                } else {
                    popupwindowChoiceArea = null;
                }
            }
        });




        mRlChoiceHospital.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {

                Intent intent = new Intent(ChoiceHospitalActivity.this,SearchHospitalListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("province",province);
                bundle.putString("city",city);
                bundle.putString("region",region);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });

        mRlSubmitFeedBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {

                SubmitFeedbackDialog submitFeedbackDialog = new SubmitFeedbackDialog(ChoiceHospitalActivity.this, R.style.CustomDialog);
                submitFeedbackDialog.setConfirm("提交", new SubmitFeedbackDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(SubmitFeedbackDialog dialog, String feedBack) {
                        feedBackPost(feedBack);
//                        ToastUtils.showTextToast2(ChoiceHospitalActivity.this, feedBack);
                    }
                }).show();


            }
        });

        mRlChoiceConfirm.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (mTvHospitalName.getText().equals("请选择")){
                    ToastUtils.showTextToast2(ChoiceHospitalActivity.this,"请选择医院");
                }else {
                    Intent intent = new Intent(ChoiceHospitalActivity.this,AddPrepareActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("hospitalName",hospitalName);
                    bundle.putString("nature",nature);
                    bundle.putString("level",level);
                    bundle.putString("areaName",areaName);
                    bundle.putString("headPic",headPic);
                    bundle.putInt("hospitalid",hospitalid);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if (resultCode == RESULT_OK){
                    hospitalName = data.getStringExtra("hospitalName");
                    detail = data.getStringExtra("detail");
                    headPic = data.getStringExtra("headPic");
                    hospitalid = data.getIntExtra("hospitalid",0);
                    level = data.getStringExtra("level");
                    nature = data.getStringExtra("nature");
                    areaName = data.getStringExtra("areaName");
                    mTvHospitalName.setText(hospitalName);
                    mTvDetail.setText(detail);

                    /**
                     * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
                     */
                    Glide.with(ChoiceHospitalActivity.this).load(Api.URL+headPic)
                            .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                            .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                            .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                            .into(mIvHospitalIcon);
                }
                break;
        }
    }

    public void initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图


        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindowChoiceArea = new PopupWindow(customViewChoiceArea, dip2px(this, 346), LinearLayout.LayoutParams.WRAP_CONTENT);

        popupwindowChoiceArea.setOutsideTouchable(true);
        popupwindowChoiceArea.setFocusable(true);
        customViewChoiceArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowChoiceArea != null && popupwindowChoiceArea.isShowing()) {
                    popupwindowChoiceArea.dismiss();
                    popupwindowChoiceArea = null;
                }


                return true;
            }
        });

        popupwindowChoiceArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowChoiceArea = null;
                Log.d("TAG", "onDismiss: ");
                mIvChoiceArea.setImageResource(R.mipmap.login_arrow_right_down_icon);
                areaListResponse.getData().clear();
            }
        });

    }

    public void initViewCustomView(View view) {
        mRcAreaList = view.findViewById(R.id.add_prepare_rc_product_list);
    }



    private void feedBackPost(String content) {


        HashMap<String, String> map = new HashMap<>();
        map.put("content", content);
//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/suggestion/add";
        postfeedBack(url, map);


    }

    protected void postfeedBack(String url, HashMap<String, String> map) {
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
                .addHeader("token", GetSharePerfenceSP.getToken(this))
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
                        ToastUtils.showTextToast2(ChoiceHospitalActivity.this, "网络请求失败");
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

                        FeedBackResponse feedBackResponse = gson.fromJson(res, FeedBackResponse.class);

                        if (feedBackResponse.getCode() == 0) {

                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this,"提交成功");

                        } else if (feedBackResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ChoiceHospitalActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ChoiceHospitalActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else if (feedBackResponse.getCode() == 10001) {
                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this, "请先注册账户");
                        } else {
                            String msg = feedBackResponse.getMsg();
                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    private void getChoiceAreaProvinceRes(String url) {

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
                        ToastUtils.showTextToast2(ChoiceHospitalActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                areaListResponse = gson.fromJson(res, AreaListResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (areaListResponse.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            areaListAdapter = new AreaListAdapter(ChoiceHospitalActivity.this, areaListResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChoiceHospitalActivity.this);
                            mRcAreaList.setLayoutManager(linearLayoutManager);
                            mRcAreaList.setAdapter(areaListAdapter);
                            areaListAdapter.setOnItemClickListener(new AreaListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int areaId, String areaName, int position) {
                                    provinceName = areaName;
                                    province = String.valueOf(areaId);
                                    mTvChoiceArea.setText(provinceName);
                                    getChoiceAreaCityRes(Api.URL + "/area/list?id=" + areaId);
                                }
                            });


                        } else if (areaListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ChoiceHospitalActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ChoiceHospitalActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = areaListResponse.getMsg();
                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void getChoiceAreaCityRes(String url) {

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
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .get()
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
                        ToastUtils.showTextToast2(ChoiceHospitalActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                areaListResponse = gson.fromJson(res, AreaListResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (areaListResponse.getCode() == 0) {

                            areaListAdapter.notifyDataSetChanged();
//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            areaListAdapter = new AreaListAdapter(ChoiceHospitalActivity.this, areaListResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChoiceHospitalActivity.this);
                            mRcAreaList.setLayoutManager(linearLayoutManager);
                            mRcAreaList.setAdapter(areaListAdapter);
                            areaListAdapter.setOnItemClickListener(new AreaListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int areaId, String areaName, int position) {
                                    cityName = areaName;
                                    city = String.valueOf(areaId);
                                    mTvChoiceArea.setText(provinceName + "-" + cityName);
                                    getChoiceAreaDistrictRes(Api.URL + "/area/list?id=" + areaId);
                                }
                            });


                        } else if (areaListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ChoiceHospitalActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ChoiceHospitalActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = areaListResponse.getMsg();
                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void getChoiceAreaDistrictRes(String url) {

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
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .get()
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
                        ToastUtils.showTextToast2(ChoiceHospitalActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                areaListResponse = gson.fromJson(res, AreaListResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (areaListResponse.getCode() == 0) {
                            areaListAdapter.notifyDataSetChanged();
//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            areaListAdapter = new AreaListAdapter(ChoiceHospitalActivity.this, areaListResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChoiceHospitalActivity.this);
                            mRcAreaList.setLayoutManager(linearLayoutManager);
                            mRcAreaList.setAdapter(areaListAdapter);
                            areaListAdapter.setOnItemClickListener(new AreaListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int areaId, String areaName, int position) {
                                    districtName = areaName;
                                    region = String.valueOf(areaId);
                                    popupwindowChoiceArea.dismiss();
                                    mTvChoiceArea.setText(provinceName + "-" + cityName + "-" + districtName);
                                }
                            });


                        } else if (areaListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ChoiceHospitalActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ChoiceHospitalActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = areaListResponse.getMsg();
                            ToastUtils.showTextToast2(ChoiceHospitalActivity.this, msg);
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