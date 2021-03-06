package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.daoqimanagement.utils.AllCapTransformationMethod;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.PersonIdCardUtil;
import com.example.daoqimanagement.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class StarFireCompleteInformationActivity extends AppCompatActivity implements View.OnClickListener {

    DialogTokenIntent dialogTokenIntent = null;

    // ????????????????????????????????????requestCode
    private static final int PERMISSION_CAMERA_FRONT_REQUEST_CODE = 0x00000012;
    // ????????????????????????????????????requestCode
    private static final int PERMISSION_CAMERA_REVERSE_REQUEST_CODE = 0x00000013;
    //?????????????????????????????????requestCode
    private static final int PERMISSION_ALBUM_FRONT_REQUEST_CODE = 0x00000014;
    //?????????????????????????????????requestCode
    private static final int PERMISSION_ALBUM_REVERSE_REQUEST_CODE = 0x00000015;

    //?????????????????????????????????????????????requestCode
    private static final int CAMERA_FRONT_REQUEST_CODE = 1;
    //?????????????????????????????????????????????requestCode
    private static final int CAMERA_REVERSE_REQUEST_CODE = 3;
    //????????????????????????????????????requestCode
    private static final int CHOOSE_PHOTO_FRONT_REQUEST_CODE = 2;
    //????????????????????????????????????requestCode
    private static final int CHOOSE_PHOTO_REVERSE_REQUEST_CODE = 4;

    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePathFront,mCameraImagePathReverse;
    //???????????????????????????uri
    private Uri mCameraFrontUri;
    private Uri mCameraReverseUri;
    private Uri mAlbumFrontUri,mAlbumReverseUri;

    // ?????????Android 10????????????
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;


    private LinearLayout mLlCameraIdFront,mLlCameraIdReverse,mLlArrowLeft;
    private ImageView mIvPhotoIdReverseFalse,mIvPhotoIdFrontFalse;
    private SimpleDraweeView mIvPhotoIdFrontTrue,mIvPhotoIdReverseTrue;
    private TextView mTvPhotoIdFront,mTvPhotoIdReverse,mTvFrontAgain,mTvReverseAgain;
    private PopupWindow popupWindow;
    private Animation animation;
    private Button mBtnSubmit;
    private EditText mEtIdCard;

    String pictureIdFront = "00000";
    String pictureIdReverse = "00001";
    File photoFrontFile,photoReverseFile;
    String photoIdFront, photoIdReverse;
    Uri baseUriFront,baseUriReverse;
    String charsSmall,charsBig;
    Bitmap bitmapFront,bitmapReverse;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorLogin.addActivity(this);
        ActivityCollector.addActivity(this);
        initView();
        mLlCameraIdFront.setOnClickListener(this);
        mLlCameraIdReverse.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mTvFrontAgain.setOnClickListener(this);
        mTvReverseAgain.setOnClickListener(this);
        mTvFrontAgain.setVisibility(View.GONE);
        mTvReverseAgain.setVisibility(View.GONE);

        mLlArrowLeft.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                hideSoftKeyboard(view);
                LoginOutDialog loginOutDialog = new LoginOutDialog(StarFireCompleteInformationActivity.this,R.style.CustomDialog);
                loginOutDialog.setConfirm("??????", new LoginOutDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(LoginOutDialog dialog) {
                        Intent intent = new Intent(StarFireCompleteInformationActivity.this,LoginActivity.class);
                        startActivity(intent);
                        ActivityCollector.removeActivity(StarFireCompleteInformationActivity.this);
                        finish();
                    }
                }).show();
            }
        });
        mEtIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charsSmall =  String.valueOf(charSequence);

                charsBig =charsSmall.toUpperCase();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void initView(){
        setContentView(R.layout.activity_star_fire_complete_information);
        mLlCameraIdFront =  findViewById(R.id.starfire_info_ll_camera_id_front);
        mLlCameraIdReverse = findViewById(R.id.starfire_info_ll_camera_id_reverse);
        mIvPhotoIdFrontTrue = findViewById(R.id.starfire_info_iv_photo_id_front_true);
        mIvPhotoIdFrontFalse = findViewById(R.id.starfire_info_iv_photo_id_front_false);
        mIvPhotoIdReverseTrue = findViewById(R.id.starfire_info_iv_photo_id_reverse_true);
        mIvPhotoIdReverseFalse = findViewById(R.id.starfire_info_iv_photo_id_reverse_false);
        mTvPhotoIdFront = findViewById(R.id.starfire_info_tv_photo_id_front);
        mTvPhotoIdReverse = findViewById(R.id.ecology_info_tv_photo_id_reverse);
        mEtIdCard = findViewById(R.id.starfire_info_et_id_number);
        mBtnSubmit= findViewById(R.id.starfire_info_btn_submit);
        mTvFrontAgain = findViewById(R.id.starfire_info_tv_front_again);
        mTvReverseAgain = findViewById(R.id.starfire_info_tv_reverse_again);
        mLlArrowLeft = findViewById(R.id.starfire_info_ll_arrowleft);
        mEtIdCard.setTransformationMethod(new AllCapTransformationMethod());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.starfire_info_ll_camera_id_front:
                hideSoftKeyboard(view);
                showPuPopWindowFront(view);
                break;
            case R.id.starfire_info_ll_camera_id_reverse:
                hideSoftKeyboard(view);
                showPuPopWindowReverse(view);
                break;
            case R.id.starfire_info_tv_front_again:
                hideSoftKeyboard(view);
                showPuPopWindowFront(view);
                break;
            case R.id.starfire_info_tv_reverse_again:
                hideSoftKeyboard(view);
                showPuPopWindowReverse(view);
                break;
            case R.id.starfire_info_btn_submit:
                hideSoftKeyboard(view);
                String idCard = "";

                if (charsBig != null){
                    idCard = charsBig.replace(" ","");
                }
                String idCardPositive = pictureIdFront;
                String idCardVerso = pictureIdReverse;
                if (TextUtils.isEmpty(idCard) || charsBig == null){
                    ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"????????????????????????");
                }else if (PersonIdCardUtil.isValidatedAllIdCard(idCard) == false){
                    ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"??????????????????????????????");
                }else if(idCardPositive.equals("00000")){
                    ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"???????????????????????????");
                }else if(idCardVerso.equals("00001")){
                    ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"???????????????????????????");
                } else if (!TextUtils.isEmpty(idCard) && idCardPositive.equals("00000") == false && idCardVerso.equals("00001") == false && PersonIdCardUtil.isValidatedAllIdCard(idCard) == true){
                    completeInfoPost(idCard,idCardPositive,idCardVerso);
                }

                Log.d("idCard", idCard);
                Log.d("idCard", idCardPositive);
                Log.d("idCard", idCardVerso);



                break;

        }
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CAMERA_FRONT_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????????????????????????????????
                    openCameraFront();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_CAMERA_REVERSE_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????????????????????????????????
                    openCameraReverse();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_FRONT_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //??????????????????????????????
                    openAlbumFront();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"???????????????????????????",Toast.LENGTH_LONG).show();
                }
                break;

            case PERMISSION_ALBUM_REVERSE_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //??????????????????????????????
                    openAlbumReverse();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"???????????????????????????",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * ?????????????????????
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_FRONT_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        bitmapFront = BitmapFactory.decodeFile(getPath(this, mCameraFrontUri));

                        if (isAndroidQ){
                            bitmapFront = compressImage1(getPath(this, mCameraFrontUri));
                        }else {
                            bitmapFront = compressImage1(mCameraImagePathFront);;
                        }
                    }
                    File file = createFilePic(this, bitmapFront);

                        postUpLoadFrontPhoto(Api.URL+"/upload", file);

                }else {
                    Toast.makeText(this,"????????????",Toast.LENGTH_LONG).show();
                }
                break;

            case CAMERA_REVERSE_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        bitmapReverse = BitmapFactory.decodeFile(getPath(this, mCameraReverseUri));

                        if (isAndroidQ){
                            bitmapReverse = compressImage1(getPath(this, mCameraReverseUri));
                        }else {
                            bitmapReverse = compressImage1(mCameraImagePathReverse);;
                        }
                    }
                    File file = createFilePic(this, bitmapReverse);

                        postUpLoadReversePhoto(Api.URL+"/upload", file);

                }else {
                    Toast.makeText(this,"????????????",Toast.LENGTH_LONG).show();
                }
                break;
            case CHOOSE_PHOTO_FRONT_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    //?????????????????????
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4??????????????????????????????
                        handleImageOnKitKatFront(data);
//                        bitmapFront = BitmapFactory.decodeFile(getPath(this, mAlbumFrontUri));
                        bitmapFront = compressImage1(getPath(this, mAlbumFrontUri));
                        File file = createFilePic(StarFireCompleteInformationActivity.this,bitmapFront);
                        postUpLoadFrontPhoto(Api.URL+"/upload", file);
                    } else {
                        //Android 4.4???????????????????????????
                        handleImageBeforeKitKatFront(data);
                    }
                }
                break;

            case CHOOSE_PHOTO_REVERSE_REQUEST_CODE:
                if (resultCode == RESULT_OK){

                    //?????????????????????
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4??????????????????????????????
                        handleImageOnKitKatReverse(data);
//                        bitmapReverse =  BitmapFactory.decodeFile(getPath(this, mAlbumReverseUri));
                        bitmapReverse = compressImage1(getPath(this, mAlbumReverseUri));
                        File file = createFilePic(StarFireCompleteInformationActivity.this,bitmapReverse);
                        postUpLoadReversePhoto(Api.URL+"/upload", file);
                    } else {
                        //Android 4.4???????????????????????????
                        handleImageBeforeKitKatReverse(data);
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * post??????????????????
     * ??????
     * @param url
     * @param file
     */
    public void postUpLoadFrontPhoto(String url, final File file) {
        //??????OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //?????????


        // MediaType.parse() ?????????????????????????????????
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // ?????????????????? ??????key ??????????????? ??? RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //????????????
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //?????????
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post??????Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post??????Key,value
                .build();
        //????????????
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call??????
        Call call = Api.ok().newCall(request);
        //call????????????
        call.enqueue(new Callback() {   //??????
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this, "??????????????????");
                        Log.d("json", "??????????????????");
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
                            ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"??????????????????????????????");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);
                                pictureIdFront = String.valueOf(uploadPhotoDateResponse.getPictureId());
                                mIvPhotoIdFrontTrue.setImageBitmap(bitmapFront);
                                mIvPhotoIdFrontTrue.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(StarFireCompleteInformationActivity.this,R.style.CustomDialogPhoto, bitmapFront);
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
                                Log.d("pictureIdFront", pictureIdFront);
                                mIvPhotoIdFrontTrue.setVisibility(View.VISIBLE);
                                mLlCameraIdFront.setVisibility(View.GONE);
                                mTvFrontAgain.setVisibility(View.VISIBLE);
                            }else if (uploadPhotoResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(StarFireCompleteInformationActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(StarFireCompleteInformationActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }
                        }






                    }

                });


            }
        });
    }

    /**
     * post??????????????????
     * ??????
     * @param url
     * @param file
     */
    public void postUpLoadReversePhoto(String url, final File file) {
        //??????OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //?????????


        // MediaType.parse() ?????????????????????????????????
//        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        String filename = file.getName();
//        // ?????????????????? ??????key ??????????????? ??? RequestBody
//        requestBody.addFormDataPart("file", filename, body);
//        //????????????
//        Request request = new Request.Builder().url(url).post(requestBody.build()).build();


        //?????????
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //post??????Key,value
                .addFormDataPart("file", file.getName(), requestBody)     //post??????Key,value
                .build();
        //????????????
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url(url)
                .post(body)
                .build();
        //call??????
        Call call = Api.ok().newCall(request);
        //call????????????
        call.enqueue(new Callback() {   //??????
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this, "??????????????????");
                        Log.d("json", "??????????????????");
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
                            ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"??????????????????????????????");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);
                                pictureIdReverse = String.valueOf(uploadPhotoDateResponse.getPictureId());
                                mIvPhotoIdReverseTrue.setImageBitmap(bitmapReverse);
                                mIvPhotoIdReverseTrue.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(StarFireCompleteInformationActivity.this,R.style.CustomDialogPhoto,bitmapReverse);
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
                                Log.d("pictureIdReverse", pictureIdReverse);
                                mIvPhotoIdReverseTrue.setVisibility(View.VISIBLE);
                                mLlCameraIdReverse.setVisibility(View.GONE);
                                mTvReverseAgain.setVisibility(View.VISIBLE);
                            }else if (uploadPhotoResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(StarFireCompleteInformationActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(StarFireCompleteInformationActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,uploadPhotoResponse.getMsg());
                            }
                        }






                    }

                });


            }
        });
    }

    public void completeInfoPost(String idCard,String idCardPositive,String idCardVerso){
        HashMap<String, String> map = new HashMap<>();
        map.put("idCard", idCard);//18158188052
        map.put("idCardPositive", idCardPositive);//111
        map.put("idCardVerso",idCardVerso);

        String url = Api.URL+"/v1/user/supplyInfoForXH";
        postCompleteInfo(url,map);

    }

    public void postCompleteInfo(String url, HashMap<String, String> map) {
        //??????OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //2.??????request
        //2.1??????requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:",String.valueOf(params));
        Set<String> keys = map.keySet();
        for (String key:keys)
        {
            params.put(key,map.get(key));

        }


        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
                .build();
        //3.???request?????????call
        Call call = Api.ok().newCall(request);

        //call????????????
        call.enqueue(new Callback() {   //??????
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this, "??????????????????");
                        Log.d("json", "??????????????????");
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
                            Intent intent = new Intent(StarFireCompleteInformationActivity.this,UserInterFaceActivity.class);
                            startActivity(intent);
                            ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,"????????????");
                            ActivityCollector.removeActivity(StarFireCompleteInformationActivity.this);
                            finish();
                        }else if (ecologyCompleteInformationResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(StarFireCompleteInformationActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(StarFireCompleteInformationActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(StarFireCompleteInformationActivity.this,ecologyCompleteInformationResponse.getMsg());
                        }


                    }

                });


            }
        });
    }

    /**
     * ??????
     * @param popupView
     */
    public void showPuPopWindowFront(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.ecology_popview_item, null);
            // ??????2,3?????????popupwindow??????????????????
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // ????????????????????? ????????????????????????????????????
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            // ???????????????????????????????????????????????????X????????????Y??????1???0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // ??????popupWindow????????????????????????????????????????????????????????????????????????
            popupWindow.showAtLocation(StarFireCompleteInformationActivity.this.findViewById(R.id.starfire_info_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    checkPermissionAndCameraFront();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbumFront();
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
     * ??????
     * @param popupView
     */
    public void showPuPopWindowReverse(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.ecology_popview_item, null);
            // ??????2,3?????????popupwindow??????????????????
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // ????????????????????? ????????????????????????????????????
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            // ???????????????????????????????????????????????????X????????????Y??????1???0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // ??????popupWindow????????????????????????????????????????????????????????????????????????
            popupWindow.showAtLocation(StarFireCompleteInformationActivity.this.findViewById(R.id.starfire_info_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    checkPermissionAndCameraReverse();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.ecology_popwindow_tv_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPermissionAndAlbumReverse();
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
     * ??????
     * ????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndCameraFront() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            openCameraFront();

        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_FRONT_REQUEST_CODE);
        }
    }

    /**
     * ??????
     * ????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndCameraReverse() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            openCameraReverse();

        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REVERSE_REQUEST_CODE);
        }
    }


    /**
     * ??????
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndAlbumFront() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //????????????????????????????????????

            openAlbumFront();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(StarFireCompleteInformationActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_FRONT_REQUEST_CODE);
        }
    }

    /**
     * ??????
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndAlbumReverse() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //????????????????????????????????????

            openAlbumReverse();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(StarFireCompleteInformationActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REVERSE_REQUEST_CODE);
        }
    }

    /**
     * ??????
     * ??????????????????
     */
    private void openCameraFront() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFrontFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // ??????android 10
                photoUri = createImageFrontUri();
            } else {
                try {
                    photoFrontFile = createImageFrontFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFrontFile != null) {
                    mCameraImagePathFront = photoFrontFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFrontFile);
                    } else {
                        photoUri = Uri.fromFile(photoFrontFile);
                    }
                }
            }

            mCameraFrontUri = photoUri;
            Log.d("opencamera", String.valueOf(mCameraFrontUri));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_FRONT_REQUEST_CODE);
            }
        }
    }

    /**
     * ??????
     * ??????????????????
     */
    private void openCameraReverse() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoReverseFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // ??????android 10
                photoUri = createImageReverseUri();
            } else {
                try {
                    photoReverseFile = createImageReverseFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoReverseFile != null) {
                    mCameraImagePathReverse = photoReverseFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoReverseFile);
                    } else {
                        photoUri = Uri.fromFile(photoReverseFile);
                    }
                }
            }

            mCameraReverseUri = photoUri;
            Log.d("opencamera", String.valueOf(mCameraReverseUri));
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REVERSE_REQUEST_CODE);
            }
        }
    }

    /**
     * ??????
     * ????????????
     */
    private void openAlbumFront(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //???????????????????????????
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_FRONT_REQUEST_CODE);
    }

    /**
     * ??????
     * ????????????
     */
    private void openAlbumReverse(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //???????????????????????????
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REVERSE_REQUEST_CODE);
    }

    /**
     * ??????
     * ??????????????????????????????
     * Api19?????????Android4.4?????????
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKatFront(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //??????document???Uri??????????????????document id??????
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//?????????????????????id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                photoIdFront =docId.split(":")[1];//id="26"
                baseUriFront=Uri.parse("content://media/external/images/media");
                mIvPhotoIdFrontTrue.setImageURI(Uri.withAppendedPath(baseUriFront, photoIdFront));
                //????????????Uri?????????????????????content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
                displayImageFront(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //?????????content?????????Uri??????????????????????????????
            imagePath = getImagePath(uri,null);
            displayImageFront(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
            displayImageFront(imagePath);
        }

        mAlbumFrontUri = uri;
    }


    /**
     * ??????
     * ??????????????????????????????
     * Api19?????????Android4.4?????????
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKatReverse(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //??????document???Uri??????????????????document id??????
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//?????????????????????id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                photoIdReverse =docId.split(":")[1];//id="26"
                baseUriReverse=Uri.parse("content://media/external/images/media");
                mIvPhotoIdReverseTrue.setImageURI(Uri.withAppendedPath(baseUriReverse, photoIdReverse));
                //????????????Uri?????????????????????content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
                displayImageReverse(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //?????????content?????????Uri??????????????????????????????
            imagePath = getImagePath(uri,null);
            displayImageReverse(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
            displayImageReverse(imagePath);
        }

        mAlbumReverseUri = uri;
    }

    /**
     * ??????
     * ??????????????????????????????
     * Android4.4????????????
     * @param data
     */
    private void handleImageBeforeKitKatFront(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImageFront(imagePath);
    }

    /**
     * ??????
     * ??????????????????????????????
     * Android4.4????????????
     * @param data
     */
    private void handleImageBeforeKitKatReverse(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImageReverse(imagePath);
    }

    /**
     * ??????getContentResolver??????query??????????????????
     * ????????????????????????????????????
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //??????Uri???selection???????????????????????????
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    /**
     * ??????
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageFrontUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ??????
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageReverseUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ??????
     * ???????????????????????????
     */
    private File createImageFrontFile() throws IOException {
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
     * ??????
     * ???????????????????????????
     */
    private File createImageReverseFile() throws IOException {
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
     * ??????
     * ??????????????????????????????
     * @param imagePath
     */
    private void displayImageFront(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvPhotoIdFrontTrue.setImageBitmap(bitmap);
        }else {
            Toast.makeText(StarFireCompleteInformationActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ??????
     * ??????????????????????????????
     * @param imagePath
     */
    private void displayImageReverse(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvPhotoIdReverseTrue.setImageBitmap(bitmap);
        }else {
            Toast.makeText(StarFireCompleteInformationActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ??????
     * ====?????? uri???content???file???????????????????????????file
     */
    public static File createFileFront(Context context, Uri uri) {
        //https://www.jb51.net/article/181745.htm

        //?????????  ????????????????????????????????????????????????
        File folder = context.getExternalCacheDir();//???Android>data>??????>???cache??????????????????????????????????????????
        //File folder = this.getExternalFilesDir("image");//???Android>data>??????>???files???image???????????????????????????????????????????????????
        if (!folder.exists()) {
            folder.mkdir();
        }
        //file??????
        File file = new File(folder.getAbsolutePath() + File.separator + "IdCardFront.jpg");

        try {
            //??????uri?????????????????????
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            //==??????????????????file???
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

    /**
     * ??????
     * ====?????? uri???content???file???????????????????????????file
     */
    public static File createFileReverse(Context context, Uri uri) {
        //https://www.jb51.net/article/181745.htm

        //?????????  ????????????????????????????????????????????????
        File folder = context.getExternalCacheDir();//???Android>data>??????>???cache??????????????????????????????????????????
        //File folder = this.getExternalFilesDir("image");//???Android>data>??????>???files???image???????????????????????????????????????????????????
        if (!folder.exists()) {
            folder.mkdir();
        }
        //file??????
        File file = new File(folder.getAbsolutePath() + File.separator + "IdCardReverse.jpg");

        try {
            //??????uri?????????????????????
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            //==??????????????????file???
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
     * ???????????????
     * @param view
     */
    public void hideSoftKeyboard(View view){
        //????????????view????????? ???????????????context,LoginActivity.this???????????????
        //view????????????
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
//                ????????????????????????
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
     * ??????
     * ====?????? bitmap????????????file
     */
    public static File createFilePic(Context context, Bitmap bitmap) {
        //https://www.jb51.net/article/181745.htm

        //?????????  ????????????????????????????????????????????????
        File folder = context.getExternalCacheDir();//???Android>data>??????>???cache??????????????????????????????????????????
        //File folder = this.getExternalFilesDir("image");//???Android>data>??????>???files???image???????????????????????????????????????????????????
        if (!folder.exists()) {
            folder.mkdir();
        }

        SimpleDateFormat df = null;//?????????????????????android???????????????????????????????????????????????????????????????
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        String filename = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filename = df.format(new Date());
        }
        //file??????
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
     * ??????????????????
     * @param
     * @param
     * @return
     */
    public static Bitmap compressImage1(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// ?????????ture,?????????????????????????????????????????????????????????
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 576, 960);// ??????????????????480x800??????????????????

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;// ??????????????????????????????????????????false

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