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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.bean.PayEarnestBailResponse;
import com.example.daoqimanagement.bean.PaymentInfoResponse;
import com.example.daoqimanagement.bean.UploadPhotoDateResponse;
import com.example.daoqimanagement.bean.UploadPhotoResponse;
import com.example.daoqimanagement.dialog.CheckPhotoBitmapDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class PayEarnestMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    // ?????????????????????requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //???????????????????????????requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE = 0x00000013;
    //??????????????????????????????requestCode
    private static final int CAMERA_REQUEST_CODE = 1;
    //??????????????????????????????requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 2;

    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePath;
    //???????????????????????????uri
    private Uri mCameraUri;

    // ?????????Android 10????????????
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private LinearLayout mLlCamera,mLlArrowLeft;
    private PopupWindow popupWindow;

    private Animation animation;

    private SimpleDraweeView  mIvPhotoTrue;
    private ImageView mIvPhotoFalse;
    private TextView mTvPhotoToast,mTvPhotoAgain,mTvBusinessName,mTvBankNo,mTvBank,mTvAmount,mTvBigVoucher;
    private Button mBtnSubmit;

    DialogTokenIntent dialogTokenIntent = null;
    Bitmap bitmap;
    String productid="00000";
    String voucher = "00000";
    String type = "00000",prepareId="00000";
    int hospitalid;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        initView();
        mTvPhotoAgain.setVisibility(View.GONE);
        mLlCamera.setOnClickListener(this);
        mTvPhotoAgain.setOnClickListener(this);
        mLlArrowLeft.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        Intent intent = getIntent();
        hospitalid = intent.getIntExtra("hospitalId",0);
        type = intent.getStringExtra("type");
        prepareId= intent.getStringExtra("prepareId");
        productid = intent.getStringExtra("productId");
        getPaymentInfoRes(Api.URL+"/v1/payment/info?type=2&hospitalId="+String.valueOf(hospitalid));



    }

    public void initView(){
        setContentView(R.layout.activity_pay_earnest_money);
        mLlCamera = findViewById(R.id.pay_earnest_ll_camera);
        mLlArrowLeft = findViewById(R.id.pay_earnest_ll_arrowleft);
        mIvPhotoTrue = findViewById(R.id.pay_earnest_iv_photo_true);
        mIvPhotoFalse = findViewById(R.id.pay_earnest_iv_photo_false);
        mTvPhotoToast = findViewById(R.id.pay_earnest_tv_photo);
        mTvPhotoAgain = findViewById(R.id.pay_earnest_tv_photo_again);
        mBtnSubmit = findViewById(R.id.pay_earnest_btn_submit);
        mTvBusinessName = findViewById(R.id.pay_earnest_tv_enterprise);
        mTvBankNo = findViewById(R.id.pay_earnest_tv_bank_number);
        mTvBank = findViewById(R.id.pay_earnest_tv_bank);
        mTvAmount = findViewById(R.id.pay_earnest_tv_money_small);
        mTvBigVoucher = findViewById(R.id.pay_earnest_tv_money_capital);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay_earnest_ll_camera:
                showPuPopWindow(view);
                break;
            case R.id.pay_earnest_tv_photo_again:
                showPuPopWindow(view);
                break;
            case R.id.pay_earnest_ll_arrowleft:
                ActivityCollector.removeActivity(PayEarnestMoneyActivity.this);
                finish();
                break;
            case R.id.pay_earnest_btn_submit:
                Log.d("voucher", type);
                Log.d("voucher", GetSharePerfenceSP.getToken(this));
                Log.d("voucher", GetSharePerfenceSP.getUid(this));
                Log.d("voucher", voucher);
                Log.d("voucher",String.valueOf(hospitalid));
                if (TextUtils.isEmpty(productid)){
                    Log.d("voucher","null");
                }else {
                    Log.d("voucher",productid);
                }


                if (voucher.equals("00000")){
                    ToastUtils.showTextToast2(PayEarnestMoneyActivity.this,"???????????????/????????????");
                }else{

                    if (type.equals("join")){
                        if(!productid.equals("00000")){
                            payBailPost(voucher, String.valueOf(hospitalid),productid);
                        }
                    }else if (type.equals("delay")){
                        payDelayPost(voucher,prepareId);
                    }

                }


        }
    }

    public void showPuPopWindow(View popupView){
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
            popupWindow.showAtLocation(PayEarnestMoneyActivity.this.findViewById(R.id.pay_earnest_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
     * ??????????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CAMERA_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????????????????????????????????
                    openCamera();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_ALBUM_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //??????????????????????????????
                    openAlbum();
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
            case CAMERA_REQUEST_CODE:

                if (resultCode == RESULT_OK){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        bitmap = BitmapFactory.decodeFile(getPath(this, mCameraUri));
                        if (isAndroidQ){
                            bitmap = compressImage1(getPath(this, mCameraUri));
                        }else {
                            bitmap = compressImage1(mCameraImagePath);;
                        }


                    }
                    File file = createFilePic(this, bitmap);
                    postUpLoadPhoto(Api.URL+"/upload", file);
                }else {
                    Toast.makeText(this,"????????????",Toast.LENGTH_LONG).show();
                }
                break;
            case CHOOSE_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    //?????????????????????
                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
                        //Android 4.4??????????????????????????????
                        handleImageOnKitKat(data);
//                        bitmap = BitmapFactory.decodeFile(getPath(this, mCameraUri));
                        bitmap = compressImage1(getPath(this, mCameraUri));
                        File file = createFilePic(this, bitmap);
                        postUpLoadPhoto(Api.URL+"/upload", file);
                    } else {
                        //Android 4.4???????????????????????????
                        handleImageBeforeKitKat(data);
                    }


                }
                break;
            default:
                break;
        }
    }



    public void payBailPost(String voucher,String hospitalid,String productid){
        HashMap<String, String> map = new HashMap<>();
        map.put("voucher", voucher);//18158188052
        map.put("hospitalid", hospitalid);//18158188052
        map.put("productid", productid);//18158188052


        String url = Api.URL+"/v1/prepare/addBail";
        postPayBail(url,map);

    }

    public void payDelayPost(String voucher,String prepareId){
        HashMap<String, String> map = new HashMap<>();
        map.put("voucher", voucher);//18158188052
        map.put("prepareId", prepareId);//18158188052



        String url = Api.URL+"/v1/delay/addFee";
        postPayBail(url,map);

    }

    public void postPayBail(String url, HashMap<String, String> map) {
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
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
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
                        ToastUtils.showTextToast2(PayEarnestMoneyActivity.this, "??????????????????");
                        Log.d("json", "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                Log.d("sad123ds", res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        PayEarnestBailResponse payEarnestBailResponse = gson.fromJson(res,PayEarnestBailResponse.class);
                        if (payEarnestBailResponse.getCode() == 0){
                            Intent intent = new Intent();
                            setResult(RESULT_OK,intent);
                            ToastUtils.showTextToast2(PayEarnestMoneyActivity.this,"????????????");
                            ActivityCollector.removeActivity(PayEarnestMoneyActivity.this);
                            finish();
                        }else if (payEarnestBailResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PayEarnestMoneyActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PayEarnestMoneyActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(PayEarnestMoneyActivity.this,payEarnestBailResponse.getMsg());
                        }


                    }

                });


            }
        });
    }



    private void getPaymentInfoRes(String url) {



        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid", GetSharePerfenceSP.getUid(this))
                .get()
                .build();
        //3.???request?????????call
        Call call = Api.ok().newCall(request);
        L.e(String.valueOf(call));
        //4.??????call
//        ????????????
//        Response response = call.execute();

        //????????????
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(PayEarnestMoneyActivity.this, "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);




                final PaymentInfoResponse paymentInfoResponse = new Gson().fromJson(res,PaymentInfoResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (paymentInfoResponse.getCode() == 0) {
                            mTvBusinessName.setText(paymentInfoResponse.getData().getBusinessName());
                            mTvBankNo.setText(paymentInfoResponse.getData().getBankNo());
                            mTvBank.setText(paymentInfoResponse.getData().getBank());
                            mTvAmount.setText(paymentInfoResponse.getData().getAmount());
                            mTvBigVoucher.setText(paymentInfoResponse.getData().getBigVoucher());

                        } else if (paymentInfoResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PayEarnestMoneyActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PayEarnestMoneyActivity.this, LoginActivity.class);
                                       ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = paymentInfoResponse.getMsg();
                            ToastUtils.showTextToast2(PayEarnestMoneyActivity.this, msg);
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
    public void postUpLoadPhoto(String url, final File file) {
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
                        ToastUtils.showTextToast2(PayEarnestMoneyActivity.this, "??????????????????");
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
                            ToastUtils.showTextToast2(PayEarnestMoneyActivity.this,"??????????????????????????????");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                final String data = uploadPhotoResponse.getData().toString();

                                UploadPhotoDateResponse uploadPhotoDateResponse = new Gson().fromJson(data, UploadPhotoDateResponse.class);
                                voucher = String.valueOf(uploadPhotoDateResponse.getPictureId());

                                mTvPhotoAgain.setVisibility(View.VISIBLE);
                                mIvPhotoTrue.setVisibility(View.VISIBLE);
                                mLlCamera.setVisibility(View.GONE);
                                mIvPhotoTrue.setImageBitmap(bitmap);

                                mIvPhotoTrue.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View view) {
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(PayEarnestMoneyActivity.this,R.style.CustomDialogPhoto, bitmap);
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
                                    dialogTokenIntent = new DialogTokenIntent(PayEarnestMoneyActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(PayEarnestMoneyActivity.this, LoginActivity.class);
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
     * ????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndCamera() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            openCamera();

        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //????????????????????????????????????

            openAlbum();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(PayEarnestMoneyActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE);
        }
    }

    /**
     * ????????????
     */
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        //???????????????????????????
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO_REQUEST_CODE);
    }

    /**
     * ??????????????????
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // ??????android 10
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
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
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
     * ??????????????????????????????
     * Api19?????????Android4.4?????????
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (isDocumentUri(this,uri)){
            //??????document???Uri??????????????????document id??????
            String docId= DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];//?????????????????????id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                String id=docId.split(":")[1];//id="26"
                Uri baseUri=Uri.parse("content://media/external/images/media");
                mIvPhotoTrue.setImageURI(Uri.withAppendedPath(baseUri, id));
                //????????????Uri?????????????????????content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
                displayImage(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //?????????content?????????Uri??????????????????????????????
            imagePath = getImagePath(uri,null);
            displayImage(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
            displayImage(imagePath);
        }

        mCameraUri = uri;

    }

    /**
     * ??????????????????????????????
     * Android4.4????????????
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
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
     * ??????????????????????????????
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mIvPhotoTrue.setImageBitmap(bitmap);
        }else {
            Toast.makeText(PayEarnestMoneyActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ???????????????????????????
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