package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.adapter.ResourceAppendixAdapter;
import com.example.daoqimanagement.bean.AddReadNumResponse;
import com.example.daoqimanagement.bean.ResourceDetailResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.FileUtils;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.example.daoqimanagement.utils.WebFormatter;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import us.feras.mdv.MarkdownView;

public class ResourceDetailActivity extends AppCompatActivity {
    private RelativeLayout mRlFinish;
    private TextView mTvTitle,  mTvTime;
    private WebView mWebContent;
    private RecyclerView mRcAppendixList;
    private ResourceAppendixAdapter resourceAppendixAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    private PopupWindow popupWindow;
    Animation animation;
    String filePath;
    String titleId;
    AlertDialog spotDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_resource_detail);
        mRlFinish = findViewById(R.id.resource_detail_rl_finish);
        mTvTitle = findViewById(R.id.resource_detail_title);
        mWebContent = findViewById(R.id.resource_detail_content);
        mTvTime = findViewById(R.id.resource_detail_time);
        mRcAppendixList = findViewById(R.id.resource_detail_appendix_list);


        Intent intent = getIntent();
        titleId = intent.getStringExtra("titleId");


        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCollector.removeActivity(ResourceDetailActivity.this);
                finish();

            }
        });

//        getResourceDetailRes(Api.URL+"/v1/encyclopedia/detail?titleId="+titleId);
        getResourceDetailRes(Api.URL+"/v1/encyclopedia/detail?titleId="+titleId);



    }

    public void showPuPopWindow(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.file_popview_item, null);
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
            popupWindow.showAtLocation(ResourceDetailActivity.this.findViewById(R.id.resource_detail_appendix_line), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//给临时权限
                        sharingIntent.setType("image/*");//根据文件类型设定type
//                    sharingIntent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    FileProvider.getUriForFile(getBaseContext(), getPackageName() +".fileprovider",
                                            new File(filePath)));

                        }else {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                        }

                        startActivity(Intent.createChooser(sharingIntent, "分享"));

                    }else {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//给临时权限
                        sharingIntent.setType("*/*");//根据文件类型设定type
//                    sharingIntent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM,
                                    FileProvider.getUriForFile(getBaseContext(), getPackageName() +".fileprovider",
                                            new File(filePath)));

                        }else {
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                        }

                        startActivity(Intent.createChooser(sharingIntent, "分享"));

                    }
                    popupWindow.dismiss();

                }
            });

            popupView.findViewById(R.id.file_popwindow_tv_check_path).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                   ToastUtils.showTextToast3(ProgressDetailActivity.this,filePath);
                    Toast.makeText(ResourceDetailActivity.this,filePath,Toast.LENGTH_LONG).show();
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
    protected void postResAddReadNum(String url) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:", String.valueOf(params));

        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",GetSharePerfenceSP.getToken(this))
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
                        ToastUtils.showTextToast2(ResourceDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("readnumadd", res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        AddReadNumResponse addReadNumResponse = gson.fromJson(res, AddReadNumResponse.class);

                        if (addReadNumResponse.getCode() == 0) {

                            saveStringToSp("refresh","refreshResourceList");

                        } else if (addReadNumResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ResourceDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ResourceDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            String msg = addReadNumResponse.getMsg();
                            ToastUtils.showTextToast2(ResourceDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }


    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("refresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }
    private void getResourceDetailRes(String url) {


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
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid", GetSharePerfenceSP.getUid(this))
                .get()
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
                        ToastUtils.showTextToast2(ResourceDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);


                final ResourceDetailResponse resourceDetailResponse = new Gson().fromJson(res, ResourceDetailResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (resourceDetailResponse.getCode() == 0) {
//                            postResAddReadNum(Api.URL+"/v1/encyclopedia/addReadNum?titleId="+titleId);
                            saveStringToSp("refresh","refreshResourceList");
                            mTvTitle.setText(resourceDetailResponse.getData().getTitle());

                            if (resourceDetailResponse.getData().getUpdatedAt() == null) {
                                mTvTime.setText("");
                            } else {
                                String createAt1 = resourceDetailResponse.getData().getUpdatedAt();
                                String createAt = createAt1.substring(0, 16);
                                StringBuffer buffer = new StringBuffer(createAt);
                                buffer.replace(4, 5, "年");
                                buffer.replace(7, 8, "月");
                                mTvTime.setText(buffer);
                            }

                            DisplayMetrics  dm = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            int screenWidth = dm.widthPixels;
                            int screenHeight = dm.heightPixels;
                            ViewGroup.LayoutParams params = mWebContent.getLayoutParams();
                            params.width= (int) (screenWidth*0.88);


                            mWebContent.setLayoutParams(params);

//                            mWebContent.loadMarkdown(resourceDetailResponse.getData().getContent());

                            String varjs = "<script type='text/javascript'> \nwindow.onload = function()\n{var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";

                            WebSettings settings = mWebContent.getSettings();
                            settings.setJavaScriptEnabled(true);

                            mWebContent.setHorizontalScrollBarEnabled(false);//水平不显示
                            mWebContent.setVerticalScrollBarEnabled(false); //垂直不显示
                            mWebContent.setScrollContainer(false);
                            mWebContent.loadDataWithBaseURL(null,varjs + resourceDetailResponse.getData().getContent(), "text/html", "UTF-8",null);
//                            mWebContent.loadUrl("https://www.apple.com.cn/?afid=p238%7CN2nnhxYV_mtid_18707vxu38484&cid=aos-cn-kwba-brand-bz");


                            resourceAppendixAdapter = new ResourceAppendixAdapter(ResourceDetailActivity.this,resourceDetailResponse.getData().getAppendix());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ResourceDetailActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
                            mRcAppendixList.setLayoutManager(layoutManager);
                            mRcAppendixList.setAdapter(resourceAppendixAdapter);
                            resourceAppendixAdapter.setOnItemClickListener(new ResourceAppendixAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String url,View itemView) {
                                    final File folder = ResourceDetailActivity.this.getExternalFilesDir(null);
                                    final String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                                    if (!folder.exists()) {
                                        folder.mkdir();
                                    }

                                    if (fileIsExists(String.valueOf(folder)+"/download/" + fileName)) {
//

                                        filePath = (folder)+"/download/" + fileName;
                                        showPuPopWindow(itemView);

//

                                    } else {
                                        spotDialog = new SpotsDialog.Builder().setContext(ResourceDetailActivity.this).setTheme(R.style.SpotDialogCustom)
                                                .setCancelable(false).build();
                                        spotDialog.show();
                                        DownLoadFile(Api.URL + url,(folder)+"/download/" + fileName);



                                    }
                                }
                            });
                        } else if (resourceDetailResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ResourceDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(ResourceDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = resourceDetailResponse.getMsg();
                            ToastUtils.showTextToast2(ResourceDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }



    /**
     * 判断文件是否存在
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
     * 下载文件到本地
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
                         * 延时执行的代码
                         */
                        spotDialog.dismiss();
                        ToastUtils.showTextToast2(ResourceDetailActivity.this, "下载完成");

                    }
                },1500); // 延时1.5秒
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                spotDialog.dismiss();
                ToastUtils.showTextToast2(ResourceDetailActivity.this, "下载失败");
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }



}