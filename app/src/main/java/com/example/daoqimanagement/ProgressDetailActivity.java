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
import android.app.AlertDialog;
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
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.adapter.ProgressDetailAppendixListAdapter;
import com.example.daoqimanagement.adapter.ProgressDetailCommentAdapter;
import com.example.daoqimanagement.adapter.ProgressDetailPictureListAdapter;
import com.example.daoqimanagement.adapter.UpDataProgressAddFileAdapter;
import com.example.daoqimanagement.adapter.UpDataProgressAddPicAdapter;
import com.example.daoqimanagement.bean.ProgressCommentAddResponse;
import com.example.daoqimanagement.bean.ProgressDetailCommentResponse;
import com.example.daoqimanagement.bean.ProgressDetailResponse;
import com.example.daoqimanagement.bean.UpLoadFileResponse;
import com.example.daoqimanagement.bean.UploadPhotoDateResponse;
import com.example.daoqimanagement.bean.UploadPhotoResponse;
import com.example.daoqimanagement.dialog.CheckPhotoURLDialog;
import com.example.daoqimanagement.dialog.CheckPhotoBitmapDialog;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.FileUtils;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

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

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.provider.DocumentsContract.isDocumentUri;

public class ProgressDetailActivity extends AppCompatActivity {

    private TextView mTvHospitalName,mTvLevel,mTvAreaName,mTvNature,mTvCreateTime,mTvTrueName,mTvDesc,mTvRemarks,mTvNeedHelp;

    private RelativeLayout mRlFinish,mRlAddFileNone,mRlAddPic,mRlAddFile;
    private Button mBtnSubmit;
    private EditText mEtDesc;
    private RecyclerView mRcPictureList,mRcAppendixList, mRcCommentList,mRcAddPic,mRcAddFile;
    DialogTokenIntent dialogTokenIntent = null;
    ProgressDetailPictureListAdapter progressDetailPictureListAdapter;
    ProgressDetailAppendixListAdapter progressDetailAppendixListAdapter;
    ProgressDetailCommentAdapter progressDetailCommentAdapter;
    UpDataProgressAddFileAdapter upDataProgressAddFileAdapter;
    UpDataProgressAddPicAdapter upDataProgressAddPicAdapter;

    private List<Bitmap> listpic = new ArrayList<Bitmap>();
    private List<String> listfile = new ArrayList<String>();

    List<String> listPicString = new ArrayList<>();
    List<String> listFileString = new ArrayList<>();

    AlertDialog spotDialog;
    private PopupWindow popupWindow;
     Animation animation;
    String filePath;
    String scheduleId;
    File photoFile ;
    String picid;
    Uri picbaseUri;
    String upLoadFileName;
    // ?????????????????????requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    //???????????????????????????requestCode
    private static final int PERMISSION_ALBUM_REQUEST_CODE = 0x00000013;
    //???????????????????????????requestCode
    private static final int PERMISSION_FILE_REQUEST_CODE = 0x00000014;
    //??????????????????????????????requestCode
    private static final int CAMERA_REQUEST_CODE = 2;
    //??????????????????????????????requestCode
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 3;
    //???????????????????????????????????????requestCode
    private static final int CHOOSE_FILE_REQUEST_CODE = 4;

    private static final int CHOOSE_HOSPITAL_REQUEST_CODE = 5;


    // ?????????Android 10????????????
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePath;
    //???????????????????????????uri
    private Uri mCameraUri,mChooseUri;

    Bitmap bitmapCamera,bitmapChoose;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_progress_detail);
        initView();
        initRecyclePic();
        initRecycleFile();
        mRlAddFileNone.setVisibility(View.GONE);
        Intent intent =getIntent();
        scheduleId = intent.getStringExtra("scheduleId");
        getProgressDetail(Api.URL+"/v1/schedule/detail?scheduleId="+scheduleId);
        getProgressDetailComment(Api.URL+"/v1/schedule/detail?scheduleId="+scheduleId);
        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(ProgressDetailActivity.this);
                finish();
            }
        });

        mRlAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                checkPermissionAndFile();
            }
        });
        upDataProgressAddFileAdapter.setOnItemClickItemListener(new UpDataProgressAddFileAdapter.OnItemClickItemListener() {
            @Override
            public void onItemClickItem(int position) {
                if (position < 3){
                    upDataProgressAddFileAdapter.removeData(position);
//                    ToastUtils.showTextToast2(UpDateProgressActivity.this,"asdasd");
                    mRlAddFile.setEnabled(true);
                    mRlAddFileNone.setVisibility(View.GONE);
                    listFileString.remove(position);
                }
            }
        });

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

        mRlAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                showPuPopWindowAddPic(view);
            }
        });

        mBtnSubmit.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                hideSoftKeyboard(view);
                String listStringPic = String.valueOf(listPicString).replace(" ","").replace("[","").replace("]","");
                String listStringFile = String.valueOf(listFileString).replace(" ","").replace("[","").replace("]","");


                String desc;
                String remarks;
                String needHelp;
                int scheduleid = Integer.parseInt(scheduleId);

                if (mEtDesc.getText().toString().trim().length() > 0 ){
                    desc = mEtDesc.getText().toString().trim();
                }else {
                    desc = "";
                }

                Log.d("listpicestring", listStringPic);
                Log.d("listfileestring", listStringFile);
                Log.d("listfileestring", desc);
                if (TextUtils.isEmpty(desc)){
                    ToastUtils.showTextToast2(ProgressDetailActivity.this,"?????????????????????");
                }else {
                    postResCommentAdd(Api.URL+"/v1/comment/add",scheduleid,desc,listStringPic,listStringFile);
                }

            }
        });



    }



    public void initView(){
        mTvHospitalName = findViewById(R.id.progress_detail_tv_hospitalName);
        mTvLevel = findViewById(R.id.progress_detail_tv_level);
        mTvAreaName = findViewById(R.id.progress_detail_tv_areaName);
        mTvNature = findViewById(R.id.progress_detail_tv_nature);
        mTvCreateTime = findViewById(R.id.progress_detail_tv_createTime);
        mTvTrueName = findViewById(R.id.progress_detail_tv_truename);
        mTvDesc = findViewById(R.id.progress_detail_tv_desc);
        mTvRemarks = findViewById(R.id.progress_detail_tv_remarks);
        mTvNeedHelp = findViewById(R.id.progress_detail_tv_needHelp);
        mRlFinish = findViewById(R.id.progress_detail_rl_finish);
        mRcPictureList = findViewById(R.id.progress_detail_rc_pictures);
        mRcAppendixList = findViewById(R.id.progress_detail_rc_appendixs);
        mRcCommentList = findViewById(R.id.progress_detail_rc_comment);
        mRcAddPic= findViewById(R.id.progress_detail_rc_addPic);
        mRcAddFile= findViewById(R.id.progress_detail_rc_addFile);
        mRlAddFileNone= findViewById(R.id.progress_detail_rl_addFile_none);
        mRlAddPic = findViewById(R.id.progress_detail_rl_addPic);
        mRlAddFile = findViewById(R.id.progress_detail_rl_addFile);
        mBtnSubmit= findViewById(R.id.progress_detail_btn_submit);
        mEtDesc = findViewById(R.id.progress_detail_et_comment);

    }

    private void initRecyclePic() {
        // ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRcAddPic.setLayoutManager(linearLayoutManager);
//   ??????????????????????????????????????????????????????
//        list = initData();
        upDataProgressAddPicAdapter = new UpDataProgressAddPicAdapter(ProgressDetailActivity.this, listpic);
        mRcAddPic.setAdapter(upDataProgressAddPicAdapter);
//   ????????????
        mRcAddPic.setItemAnimator(new DefaultItemAnimator());
    }


    private void initRecycleFile() {
        // ????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgressDetailActivity.this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRcAddFile.setLayoutManager(linearLayoutManager);
//   ??????????????????????????????????????????????????????
//        list = initData();
        upDataProgressAddFileAdapter = new UpDataProgressAddFileAdapter(ProgressDetailActivity.this, listfile);
        mRcAddFile.setAdapter(upDataProgressAddFileAdapter);
//   ????????????
        mRcAddFile.setItemAnimator(new DefaultItemAnimator());
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

            case PERMISSION_FILE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //???????????????????????????????????????

                    openFileManager();
                } else {
                    //?????????????????????????????????
                    Toast.makeText(this,"????????????????????????????????????",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

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
                    File file = createFilePic(ProgressDetailActivity.this, bitmapCamera);
                    postUpLoadPhotoCamera(Api.URL+"/upload", file);
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
                        bitmapChoose = compressImage1(getPath(this,mChooseUri));
                        File file = createFilePic(ProgressDetailActivity.this,bitmapChoose);
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



    /**
     *
     * @param popupView
     */
    public void showPuPopWindowAddPic(View popupView){
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
            popupWindow.showAtLocation(ProgressDetailActivity.this.findViewById(R.id.progress_detail_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
     * android10??????uri???file
     * @param uri
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static File uriToFileApiQ(Uri uri,Context context) {
        File file = null;
        //android10????????????
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //??????????????????????????????
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                try {
                    InputStream is = contentResolver.openInputStream(uri);
                    File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                    FileOutputStream fos = new FileOutputStream(cache);
                    android.os.FileUtils.copy(is, fos);
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

    private void getProgressDetail(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

        //1.??????okhttp??????
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.??????request
        //2.1??????requestbody

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
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                final ProgressDetailResponse progressDetailResponse  = new Gson().fromJson(res,ProgressDetailResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (progressDetailResponse.getCode() == 0) {
                            mTvHospitalName.setText(progressDetailResponse.getData().getHospitalName());
                            mTvLevel.setText(progressDetailResponse.getData().getLevel());
                            mTvAreaName.setText(progressDetailResponse.getData().getAreaName());
                            mTvNature.setText(progressDetailResponse.getData().getNature());
                            mTvTrueName.setText(progressDetailResponse.getData().getTruename());
                            mTvDesc.setText(progressDetailResponse.getData().getDesc());
                            mTvRemarks.setText(progressDetailResponse.getData().getRemarks());
                            mTvNeedHelp.setText(progressDetailResponse.getData().getNeedHelp());
                            if (progressDetailResponse.getData().getCreateTime() == null){
                                mTvCreateTime.setText("");
                            }else {
                                String createAt1 = progressDetailResponse.getData().getCreateTime();
                                String createAt= createAt1.substring(0,16);
                                StringBuffer buffer = new StringBuffer(createAt);
                                buffer.replace(4,5,"???");
                                buffer.replace(7,8,"???");
                                buffer.replace(10,11,"???");
                                buffer.insert(11," ");
                                mTvCreateTime.setText(buffer);
                            }


                            if (progressDetailResponse.getData().getPictures() != null){
                                progressDetailPictureListAdapter = new ProgressDetailPictureListAdapter(ProgressDetailActivity.this,progressDetailResponse.getData().getPictures());
                                progressDetailAppendixListAdapter = new ProgressDetailAppendixListAdapter(ProgressDetailActivity.this,progressDetailResponse.getData().getAppendixs());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgressDetailActivity.this){
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                };
                                mRcPictureList.setLayoutManager(linearLayoutManager);
                                mRcPictureList.setAdapter(progressDetailPictureListAdapter);
                                progressDetailPictureListAdapter.setOnItemClickListener(new ProgressDetailPictureListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String path, int position) {
//                                        Intent intent = new Intent(ProgressDetailActivity.this,CheckPhotoUrlActivity.class);
//                                        intent.putExtra("path",Api.URL+path);
////                                        Log.d("path", Api.URL+path);
//                                        startActivity(intent);

                                        CheckPhotoURLDialog checkPhotoURLDialog = new CheckPhotoURLDialog(ProgressDetailActivity.this,R.style.CustomDialogPhoto,Api.URL+path);
                                        checkPhotoURLDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        checkPhotoURLDialog.show();
                                        if (checkPhotoURLDialog.isShowing()){
                                            final Window window=getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                window.setStatusBarColor(Color.parseColor("#000000"));
                                            }
                                        }
                                        checkPhotoURLDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {

                                                final Window window=getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    window.setStatusBarColor(Color.parseColor("#ffffff"));
                                                }

                                            }
                                        });


                                        Window dialogWindow = checkPhotoURLDialog.getWindow();
                                        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
                                        dialogWindow.setGravity(Gravity.BOTTOM);
                                        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                        lp.y = 0;
                                        dialogWindow.setAttributes(lp);

                                    }
                                });
                            }

                            if (progressDetailResponse.getData().getAppendixs() != null){
                                progressDetailAppendixListAdapter = new ProgressDetailAppendixListAdapter(ProgressDetailActivity.this,progressDetailResponse.getData().getAppendixs());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgressDetailActivity.this){
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                };
                                mRcAppendixList.setLayoutManager(linearLayoutManager);
                                mRcAppendixList.setAdapter(progressDetailAppendixListAdapter);
                                progressDetailAppendixListAdapter.setOnItemClickListener(new ProgressDetailAppendixListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String path, int position, View view) {
                                        Log.d("path", Api.URL + path);
                                        final File folder = ProgressDetailActivity.this.getExternalFilesDir(null);
                                        final String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                                        if (!folder.exists()) {
                                            folder.mkdir();
                                        }

                                        if (fileIsExists(String.valueOf(folder)+"/download/" + fileName)) {
//

                                            filePath = (folder)+"/download/" + fileName;
                                            showPuPopWindow(view);

//

                                        } else {
                                            spotDialog = new SpotsDialog.Builder().setContext(ProgressDetailActivity.this).setTheme(R.style.SpotDialogCustom)
                                                    .setCancelable(false).build();
                                            spotDialog.show();
                                            DownLoadFile(Api.URL + path,(folder)+"/download/" + fileName);



                                        }
                                    }
                                });
                            }


                        } else if (progressDetailResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                       ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = progressDetailResponse.getMsg();
                            ToastUtils.showTextToast2(ProgressDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    private void getProgressDetailComment(String url) {

//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        Log.e("params:", String.valueOf(params));
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            params.put(key, map.get(key));
//
//        }

        //1.??????okhttp??????
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.??????request
        //2.1??????requestbody

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
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                final ProgressDetailCommentResponse progressDetailCommentResponse = new Gson().fromJson(res,ProgressDetailCommentResponse.class);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (progressDetailCommentResponse.getCode() == 0) {


                            progressDetailCommentAdapter = new ProgressDetailCommentAdapter(ProgressDetailActivity.this,progressDetailCommentResponse.getData().getComment(),ProgressDetailActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgressDetailActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            mRcCommentList.setLayoutManager(linearLayoutManager);
                            mRcCommentList.setAdapter(progressDetailCommentAdapter);


                        } else if (progressDetailCommentResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = progressDetailCommentResponse.getMsg();
                            ToastUtils.showTextToast2(ProgressDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    protected void postResCommentAdd(String url, int scheduleid, String desc, String pic, String appendix ) {
        //1.??????okhttp??????
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.??????request
        //2.1??????requestbody

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
            jsonObject.put("scheduleId",scheduleid);
            jsonObject.put("desc",desc);
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
        //3.???request?????????call
        Call call = Api.ok().newCall(request);
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

                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
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
                        ProgressCommentAddResponse progressCommentAddResponse = gson.fromJson(res,ProgressCommentAddResponse.class);
                        if (progressCommentAddResponse.getCode() == 0){
                            ToastUtils.showTextToast2(ProgressDetailActivity.this,"??????????????????");
                            getProgressDetailComment(Api.URL+"/v1/schedule/detail?scheduleId="+scheduleId);
                            upDataProgressAddPicAdapter.notifyDataSetChanged();
                            upDataProgressAddFileAdapter.notifyDataSetChanged();
                            listPicString.clear();
                            listFileString.clear();
                            listfile.clear();
                            listpic.clear();
                            mEtDesc.setText("");
                            mEtDesc.clearFocus();
                            Log.d("listPicString", String.valueOf(listPicString));

                        } else if (progressCommentAddResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(ProgressDetailActivity.this,progressCommentAddResponse.getMsg());
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
    public void postUpLoadPhotoCamera(String url, final File file) {
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
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
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
                            ToastUtils.showTextToast2(ProgressDetailActivity.this,"??????????????????????????????");

                        }else {

                            UploadPhotoResponse uploadPhotoResponse = new Gson().fromJson(json, UploadPhotoResponse.class);
                            if (uploadPhotoResponse.getCode() == 0) {
                                upDataProgressAddPicAdapter.addData(listpic.size(),bitmapCamera);
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
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(ProgressDetailActivity.this,R.style.CustomDialogPhoto,bitmap);
                                        checkPhotoBitmapDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                                        checkPhotoBitmapDialog.show();
                                        checkPhotoBitmapDialog.setCancelable(true);
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
                                    dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(ProgressDetailActivity.this,uploadPhotoResponse.getMsg());
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
    public void postUpLoadPhotoAlbum(String url, final File file) {
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
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
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
                            ToastUtils.showTextToast2(ProgressDetailActivity.this,"??????????????????????????????");

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
                                        CheckPhotoBitmapDialog checkPhotoBitmapDialog = new CheckPhotoBitmapDialog(ProgressDetailActivity.this,R.style.CustomDialogPhoto,bitmap);
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
                                    dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(ProgressDetailActivity.this,uploadPhotoResponse.getMsg());
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
    public void postUpLoadFile(String url, final File file) {
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
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
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
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "??????????????????");
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
                            ToastUtils.showTextToast2(ProgressDetailActivity.this,"??????????????????????????????");

                        }else {

                            UpLoadFileResponse upLoadFileResponse = new Gson().fromJson(json, UpLoadFileResponse.class);
                            if (upLoadFileResponse.getCode() == 0) {

                                upDataProgressAddFileAdapter.addData(listfile.size(), upLoadFileName);
                                if (listfile.size() > 2){
                                    mRlAddFile.setEnabled(false);
                                    mRlAddFileNone.setVisibility(View.VISIBLE);
                                }

                                listFileString.add(String.valueOf(upLoadFileResponse.getData().getFileId()));

                            }else if (upLoadFileResponse.getCode() == 10009){
                                if (dialogTokenIntent == null) {
                                    dialogTokenIntent = new DialogTokenIntent(ProgressDetailActivity.this, R.style.CustomDialog);
                                    dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                        @Override
                                        public void OnConfirm(DialogTokenIntent dialog) {
                                            Intent intent = new Intent(ProgressDetailActivity.this, LoginActivity.class);
                                            ActivityCollector.finishAll();
                                            startActivity(intent);

                                        }
                                    }).show();

                                    dialogTokenIntent.setCanceledOnTouchOutside(false);
                                    dialogTokenIntent.setCancelable(false);
                                }
                            }else {
                                ToastUtils.showTextToast2(ProgressDetailActivity.this,upLoadFileResponse.getMsg());
                            }
                        }






                    }

                });


            }
        });
    }

    public void showPuPopWindow(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.file_popview_item, null);
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
            popupWindow.showAtLocation(ProgressDetailActivity.this.findViewById(R.id.progress_detail_bottom_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });

            popupView.findViewById(R.id.file_popwindow_tv_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FileUtils.isImageFileType1(filePath) == true){
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//???????????????
                        sharingIntent.setType("image/*");//????????????????????????type
//                    sharingIntent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    FileProvider.getUriForFile(getBaseContext(), getPackageName() +".fileprovider",
                                            new File(filePath)));

                        }else {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                        }

                        startActivity(Intent.createChooser(sharingIntent, "??????"));

                    }else {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//???????????????
                        sharingIntent.setType("*/*");//????????????????????????type
//                    sharingIntent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    FileProvider.getUriForFile(getBaseContext(), getPackageName() +".fileprovider",
                                            new File(filePath)));

                        }else {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                        }

                        startActivity(Intent.createChooser(sharingIntent, "??????"));

                    }
                    popupWindow.dismiss();

                }
            });

            popupView.findViewById(R.id.file_popwindow_tv_check_path).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                   ToastUtils.showTextToast3(ProgressDetailActivity.this,filePath);
                    Toast.makeText(ProgressDetailActivity.this,filePath,Toast.LENGTH_LONG).show();
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.file_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });

        }
    }


    /**
     * ????????????????????????
     * @param filePath
     * @return
     */
    private boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * ?????????????????????
     * @param url
     * @param path
     */
    private void DownLoadFile(String url,String path){
        FileDownloader.getImpl().create(url).setPath(path).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {


            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {



            }

            @Override
            protected void completed(BaseDownloadTask task) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         * ?????????????????????
                         */
                        spotDialog.dismiss();
                        ToastUtils.showTextToast2(ProgressDetailActivity.this, "????????????");

                    }
                },1500); // ??????1.5???



            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                spotDialog.dismiss();
                ToastUtils.showTextToast2(ProgressDetailActivity.this, "????????????");
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
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


    /**
     * ??????uri???????????????????????????
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
        //??????????????????480x800?????????
        float hh = 800f;//?????????????????????800f
        float ww = 480f;//?????????????????????480f
        //????????????????????????????????????????????????????????????????????????????????????????????????
        int be = 1;//be=1???????????????
        if (originalWidth > originalHeight && originalWidth > ww) {//???????????????????????????????????????????????????
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//???????????????????????????????????????????????????
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //????????????
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//??????????????????
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//?????????????????????
    }

    /**
     * ??????????????????
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//???????????????????????????100????????????????????????????????????????????????baos???
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //?????????????????????????????????????????????100kb,??????????????????
            baos.reset();//??????baos?????????baos
            //??????????????? ??????????????? ????????????????????? ???????????????100????????????0?????????  ???????????????????????????????????????????????????
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//????????????options%?????????????????????????????????baos???
            options -= 10;//???????????????10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//?????????????????????baos?????????ByteArrayInputStream???
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//???ByteArrayInputStream??????????????????
        return bitmap;
    }


    /**
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndAlbum() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            openAlbum();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(ProgressDetailActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALBUM_REQUEST_CODE);
        }
    }

    /**
     * ??????????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndFile() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            openFileManager();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(ProgressDetailActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE_REQUEST_CODE);
        }
    }

    // ?????????????????????????????????
    private void openFileManager() {
        // ?????????????????????????????????
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(???image/*???);//????????????
        //intent.setType(???audio/*???); //????????????
        //intent.setType(???video/*???); //???????????? ???mp4 3gp ???android????????????????????????
        //intent.setType(???video/*;image/*???);//???????????????????????????
        intent.setType("*/*");//???????????????
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
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
     * ??????????????????
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
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
                picid=docId.split(":")[1];//id="26"
                picbaseUri=Uri.parse("content://media/external/images/media");
//                picture.setImageURI(Uri.withAppendedPath(baseUri, id));
                //????????????Uri?????????????????????content://media/external/images/media/26
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://media/external/images/media"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
                displayImage(imagePath);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //?????????content?????????Uri??????????????????????????????
//            imagePath = getImagePath(uri,null);
            displayImage(imagePath);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //?????????file?????????Uri?????????????????????????????????
            imagePath = uri.getPath();
            displayImage(imagePath);
        }
        mChooseUri = uri;

    }

    /**
     * ??????????????????????????????
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(ProgressDetailActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
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