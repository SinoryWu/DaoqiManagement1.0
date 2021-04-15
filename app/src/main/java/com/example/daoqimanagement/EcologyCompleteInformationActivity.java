package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.app.Activity;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.bean.EcologyCompleteInformationResponse;
import com.example.daoqimanagement.bean.UploadPhotoDateResponse;
import com.example.daoqimanagement.bean.UploadPhotoResponse;
import com.example.daoqimanagement.dialog.CheckPhotoBitmapDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.dialog.LoginOutDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ActivityCollectorLogin;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class EcologyCompleteInformationActivity extends AppCompatActivity implements View.OnClickListener {


    DialogTokenIntent dialogTokenIntent = null;
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //申请访问相册权限的requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE = 0x00000013;
    //成功调用相机后返回的requestCode
    private static final int CAMERA_REQUEST_CODE = 1;
    //成功打开相册后返回的requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 2;


    // 申请相机权限的requestCodeEntrust
    private static final int PERMISSION_CAMERA_REQUEST_CODE_ENTRUST = 0x00000014;
    //申请访问相册权限的requestCodeEntrust
    private static final int PERMISSION_ALBUM_REQUEST_CODE_ENTRUST = 0x00000015;
    //成功调用相机后返回的requestCodeEntrust
    private static final int CAMERA_REQUEST_CODE_ENTRUST = 3;
    //成功打开相册后返回的requestCodeEntrust
    private static final int CHOOSE_PHOTO_REQUEST_CODE_ENTRUST = 4;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath,mCameraImagePathEntrust;
    //用于保存拍照图片的uri
    private Uri mCameraUri,mCameraUriEntrust,mChooseUri,mChooseUriEntrust;


    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private LinearLayout mLlCamera, mLlArrowLeft,mLlCameraEntrust;
    private PopupWindow popupWindow;


    private Animation animation;

    private ImageView mIvPhotoFalse;
    private SimpleDraweeView mIvPhotoTrue,mIvPhotoTrueEntrust;

    private TextView mTvPhotoToast,mTvPhotoAgain,mTvPhotoAgainEntrust;
    private Button mBtnSubmit;
    private String license = "00000";
    int empower = 0;
    private EditText mEtEnterprise,mEtCreditCode,mEtBank,mEtBankNumber;
    File photoFile = null;
    String photoId;
    Uri baseUri;
    Bitmap bitmapCamera,bitmapEntrust ;

    List<String> listPic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        ActivityCollectorLogin.addActivity(this);
        initView();
        mLlCamera.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);

        mLlCameraEntrust.setOnClickListener(this);
        mTvPhotoAgain.setVisibility(View.GONE);
        mTvPhotoAgainEntrust.setVisibility(View.GONE);
        mTvPhotoAgain.setOnClickListener(this);
        mTvPhotoAgainEntrust.setOnClickListener(this);

        listPic.add("0");
        listPic.add("0");

        mLlArrowLeft.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                hideSoftKeyboard(view);
                LoginOutDialog loginOutDialog = new LoginOutDialog(EcologyCompleteInformationActivity.this,R.style.CustomDialog);
                loginOutDialog.setConfirm("是的", new LoginOutDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(LoginOutDialog dialog) {

                        Intent intent = new Intent(EcologyCompleteInformationActivity.this,LoginActivity.class);
                        startActivity(intent);
                        ActivityCollector.removeActivity(EcologyCompleteInformationActivity.this);
                        finish();


                    }
                }).show();
            }
        });
    }

    public void initView() {
        setContentView(R.layout.activity_ecology_complete_information);
        mLlCamera = findViewById(R.id.ecology_info_ll_camera);
        mLlCameraEntrust = findViewById(R.id.ecology_info_ll_camera_entrust);
        mIvPhotoTrue = findViewById(R.id.ecology_info_iv_photo_true);
        mIvPhotoFalse = findViewById(R.id.ecology_info_iv_photo_false);
        mTvPhotoToast = findViewById(R.id.ecology_info_tv_photo);
        mBtnSubmit = findViewById(R.id.ecology_info_btn_submit);
        mLlArrowLeft = findViewById(R.id.ecology_info_ll_arrowleft);
        mEtEnterprise = findViewById(R.id.ecology_info_et_enterprise);
        mEtCreditCode = findViewById(R.id.ecology_info_et_credit);
        mEtBank = findViewById(R.id.ecology_info_et_bank);
        mEtBankNumber = findViewById(R.id.ecology_info_et_bank_number);
        mIvPhotoTrueEntrust = findViewById(R.id.ecology_info_iv_photo_true_entrust);
        mTvPhotoAgain = findViewById(R.id.ecology_info_tv_photo_again);
        mTvPhotoAgainEntrust = findViewById(R.id.ecology_info_tv_photo_again_entrust);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecology_info_ll_camera:
                showPuPopWindow(view);
                hideSoftKeyboard(view);
                break;
            case R.id.ecology_info_btn_submit:
                hideSoftKeyboard(view);
                Log.d("pictureId", String.valueOf(license));
                String businessName = mEtEnterprise.getText().toString().trim();
                String creditCode = mEtCreditCode.getText().toString().trim();
                String bank = mEtBank.getText().toString().trim();
                String bankNo = mEtBankNumber.getText().toString().trim();
//                String license = String.valueOf(listPic).replace(" ","").replace("[","").replace("]","");

                Log.d("license", license);

                if (TextUtils.isEmpty(businessName)) {
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请输入企业名称");

                } else if (TextUtils.isEmpty(creditCode)) {
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请输入统一社会信用代码");
                } else if (TextUtils.isEmpty(bank)) {
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请输入开户行");
                }else if (TextUtils.isEmpty(bankNo)) {
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请输入开户账号");
                }else if (license.equals("00000")){
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请上传营业执照");
                } else if (empower== 0){
                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "请上传授权委托书");
                }else if (!TextUtils.isEmpty(businessName)  && !TextUtils.isEmpty(creditCode) &&  !TextUtils.isEmpty(bank) && !TextUtils.isEmpty(bankNo)  && !license.equals("00000") && empower != 0 ){
//                    ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this,"可以注册");
                    postCompleteInfo(Api.URL+"/v1/user/supplyInfoForST",businessName,creditCode,bank,bankNo,license,empower);
                }

                break;

            case R.id.ecology_info_ll_camera_entrust:
                showPuPopWindowEntrust(view);
                hideSoftKeyboard(view);
                break;
            case R.id.ecology_info_tv_photo_again:
                showPuPopWindow(view);
                hideSoftKeyboard(view);
                break;
            case R.id.ecology_info_tv_photo_again_entrust:
                showPuPopWindowEntrust(view);
                hideSoftKeyboard(view);
                break;



        }
    }

    /**
     * 底部弹出popupwindow
     * 含动画
     *
     * @param popupView
     */
    public void showPuPopWindow(View popupView) {
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
            popupWindow.showAtLocation(EcologyCompleteInformationActivity.this.findViewById(R.id.ecology_info_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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


    /**
     * 底部弹出popupwindow
     * 含动画
     *
     * @param popupView
     */
    public void showPuPopWindowEntrust(View popupView) {
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
            popupWindow.showAtLocation(EcologyCompleteInformationActivity.this.findViewById(R.id.ecology_info_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    checkPermissionAndCameraEntrust();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbumEntrust();
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

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_CAMERA_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCamera();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbum();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "访问相册权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_CAMERA_REQUEST_CODE_ENTRUST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，调起相机拍照。
                    openCameraEntrust();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE_ENTRUST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许权限，打开相册。
                    openAlbumEntrust();
                } else {
                    //拒绝权限，弹出提示框。
                    Toast.makeText(this, "访问相册权限被拒绝", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 处理拍照后结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        bitmapCamera = BitmapFactory.decodeFile( getPath(this,mCameraUri));


                        if (isAndroidQ){
                            bitmapCamera = compressImage1(getPath(this, mCameraUri));
                        }else {
                            bitmapCamera = compressImage1(mCameraImagePath);;
                        }
                    }
                    File file = createFilePic(this,bitmapCamera);

                    postUpLoadPhoto(Api.URL+"/upload", file);




                } else {
                    Toast.makeText(this, "取消拍摄", Toast.LENGTH_LONG).show();
                }
                break;
            case CHOOSE_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //Android 4.4及以上手机用这个方法
                        handleImageOnKitKat(data);

//                        bitmapCamera = BitmapFactory.decodeFile( getPath(this,mChooseUri));
                        bitmapCamera = compressImage1(getPath(this,mChooseUri));
                        File file = createFilePic(this, bitmapCamera);

                        postUpLoadPhoto(Api.URL+"/upload", file);
                    } else {
                        //Android 4.4以下手机用这个方法
                        handleImageBeforeKitKat(data);
                    }


                }
                break;
            case CAMERA_REQUEST_CODE_ENTRUST:
                if (resultCode == RESULT_OK) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        bitmapEntrust = BitmapFactory.decodeFile( getPath(this,mCameraUriEntrust));

                        if (isAndroidQ){
                            bitmapCamera = compressImage1(getPath(this, mCameraUriEntrust));
                        }else {
                            bitmapCamera = compressImage1(mCameraImagePathEntrust);;
                        }

                    }
                    File file = createFilePic(this,bitmapEntrust);

                    postUpLoadPhotoEntrust(Api.URL+"/upload", file);




                } else {
                    Toast.makeText(this, "取消拍摄", Toast.LENGTH_LONG).show();
                }
                break;
            case CHOOSE_PHOTO_REQUEST_CODE_ENTRUST:
                if (resultCode == RESULT_OK) {

                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //Android 4.4及以上手机用这个方法
                        handleImageOnKitKatEntrust(data);

//                        bitmapEntrust = BitmapFactory.decodeFile( getPath(this,mChooseUriEntrust));
                        bitmapEntrust = compressImage1(getPath(this,mChooseUriEntrust));
                        File file = createFilePic(this, bitmapEntrust);

                        postUpLoadPhotoEntrust(Api.URL+"/upload", file);
                    } else {
                        //Android 4.4以下手机用这个方法
                        handleImageBeforeKitKat(data);
                    }


                }
                break;
            default:
                break;
        }
    }


    /**
     * post请求上传照片
     * @param url
     * @param file
     */
    public void postUpLoadPhoto(String url, final File file) {
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
                        ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();
                Log.d("jsonPhoto", json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (json.equals("") || json == null){
                            ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this,"上传失败，请重新拍摄");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {

                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);
                                license = String.valueOf(uploadPhotoDateResponse.getPictureId());
                                Log.d("pictureId", license);

                                mIvPhotoTrue.setVisibility(View.VISIBLE);
                                mIvPhotoTrue.setImageBitmap(bitmapCamera);
                                mLlCamera.setVisibility(View.GONE);
                                mTvPhotoAgain.setVisibility(View.VISIBLE);
                                mIvPhotoTrue.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(EcologyCompleteInformationActivity.this,R.style.CustomDialogPhoto,bitmapCamera);
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
                                                    window.setStatusBarColor(Color.parseColor("#3F2B4D"));
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
                            } else if (uploadPhotoResponse.getCode() == 10009) {
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(EcologyCompleteInformationActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {

                                            Intent intent = new Intent(EcologyCompleteInformationActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            } else {
                                String msg = uploadPhotoResponse.getMsg();
                                ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, msg);
                            }
                        }






                    }

                });


            }
        });
    }


    /**
     * post请求上传照片
     * @param url
     * @param file
     */
    public void postUpLoadPhotoEntrust(String url, final File file) {
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
                        ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "网络请求失败");
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
                            ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this,"上传失败，请重新拍摄");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);
                                empower = uploadPhotoDateResponse.getPictureId();
                                Log.d("pictureId", license);


                                mIvPhotoTrueEntrust.setImageBitmap(bitmapEntrust);
                                mIvPhotoTrueEntrust.setVisibility(View.VISIBLE);
                                mLlCameraEntrust.setVisibility(View.GONE);
                                mTvPhotoAgainEntrust.setVisibility(View.VISIBLE);
                                mIvPhotoTrueEntrust.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(EcologyCompleteInformationActivity.this,R.style.CustomDialogPhoto,bitmapEntrust);
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
                                                    window.setStatusBarColor(Color.parseColor("#3F2B4D"));
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
                            }else if (uploadPhotoResponse.getCode() == 10009) {
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(EcologyCompleteInformationActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {

                                            Intent intent = new Intent(EcologyCompleteInformationActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            } else {
                                String msg = uploadPhotoResponse.getMsg();
                                ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, msg);
                            }
                        }






                    }

                });


            }
        });
    }




    public void postCompleteInfo(String url,String businessName,String creditCode, String bank,String bankNo,String license,int empower) {
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
            jsonObject.put("businessName",businessName);
            jsonObject.put("creditCode",creditCode);
            jsonObject.put("bank",bank);
            jsonObject.put("bankNo",bankNo);
            jsonObject.put("license",license);
            jsonObject.put("empower",empower);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = jsonObject.toString();

        Log.d("jsonStr", jsonStr);

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);

        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, "网络请求失败");
                        Log.d("json", "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        EcologyCompleteInformationResponse ecologyCompleteInformationResponse = gson.fromJson(res,EcologyCompleteInformationResponse.class);
                        if (ecologyCompleteInformationResponse.getCode() == 0){
                            Intent intent = new Intent(EcologyCompleteInformationActivity.this,PayJoinExamineActivity.class);
                            ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this,"提交成功");
                            startActivity(intent);
                            ActivityCollector.removeActivity(EcologyCompleteInformationActivity.this);
                            finish();

                        }else if (ecologyCompleteInformationResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(EcologyCompleteInformationActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(EcologyCompleteInformationActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = ecologyCompleteInformationResponse.getMsg();
                            ToastUtils.showTextToast2(EcologyCompleteInformationActivity.this, msg);
                        }

                    }

                });


            }
        });
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }


    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCameraEntrust() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相机拍照。
            openCameraEntrust();

        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相册选择图片。

            openAlbum();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(EcologyCompleteInformationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE);
        }
    }


    /**
     * 检查权限并打开相册。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndAlbumEntrust() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //有权限调起相册选择图片。

            openAlbumEntrust();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(EcologyCompleteInformationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE_ENTRUST);
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE);
    }

    /**
     * 打开相册
     */
    private void openAlbumEntrust() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //把所有照片显示出来
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE_ENTRUST);
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
     * 调起相机拍照
     */
    private void openCameraEntrust() {
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
                    mCameraImagePathEntrust = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUriEntrust = photoUri;

            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE_ENTRUST);
            }


        }
    }

    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     *
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this, uri)) {
            //如果document是Uri类型，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                photoId = docId.split(":")[1];//id="26"
                baseUri = Uri.parse("content://media/external/images/media");

                //直接传入Uri地址，该地址为content://media/external/images/media/26
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                displayImage(imagePath);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
            displayImage(imagePath);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            displayImage(imagePath);
        }

        if (imagePath != null){
            Log.d("as3d113s4d6as5", imagePath);
        }
        mChooseUri = uri;
        Log.d("as3d113s4d6as5", String.valueOf(uri));

    }


    /**
     * 根据图片路径显示图片
     * Api19也就是Android4.4及以上
     *
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKatEntrust(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this, uri)) {
            //如果document是Uri类型，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = docId.split(":")[1];//解析数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                photoId = docId.split(":")[1];//id="26"
                baseUri = Uri.parse("content://media/external/images/media");

                //直接传入Uri地址，该地址为content://media/external/images/media/26
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                displayImage(imagePath);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
            displayImage(imagePath);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            displayImage(imagePath);
        }

        if (imagePath != null){
            Log.d("as3d113s4d6as5", imagePath);
        }
        mChooseUriEntrust = uri;
        Log.d("as3d113s4d6as5", String.valueOf(uri));

    }

    /**
     * 根据图片路径显示图片
     * Android4.4以下版本
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 通过getContentResolver里的query方法查询数据
     * 目的是获取图片的真实路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取图片真实路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    /**
     * 通过图片路径展示图片
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvPhotoTrue.setImageBitmap(bitmap);
        } else {
            Toast.makeText(EcologyCompleteInformationActivity.this, "获取照片失败", Toast.LENGTH_SHORT).show();
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
            imageName = "BusinessLicense.jpg";
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

    /**
     * ====根据 uri（content或file开头都行）生成新的file
     */
    public static File createFile(Context context, Uri uri) {
        //https://www.jb51.net/article/181745.htm

        //文件夹  这两种文件夹都会随软件卸载而删掉
        File folder = context.getExternalCacheDir();//在Android>data>包名>的cache目录下，一般存放临时缓存数据
        //File folder = this.getExternalFilesDir("image");//在Android>data>包名>的files的image目录下，一般放一些长时间保存的数据
        if (!folder.exists()) {
            folder.mkdir();
        }
        //file图片
        File file = new File(folder.getAbsolutePath() + File.separator + "BusinessLicense.jpg");

        try {
            //根据uri获取输入字节流
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            //==把输入流写进file里
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileOutputStream.flush();
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
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