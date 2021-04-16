package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.SearPrepareListAdapter;
import com.example.daoqimanagement.adapter.UpDataProgressAddFileAdapter;
import com.example.daoqimanagement.adapter.UpDataProgressAddPicAdapter;
import com.example.daoqimanagement.bean.HomeFragmentHospitalPrepareListResponse;
import com.example.daoqimanagement.bean.UpDataProgressResponse;
import com.example.daoqimanagement.bean.UpLoadFileResponse;
import com.example.daoqimanagement.bean.UploadPhotoDateResponse;
import com.example.daoqimanagement.bean.UploadPhotoResponse;
import com.example.daoqimanagement.dialog.CheckPhotoBitmapDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class UpDateProgressActivity extends AppCompatActivity {
    private SearPrepareListAdapter searPrepareListAdapter;
    private RelativeLayout mRlChoiceHospital,mRlAddPic,mRlAddFile,mRlAddFileNone,mRlUpdate,mRlFinish;
    private TextView mTvHospitalName;
    private ImageView mIvHeadPic;
    private EditText mEtRemarks,mEtNeedHelp,mEtDesc;
    int prepareId = 0;
    String hospitalName = "0";
    String hospitalHeadPic = "0";
    UpDataProgressAddPicAdapter upDataProgressAddPicAdapter;
    private RecyclerView mRcAddPic,mRcAddFile;
    private List<Bitmap> listpic = new ArrayList<Bitmap>();
    private List<String> listfile = new ArrayList<String>();
    Bitmap bitmapChoose,bitmapCamera;
    private RecyclerView mRcHospitalList;
    List<String> listPicString = new ArrayList<>();
    List<String> listFileString = new ArrayList<>();
    HomeFragmentHospitalPrepareListResponse.DataBeanX hospitalPrepareListDataBeanX = new HomeFragmentHospitalPrepareListResponse.DataBeanX();
    List<HomeFragmentHospitalPrepareListResponse.DataBeanX.DataBean> hospitalPrepareListDataBeans = new ArrayList<>();
    UpDataProgressAddFileAdapter upDataProgressAddFileAdapter;

    DialogTokenIntent dialogTokenIntent = null;
    File photoFile ;
    String picid;
    Uri picbaseUri,chooseUri;
    String upLoadFileName;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //申请访问相册权限的requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE = 0x00000013;
    //申请访问文件权限的requestCode
    private static final int PERMISSION_FILE_REQUEST_CODE = 0x00000014;
    //成功调用相机后返回的requestCode
    private static final int CAMERA_REQUEST_CODE = 2;
    //成功打开相册后返回的requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 3;
    //成功打开文件管理器后返回的requestCode
    private static final int CHOOSE_FILE_REQUEST_CODE = 4;

    private static final int CHOOSE_HOSPITAL_REQUEST_CODE = 5;


    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    private PopupWindow popupWindow;
    private Animation animation;
    private View customViewChoiceHospital;
    private PopupWindow popupwindowChoiceHospital;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_update_progress);
        mRlChoiceHospital = findViewById(R.id.update_progress_rl_choice_hospital);
        mTvHospitalName = findViewById(R.id.update_progress_tv_hospital_name);
        mIvHeadPic= findViewById(R.id.update_progress_iv_hospital_icon);
        mRcAddPic  = findViewById(R.id.update_progress_rc_addPic);
        mRlAddPic= findViewById(R.id.update_progress_rl_addPic);
        mRlAddFile = findViewById(R.id.update_progress_rl_addFile);
        mRlAddFileNone = findViewById(R.id.update_progress_rl_addFile_none);
        mRcAddFile = findViewById(R.id.update_progress_rc_addFile);
        mRlUpdate= findViewById(R.id.update_progress_rl_update_button);
        mEtRemarks= findViewById(R.id.update_progress_et_remarks);
        mEtNeedHelp= findViewById(R.id.update_progress_et_needHelp);
        mEtDesc= findViewById(R.id.update_progress_et_describe);
        mRlFinish= findViewById(R.id.update_progress_rl_finish);

        mEtRemarks.clearFocus();
        mEtNeedHelp.clearFocus();
        mRlAddFileNone.setVisibility(View.GONE);
        Intent intent = getIntent();
       String hospitalname = intent.getStringExtra("hospitalname");
       String prepareid = intent.getStringExtra("prepareId");
       if (!TextUtils.isEmpty(hospitalname)){
           mTvHospitalName.setText(hospitalname);
       }

       if (!TextUtils.isEmpty(prepareid)){
           prepareId = Integer.parseInt(prepareid);
       }

        Log.d("prepareId", String.valueOf(prepareId));
        initRecyclePic();
        initRecycleFile();
        mRlChoiceHospital.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
//                Intent intent = new Intent(UpDateProgressActivity.this,SearchPrepareListActivity.class);
//                startActivityForResult(intent,CHOOSE_HOSPITAL_REQUEST_CODE);
                hospitalPrepareListDataBeans.clear();
                getPrepareHospitalList(Api.URL+"/v1/prepare/listForUser?limit=20");
                initHospitalPopupWindowView();
                popupwindowChoiceHospital.showAsDropDown(view, 0, dip2px(UpDateProgressActivity.this, 12));
            }
        });
        mRlAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                showPuPopWindow(view);





            }
        });
        customViewChoiceHospital = getLayoutInflater().inflate(R.layout.choice_hospital_popwindow_item,
                null, false);
        initViewCustomView(customViewChoiceHospital);


        upDataProgressAddPicAdapter.setOnItemClickItemListener(new UpDataProgressAddPicAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {
                Log.d("mdatas", String.valueOf(position));

                if (position < 4){
                    upDataProgressAddPicAdapter.removeData(position);
//                    ToastUtils.showTextToast2(UpDateProgressActivity.this,"asdasd");
                    mRlAddPic.setVisibility(View.VISIBLE);
                    listPicString.remove(position);
                }
            }
        });

        upDataProgressAddFileAdapter.setOnItemClickItemListener(new UpDataProgressAddFileAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {
                if (position < 3){
                    upDataProgressAddFileAdapter.removeData(position);
//                    ToastUtils.showTextToast2(UpDateProgressActivity.this,"asdasd");
                    mRlAddFile.setVisibility(View.VISIBLE);
                    mRlAddFileNone.setVisibility(View.GONE);
                    listFileString.remove(position);

                }
            }
        });

        mRlAddFile.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                hideSoftKeyboard(view);
                checkPermissionAndFile();
            }
        });

        mRlUpdate.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                hideSoftKeyboard(view);
                String listStringPic = String.valueOf(listPicString).replace(" ","").replace("[","").replace("]","");
                String listStringFile = String.valueOf(listFileString).replace(" ","").replace("[","").replace("]","");


                String desc;
                String remarks;
                String needHelp;


                if (mEtDesc.getText().toString().trim().length() > 0 ){
                    desc = mEtDesc.getText().toString().trim();
                }else {
                    desc = "";
                }

                if (mEtRemarks.getText().toString().trim().length() > 0 ){
                    remarks = mEtRemarks.getText().toString().trim();
                }else {
                    remarks = "";
                }

                if (mEtNeedHelp.getText().toString().trim().length() > 0 ){
                    needHelp = mEtNeedHelp.getText().toString().trim();
                }else {
                    needHelp = "";
                }

                Log.d("listpicestring", listStringPic);
                Log.d("listfileestring", listStringFile);

                if (mTvHospitalName.getText().equals("请选择")){
                    ToastUtils.showTextToast2(UpDateProgressActivity.this,"请选择医院");
                }else if (TextUtils.isEmpty(desc)){
                    ToastUtils.showTextToast2(UpDateProgressActivity.this,"请输入进度描述");
                }else if (!mTvHospitalName.getText().equals("请选择") && !desc.equals("")){
                    postResScheduleAdd(Api.URL+"/v1/schedule/add",prepareId,desc,remarks,needHelp,listStringPic,listStringFile);
                }

            }
        });

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(UpDateProgressActivity.this);
                finish();
            }
        });


    }
    public void initViewCustomView(View view) {
        mRcHospitalList = view.findViewById(R.id.choice_hospital_rc_product_list);
    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void initHospitalPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图


        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindowChoiceHospital = new PopupWindow(customViewChoiceHospital, dip2px(this, 346), LinearLayout.LayoutParams.WRAP_CONTENT);

        popupwindowChoiceHospital.setOutsideTouchable(true);
        popupwindowChoiceHospital.setFocusable(true);
        customViewChoiceHospital.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowChoiceHospital != null && popupwindowChoiceHospital.isShowing()) {
                    popupwindowChoiceHospital.dismiss();
                    popupwindowChoiceHospital = null;
                }


                return true;
            }
        });

        popupwindowChoiceHospital.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowChoiceHospital = null;
                Log.d("TAG", "onDismiss: ");

            }
        });

    }

    private void initRecyclePic() {
        // 纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRcAddPic.setLayoutManager(linearLayoutManager);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        upDataProgressAddPicAdapter = new UpDataProgressAddPicAdapter(UpDateProgressActivity.this, listpic);
        mRcAddPic.setAdapter(upDataProgressAddPicAdapter);
//   添加动画
        mRcAddPic.setItemAnimator(new DefaultItemAnimator());
    }


    private void initRecycleFile() {
        // 纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpDateProgressActivity.this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRcAddFile.setLayoutManager(linearLayoutManager);
//   获取数据，向适配器传数据，绑定适配器
//        list = initData();
        upDataProgressAddFileAdapter = new UpDataProgressAddFileAdapter(UpDateProgressActivity.this, listfile);
        mRcAddFile.setAdapter(upDataProgressAddFileAdapter);
//   添加动画
        mRcAddFile.setItemAnimator(new DefaultItemAnimator());
    }


    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 0; i++) {
            mDatas.add("我是商品" + i);

        }

        return mDatas;
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
                        ToastUtils.showTextToast2(UpDateProgressActivity.this, "网络请求失败");
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

                                    if (dataBean.getStatus() != 0 && dataBean.getStatus() != 70 && dataBean.getStatus() != 80 && dataBean.getStatus() != 90){
                                        hospitalPrepareListDataBeans.add(dataBean);
                                    }


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

                            searPrepareListAdapter = new SearPrepareListAdapter(UpDateProgressActivity.this,hospitalPrepareListDataBeans);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpDateProgressActivity.this);
                            mRcHospitalList.setLayoutManager(linearLayoutManager);
                            mRcHospitalList.setAdapter(searPrepareListAdapter);
//                            searPrepareListAdapter.addFooterView(LayoutInflater.from(SearchPrepareListActivity.this).inflate(R.layout.home_hopital_list_layout_footer,null));




                            searPrepareListAdapter.setOnItemClickListener(new SearPrepareListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int prepareid, String hospitalname, int statu, String hospitalheadpic, int positions) {
//                                    Intent intent = new Intent();
//                                    intent.putExtra("prepareId",prepareId);
//                                    intent.putExtra("hospitalName",hospitalName);
//                                    intent.putExtra("status",status);
//                                    intent.putExtra("hospitalHeadPic",hospitalHeadPic);
//                                    setResult(RESULT_OK,intent);
//                                    ActivityCollector.removeActivity(SearchPrepareListActivity.this);
//                                    finish();

                                    prepareId = prepareid;
                                    hospitalName  =hospitalname;
                                    hospitalHeadPic = hospitalheadpic;
                                    mTvHospitalName.setText(hospitalName);

                                    /**
                                     * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
                                     */
                                    Glide.with(UpDateProgressActivity.this).load(Api.URL+hospitalHeadPic)
                                            .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                                            .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                                            .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                                            .into(mIvHeadPic);
                                    popupwindowChoiceHospital.dismiss();
                                }
                            });



                        }else if (homeFragmentHospitalPrepareListResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,homeFragmentHospitalPrepareListResponse.getMsg());
                        }
                    }
                });


            }
        });
    }

    /**
     *
     * @param popupView
     */
    public void showPuPopWindow(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.ecology_popview_item, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindow.showAtLocation(UpDateProgressActivity.this.findViewById(R.id.update_progress_rl_update_button), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndCamera();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbum();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){


            case CHOOSE_HOSPITAL_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    prepareId = data.getIntExtra("prepareId",0);
                    hospitalName  =data.getStringExtra("hospitalName");
                    hospitalHeadPic = data.getStringExtra("hospitalHeadPic");
                    mTvHospitalName.setText(hospitalName);

                    /**
                     * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
                     */
                    Glide.with(UpDateProgressActivity.this).load(Api.URL+hospitalHeadPic)
                            .error(R.mipmap.home_fragment_hospital_list_icon)//异常时候显示的图片
                            .fallback(R.mipmap.home_fragment_hospital_list_icon)//url为空的时候,显示的图片
                            .placeholder(R.mipmap.home_fragment_hospital_list_icon)//加载成功前显示的图片
                            .into(mIvHeadPic);
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
//                    upDataProgressAddPicAdapter.addData(listpic.size(), String.valueOf(mCameraUri));
////
//
//                    if (listpic.size() > 3){
//                        mRlAddPic.setVisibility(View.GONE);
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                        if (isAndroidQ){
                            bitmapCamera = compressImage1(getPath(this, mCameraUri));
                        }else {
                            bitmapCamera = compressImage1(mCameraImagePath);;
                        }

                    }
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = getBitmapFormUri(UpDateProgressActivity.this,mCameraUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    File file = createFilePic(UpDateProgressActivity.this, bitmapCamera);
                    postUpLoadPhotoCamera(Api.URL+"/upload", file);
                }else {
                    Toast.makeText(this,"取消拍摄",Toast.LENGTH_LONG).show();
                }
                break;

            case CHOOSE_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4及以上手机用这个方法

                        handleImageOnKitKat(data);
                        bitmapChoose = compressImage1(getPath(this,chooseUri));
//                        try {
//                            bitmap = getBitmapFormUri(UpDateProgressActivity.this,Uri.withAppendedPath(picbaseUri, picid));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        File file = createFilePic(UpDateProgressActivity.this, bitmapChoose);
                        postUpLoadPhotoAlbum(Api.URL+"/upload", file);
//                        postUpLoadReversePhoto(Api.URL+"/upload", file);
                    }
                }
                break;
            case CHOOSE_FILE_REQUEST_CODE:
                if (data != null) {
                    if (resultCode == RESULT_OK) {
                        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){


                            Uri uri = data.getData();

//
                            if (uri != null) {
                                File file = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    file = uriToFileApiQ(uri,this);
                                    Log.d("uri", file.getName());
                                }else {
                                    String path = getPath(this, uri);

                                    if (path != null) {
                                        file = new File(path);

                                    }
                                }
                                if (file.exists()) {
                                    String upLoadFilePath = file.toString();
                                    upLoadFileName = file.getName();
                                    postUpLoadFile(Api.URL+"/upload/file",file);

                                }



                            }
                        }

                    }
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CAMERA_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCamera();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"拍照权限被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbum();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"访问相册权限被拒绝",Toast.LENGTH_LONG).show();
                }
                break;

            case PERMISSION_FILE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openFileManager();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this,"访问文件管理器权限被拒绝",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openAlbum();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(UpDateProgressActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE);
        }
    }

    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndFile() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openFileManager();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(UpDateProgressActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE_REQUEST_CODE);
        }
    }

    // 打开文件管理器选择文件
    private void openFileManager() {
        // 打开文件管理器选择文件
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");//选择图片
//        intent.setType("audio/*"); //选择音频
//        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
//        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
//        intent.setType("video/*;image/*;audio/*;application/msword");//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);

//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授予临时权限别忘了
        startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
    }


    /**
     * android10以上uri转file
     * @param uri
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static File uriToFileApiQ(Uri uri,Context context) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    FileUtils.copy(is, fos);
                    file = cache;
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    /**
     * 安卓10以下uri转file
     * @param uri
     * @param context
     * @return
     */
    public static File uriToFile(Uri uri,Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }


    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE);
    }


    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }


    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            Log.d("opencamera", String.valueOf(mCameraUri));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //如果document是Uri类型，则通过document id处理
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                picid=docId.split(":")[1];//id="26"
                picbaseUri=Uri.parse("content://media/external/images/media");
//                picture.setImageURI(Uri.withAppendedPath(baseUri, id));
                //直接传入Uri地址，该地址为content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
                displayImage(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
//            imagePath = getImagePath(uri,null);
            displayImage(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            displayImage(imagePath);
        }
        chooseUri = uri;

    }

    /**
     * 通过图片路径展示图片
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(UpDateProgressActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            imageName = "output_image.jpg";
        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }


    /**
     * 正面
     * ====根据 bitmap生成新的file
     */
    public static File createFilePic(Context context, Bitmap bitmap) {
        //https://www.jb51.net/article/181745.htm

        //文件夹  这两种文件夹都会随软件卸载而删掉
        File folder = context.getExternalCacheDir();//在Android>data>包名>的cache目录下，一般存放临时缓存数据
        //File folder = this.getExternalFilesDir("image");//在Android>data>包名>的files的image目录下，一般放一些长时间保存的数据
        if (!folder.exists()) {
            folder.mkdir();
        }

        SimpleDateFormat df = null;//设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        String filename = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filename = df.format(new Date());
        }
        //file图片
        File file = new File(folder.getAbsolutePath() + File.separator +filename+ ".jpg");

        try {

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 通过Uri获取一个Bitmap对象
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }




    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }



    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoCamera(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = Api.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(UpDateProgressActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,"上传失败，请重新拍摄");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                upDataProgressAddPicAdapter.addData(listpic.size(), bitmapCamera);
//

                                if (listpic.size() > 3){
                                    mRlAddPic.setVisibility(View.GONE);
                                }
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);

                                listPicString.add(String.valueOf(uploadPhotoDateResponse.getPictureId()));
                                upDataProgressAddPicAdapter.setOnItemClickListener(new UpDataProgressAddPicAdapter.OnClickItemListener() {
                                    @Override
                                    public void onItemClickItem(int position, Bitmap bitmap) {
//                                        Intent intent = new Intent(UpDateProgressActivity.this, CheckPhotoActivity.class);
//                                        intent.putExtra("uri",uri);
////                                        startActivity(intent);
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(UpDateProgressActivity.this,R.style.CustomDialogPhoto,bitmap);
                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        checkPhotoBitmapDialog.show();
                                        if (checkPhotoBitmapDialog.isShowing()){
                                            final Window window=getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                window.setStatusBarColor(Color.parseColor("#000000"));
                                            }
                                        }
                                        checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {

                                                final Window window=getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
                                                }

                                            }
                                        });


                                        Window dialogWindow = checkPhotoBitmapDialog.getWindow();
                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                                        dialogWindow.setGravity(Gravity.BOTTOM);
                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.y = 0;
                                        dialogWindow.setAttributes(lp);
                                    }
                                });
                            }else if (uploadPhotoResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(UpDateProgressActivity.this,uploadPhotoResponse.getMsg());
                            }
                        }






                    }

                });


            }
        });
    }


    /**
     * post请求上传照片
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadPhotoAlbum(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = Api.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(UpDateProgressActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,"上传失败，请重新拍摄");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                Log.d("uploadPhotoResponse", String.valueOf(uploadPhotoResponse));
                                upDataProgressAddPicAdapter.addData(listpic.size(), bitmapChoose);
                                if (listpic.size() > 3){
                                    mRlAddPic.setVisibility(View.GONE);
                                }
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);

                                listPicString.add(String.valueOf(uploadPhotoDateResponse.getPictureId()));

                                upDataProgressAddPicAdapter.setOnItemClickListener(new UpDataProgressAddPicAdapter.OnClickItemListener() {
                                    @Override
                                    public void onItemClickItem(int position, Bitmap bitmap) {
//                                        Intent intent = new Intent(UpDateProgressActivity.this, CheckPhotoActivity.class);
//                                        intent.putExtra("uri",uri);
////                                        startActivity(intent);
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(UpDateProgressActivity.this,R.style.CustomDialogPhoto,bitmap);
                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        checkPhotoBitmapDialog.show();
                                        if (checkPhotoBitmapDialog.isShowing()){
                                            final Window window=getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                window.setStatusBarColor(Color.parseColor("#000000"));
                                            }
                                        }
                                        checkPhotoBitmapDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {

                                                final Window window=getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
                                                }

                                            }
                                        });


                                        Window dialogWindow = checkPhotoBitmapDialog.getWindow();
                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                                        dialogWindow.setGravity(Gravity.BOTTOM);
                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.y = 0;
                                        dialogWindow.setAttributes(lp);
                                    }
                                });
                            }else if (uploadPhotoResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(UpDateProgressActivity.this,uploadPhotoResponse.getMsg());
                            }
                        }






                    }

                });


            }
        });
    }

    /**
     * post请求上传文件
     * 相机
     * @param url
     * @param file
     */
    public void postUpLoadFile(String url, final File file) {
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体


        // MediaType.parse() 里面是上传的文件类型。
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // 参数分别为， 请求key ，文件名称 ， RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //构建请求
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post请求Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call对象
        Call call = Api.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(UpDateProgressActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,"上传失败，请重新拍摄");

                        }else {

                            UpLoadFileResponse upLoadFileResponse = new Gson().fromJson(json, UpLoadFileResponse.class);
                            if (upLoadFileResponse.getCode() == 0) {

                                upDataProgressAddFileAdapter.addData(listfile.size(), upLoadFileName);
                                if (listfile.size() > 2){
                                    mRlAddFile.setVisibility(View.GONE);
                                    mRlAddFileNone.setVisibility(View.VISIBLE);
                                }

                                listFileString.add(String.valueOf(upLoadFileResponse.getData().getFileId()));

                            }else if (upLoadFileResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(UpDateProgressActivity.this,upLoadFileResponse.getMsg());
                            }
                        }






                    }

                });


            }
        });
    }


    protected void postResScheduleAdd(String url, int prepareId, String desc, String remarks, String needHelp, String pic, String appendix ) {
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
            jsonObject.put("prepareId",prepareId);
            jsonObject.put("desc",desc);
            jsonObject.put("remarks",remarks);
            jsonObject.put("needHelp",needHelp);
            jsonObject.put("pic",pic);
            jsonObject.put("appendix",appendix);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
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

                        ToastUtils.showTextToast2(UpDateProgressActivity.this, "网络请求失败");
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
                        UpDataProgressResponse upDataProgressResponse = gson.fromJson(res,UpDataProgressResponse.class);
                        if (upDataProgressResponse.getCode() == 0){
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,"更新成功");
                            saveStringToSp("refresh","refreshProgressList");
                            ActivityCollector.removeActivity(UpDateProgressActivity.this);
                            finish();
                        } else if (upDataProgressResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else if (upDataProgressResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(UpDateProgressActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(UpDateProgressActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(UpDateProgressActivity.this,upDataProgressResponse.getMsg());
                        }

                    }
                });


            }
        });


    }

    public String getTokenToSp(String key, String val){
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String token = sp.getString("token","");
        return token;
    }

    public String getUidToSp(String key, String val){
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid","");
        return uid;
    }

    public String getUserTypeToSp(String key, String val){
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype","");
        return userType;
    }
    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public void hideSoftKeyboard(View view){
        //这里获取view为参数 之前试过用context,LoginActivity.this会造成闪退
        //view不会闪退
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 图片质量压缩
     * @param
     * @param
     * @return
     */
    public static Bitmap compressImage1(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 576, 960);// 此处，选取了480x800分辨率的照片

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;// 处理完后，同时需要记得设置为false

        return BitmapFactory.decodeFile(filePath, options);


    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }



}