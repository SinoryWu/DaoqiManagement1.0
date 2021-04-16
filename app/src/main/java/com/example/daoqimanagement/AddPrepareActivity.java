package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.PrepareAddContactAdapter;
import com.example.daoqimanagement.adapter.ProductListAdapter;
import com.example.daoqimanagement.bean.AddPrepareResponse;
import com.example.daoqimanagement.bean.PrepareContactListResponse;
import com.example.daoqimanagement.bean.PrepareNumResponse;
import com.example.daoqimanagement.bean.ProductListResponse;
import com.example.daoqimanagement.dialog.AddContactDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ActivityCollectorPrepare;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPrepareActivity extends AppCompatActivity {

    private PopupWindow popupwindowChoiceProduct;
    private View customViewChoiceProduct;
    private RecyclerView mRcProductList,mRcContactList;
    ProductListAdapter productListAdapter;
    private TextView mTvChoiceProduct, mTvProductIntroduce,mTvHospitalName,mTvHospitalLevel,mTvHospitalArea,mTvPeopleNum;
    private EditText mEtDepartment,mEtReason;
    private RelativeLayout mRlAddReport,mRlFinish,mRlAddContact;
    private ImageView mIvChoice,mIvHeadPic;
    DialogTokenIntent dialogTokenIntent = null;
    String productid;
    PrepareAddContactAdapter prepareAddContactAdapter;


    List<String > nameList = new ArrayList<>();
    List<String > mobileList = new ArrayList<>();
    List<String > departmentList = new ArrayList<>();
    List<String > positionList = new ArrayList<>();
    List<String> contactList = new ArrayList<>();

    String hospitalName = "0";
    String headPic;
    String level = "0";
    String nature = "0";
    String areaName = "0";
    int hospitalid;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollectorPrepare.addActivity(this);
        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prepare);
        final Intent intent= getIntent();
        hospitalName = intent.getStringExtra("hospitalName");
        headPic = intent.getStringExtra("headPic");
        level = intent.getStringExtra("level");
        nature = intent.getStringExtra("nature");
        areaName = intent.getStringExtra("areaName");
        hospitalid = intent.getIntExtra("hospitalid",0);
        Log.d("hospitalid123", String.valueOf(hospitalid));





        customViewChoiceProduct = getLayoutInflater().inflate(R.layout.add_prepare_choice_product_popview_item,
                null, false);
        initViewCustomView(customViewChoiceProduct);
        getProductListRes(Api.URL + "/v1/product/list");






        mTvChoiceProduct = findViewById(R.id.add_prepare_tv_choice_product);
        mTvProductIntroduce = findViewById(R.id.add_prepare_tv_introduce);

        mEtDepartment= findViewById(R.id.add_prepare_et_department);
        mEtReason= findViewById(R.id.add_prepare_et_reason);
        mRlAddReport = findViewById(R.id.add_prepare_rl_report_add_button);
        mIvChoice = findViewById(R.id.add_prepare_iv_choice_product);
        mRlFinish = findViewById(R.id.add_prepare_rl_finish);
        mTvHospitalName =findViewById(R.id.add_prepare_tv_hospital_name);
        mTvHospitalLevel =findViewById(R.id.add_prepare_tv_hospital_level);
        mTvHospitalArea =findViewById(R.id.add_prepare_tv_hospital_area);
        mIvHeadPic =findViewById(R.id.add_prepare_hospital_headpic);
        mTvPeopleNum =findViewById(R.id.add_prepare_tv_people_num);
        mRlAddContact = findViewById(R.id.add_prepare_rl_contact);
        mRcContactList= findViewById(R.id.add_prepare_rc_contact);
        mTvHospitalName.setText(hospitalName);
        mTvHospitalLevel.setText(nature+"/"+level);
        mTvHospitalArea.setText(areaName);


        initRecyclePic();
        prepareAddContactAdapter.setOnItemClickItemListener(new PrepareAddContactAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {

//                prepareAddContactAdapter.removeData(position);
                prepareAddContactAdapter.removeData(position);
                contactList.remove(position);
//                dataBeans.remove(position);
//                Log.d("removeposition", String.valueOf(dataBeans.remove(position)));
            }
        });

        /**
         * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
         */
        Glide.with(AddPrepareActivity.this).load(Api.URL+headPic)
                .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                .into(mIvHeadPic);
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(AddPrepareActivity.this);
                finish();

            }
        });

        mRlAddContact.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                AddContactDialog addContactDialog = new AddContactDialog(AddPrepareActivity.this,R.style.CustomDialog);
                addContactDialog.setConfirm("提交", new AddContactDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(AddContactDialog dialog, String name, String mobile, String department, String position) {

                        prepareAddContactAdapter.addData(mobileList.size(),name,mobile,department,position);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name",name);
                            jsonObject.put("mobile",mobile);
                            jsonObject.put("department",department);
                            jsonObject.put("position",position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       String jsonStr = jsonObject.toString();
                        contactList.add(jsonStr);
//                        dataBeans.add(dataBean);



                        Log.d("contactList", String.valueOf(contactList));

//                        Log.d("asdsd", name);
//                        Log.d("asdsd", mobile);
//                        Log.d("asdsd", department);
//                        Log.d("asdsd", position);

                    }
                }).show();
            }
        });

        mTvChoiceProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (popupwindowChoiceProduct == null) {
                    initPopupWindowView();
                    popupwindowChoiceProduct.showAsDropDown(view, 0, dip2px(AddPrepareActivity.this, 3));

                    mIvChoice.setImageResource(R.mipmap.login_arrow_right_up_icon);
                } else {
                    popupwindowChoiceProduct = null;
                }
            }
        });

        mRlAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);

                Log.d("mRlAddReport", String.valueOf(hospitalid));
                String department = mEtDepartment.getText().toString();
                String reason = mEtDepartment.getText().toString();
                if (TextUtils.isEmpty(department)) {
                    ToastUtils.showTextToast2(AddPrepareActivity.this, "请输入预报备科室");
                } else if (TextUtils.isEmpty(reason)) {
                    ToastUtils.showTextToast2(AddPrepareActivity.this, "请输入理由");
                }else if (contactList == null || contactList.size()==0) {
                    ToastUtils.showTextToast2(AddPrepareActivity.this, "请添加联系人");
                }else if (!TextUtils.isEmpty(department) && !TextUtils.isEmpty(reason) && contactList != null && contactList.size() !=0){

                    Log.d("mRlAddReport", department);
                    Log.d("mRlAddReport", reason);
                    Log.d("mRlAddReport", productid);
                    Log.d("mRlAddReport", String.valueOf(contactList));

//                    loginAddReportsPost(hospitalid,productid,department,reason);
                    postResAddReport(Api.URL + "/v1/prepare/add",hospitalid,Integer.valueOf(productid),department,reason,contactList);
                }
            }
        });



    }

    private void initRecyclePic() {
        // 纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddPrepareActivity.this){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRcContactList.setLayoutManager(linearLayoutManager);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        prepareAddContactAdapter = new PrepareAddContactAdapter(AddPrepareActivity.this, nameList,mobileList,departmentList,positionList);
        mRcContactList.setAdapter(prepareAddContactAdapter);
//   添加动画
        mRcContactList.setItemAnimator(new DefaultItemAnimator());
    }

    protected void getResPrepareNum(String url) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
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
                        ToastUtils.showTextToast2(AddPrepareActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("numres", res);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        PrepareNumResponse prepareNumResponse = gson.fromJson(res, PrepareNumResponse.class);

                        if (prepareNumResponse.getCode() == 0) {
                            if (prepareNumResponse.getData().getNum() == 0){
                                mTvPeopleNum.setText("");
                            }else {
                                mTvPeopleNum.setText(String.valueOf(prepareNumResponse.getData().getNum())+"人");
                            }


                        }  else if (prepareNumResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(AddPrepareActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(AddPrepareActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = prepareNumResponse.getMsg();
                            ToastUtils.showTextToast2(AddPrepareActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    public void initViewCustomView(View view) {
        mRcProductList = view.findViewById(R.id.add_prepare_rc_product_list);
    }


    private void getProductListRes(String url) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody


        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
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
                        ToastUtils.showTextToast2(AddPrepareActivity.this, "网络请求失败");
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

                        ProductListResponse productListResponse = gson.fromJson(res, ProductListResponse.class);

                        if (productListResponse.getCode() == 0) {
                            Log.d("productListResponse", String.valueOf(productListResponse.getData().get(0).getProductName()));
                            mTvChoiceProduct.setText(productListResponse.getData().get(0).getProductName());
                            mTvProductIntroduce.setText(productListResponse.getData().get(0).getIntroduce());
                            productid = String.valueOf(productListResponse.getData().get(0).getProductid());
                            getResPrepareNum(Api.URL+"/v1/prepare/getPrepareNum?hospitalid="+hospitalid+"&productid="+productid);

                            Log.d("productId", productid);
                            Log.d("productId", String.valueOf(hospitalid));
                            productListAdapter = new ProductListAdapter(AddPrepareActivity.this, productListResponse.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddPrepareActivity.this);
                            mRcProductList.setLayoutManager(linearLayoutManager);
                            mRcProductList.setAdapter(productListAdapter);
                            productListAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String productName, int productId, String introduce, int position) {
                                    mTvProductIntroduce.setText(introduce);
                                    productid = String.valueOf(productId);
                                    mTvChoiceProduct.setText(productName);
                                    popupwindowChoiceProduct.dismiss();
                                    getResPrepareNum(Api.URL+"/v1/prepare/getPrepareNum?hospitalid="+hospitalid+"&productid="+productid);
//                                    prepareNumPost(String.valueOf(hospitalid),productid);
                                }
                            });

                        }else if (productListResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(AddPrepareActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(AddPrepareActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = productListResponse.getMsg();
                            ToastUtils.showTextToast2(AddPrepareActivity.this, msg);
                        }
                    }
                });

            }
        });
    }


    protected void postResAddReport(String url, int hospitalid, int productid, String department, String reason, List contact ) {
        //1.拿到okhttp对象

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hospitalid",hospitalid);
            jsonObject.put("productid",productid);
            jsonObject.put("department",department);
            jsonObject.put("reason",reason);
            jsonObject.put("contact",contact);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.d("contact", String.valueOf(contact));
        String jsonStr =jsonObject.toString();
        String jsonStr1 = StringEscapeUtils.unescapeJson(jsonStr);
       int index1 = jsonStr1.indexOf("[");
       int index2 = jsonStr1.indexOf("]");

        String left = jsonStr1.substring(0,index1-1);
        String mid = jsonStr1.substring(index1,index2+1);
        String right = jsonStr1.substring(index2+2,index2+3);
        String jsonStr2 = left+mid+right;
        Log.d("contact", String.valueOf(index1));
        Log.d("contact",jsonStr1);
        Log.d("contact",left);
        Log.d("contact",mid);
        Log.d("contact",right);
        Log.d("contact",jsonStr2);




        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr2);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "12")
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

                        ToastUtils.showTextToast2(AddPrepareActivity.this, "网络请求失败");
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
                        final AddPrepareResponse addPrepareResponse = gson.fromJson(res, AddPrepareResponse.class);
                        if (addPrepareResponse.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            ToastUtils.showTextToast2(AddPrepareActivity.this,"操作成功");
                            saveStringToSp("refresh","refreshHospitalList");
                            ActivityCollectorPrepare.finishAll();

                        }  else if (addPrepareResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(AddPrepareActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(AddPrepareActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = addPrepareResponse.getMsg();
                            ToastUtils.showTextToast2(AddPrepareActivity.this, msg);
                        }

                    }
                });


            }
        });


    }
    private void loginAddReportsPost(int hospitalid, String productid, String department, String reason) {


        HashMap<String, String> map = new HashMap<>();
        map.put("hospitalid", String.valueOf(hospitalid));//18158188052
        map.put("productid", productid);//111
        map.put("department", department);
        map.put("reason", reason);
//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/prepare/add";
        postAddReportRes(url, map);


    }
    private void postAddReportRes(String url, HashMap<String, String> map) {

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:", String.valueOf(params));
        Set<String> keys = map.keySet();
        for (String key : keys) {
            params.put(key, map.get(key));

        }

        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", getTokenToSp("token",""))
                .addHeader("uid", getUidToSp("uid",""))
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")

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
                        ToastUtils.showTextToast2(AddPrepareActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                Gson gson = new Gson();
                final AddPrepareResponse addPrepareResponse = gson.fromJson(res, AddPrepareResponse.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (addPrepareResponse.getCode() == 0) {

//                            mTvProgressListLoadComplete.setVisibility(View.VISIBLE);
                            ToastUtils.showTextToast2(AddPrepareActivity.this,"操作成功");
                            saveStringToSp("refresh","refreshHospitalList");
                            ActivityCollectorPrepare.finishAll();

                        }  else if (addPrepareResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(AddPrepareActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(AddPrepareActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();

                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = addPrepareResponse.getMsg();
                            ToastUtils.showTextToast2(AddPrepareActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    public void initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图


        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindowChoiceProduct = new PopupWindow(customViewChoiceProduct, dip2px(this, 346), LinearLayout.LayoutParams.WRAP_CONTENT);

        popupwindowChoiceProduct.setOutsideTouchable(true);
        popupwindowChoiceProduct.setFocusable(true);
        customViewChoiceProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowChoiceProduct != null && popupwindowChoiceProduct.isShowing()) {
                    popupwindowChoiceProduct.dismiss();
                    popupwindowChoiceProduct = null;


                }


                return true;
            }
        });

        popupwindowChoiceProduct.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowChoiceProduct = null;
                Log.d("TAG", "onDismiss: ");
                mIvChoice.setImageResource(R.mipmap.login_arrow_right_down_icon);
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

    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }


}