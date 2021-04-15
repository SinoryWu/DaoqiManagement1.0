package com.example.daoqimanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.bean.UpLoadBean;
import com.example.daoqimanagement.utils.Api;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button uploadBt;
    private TextView uploadTv;
    private ImageView uploadImage;
    private Button mBtnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();//检查危险权限
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveStringToSp("usertype","2");
//                saveStringToSp("token","1624194990134ec87b276ee2658fe294");
//                saveStringToSp("uid","12");
//                saveStringToSp("type","5");

                saveStringToSp("usertype","1");
                saveStringToSp("token","e521b7d630d747a5381c0ad5e700aef1");
                saveStringToSp("uid","68");
                saveStringToSp("type","2");

            }
        });

    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            try {
                upLodeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},201);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("tag", "onRequestPermissionsResult: "+requestCode );
        if (grantResults!=null&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "用户授权", Toast.LENGTH_SHORT).show();
            try {
                upLodeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "用户未授权", Toast.LENGTH_SHORT).show();
        }
    }
    private File file;
    private void upLodeFile() throws IOException {
        file = new File(Environment.getExternalStorageDirectory() + "/Pictures/Screenshots/a.jpg");//获取目录（不加后面的字符串是你的根目录+后面是继续找的意思）

        //ok上传
        /**
         * ok上传文件实例
         * 由于服务器暂停维护，接口无法访问
         * 代码就这些
         */
        //创建OK
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/* "), file);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", "1")                      //post请求Key,value
                .addFormDataPart("file", file.getName(),requestBody)     //post请求Key,value
                .build();
        //构建请求
        Request request = new Request.Builder()
//                .url("http://yun918.cn/study/public/index.php/file_upload.php")
                .url("http://yun918.cn/study/public/file_upload.php")
                .post(body)
                .build();
        //call对象
        Call call = Api.ok().newCall(request);
        //call执行请求
        call.enqueue(new Callback() {   //异步
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag", "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                final UpLoadBean upLoadBean = new Gson().fromJson(json, UpLoadBean.class);
                if (!TextUtils.isEmpty(json)){
                    int code = upLoadBean.getCode();
                    String str = String.valueOf(code);
                    if (str.equals("200")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadTv.setText(upLoadBean.getRes());
                                Glide.with(MainActivity.this).load(upLoadBean.getData().getUrl()).into(uploadImage);
                            }
                        });
                    }else {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void initView() {
        uploadBt = (Button) findViewById(R.id.upload_bt);
        uploadTv = (TextView) findViewById(R.id.upload_tv);
        uploadImage = (ImageView) findViewById(R.id.upload_image);
        mBtnSave = findViewById(R.id.save_token_uid_usertype);
    }


    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.putString(key, val);
        editor.putString(key, val);
        editor.commit();
    }
}
