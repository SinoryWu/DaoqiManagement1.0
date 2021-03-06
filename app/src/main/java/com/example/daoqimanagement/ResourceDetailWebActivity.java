package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.daoqimanagement.bean.AddReadNumResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResourceDetailWebActivity extends AppCompatActivity {
    private RelativeLayout mRlFinish;
    private WebView webView;
    private LinearLayout webError;
    DialogTokenIntent dialogTokenIntent = null;
    private boolean isError = false;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_resource_detail_web);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView = findViewById(R.id.resource_detail_webview);
        mRlFinish =  findViewById(R.id.resource_detail_web_rl_finish);
        webError  =findViewById(R.id.resource_detail_web_error);
        final String titleId = intent.getStringExtra("titleId");

        webError.setVisibility(View.GONE);


        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(ResourceDetailWebActivity.this);
                finish();
            }
        });
        //??????WebSettings??????
        WebSettings webSettings = webView.getSettings();



//??????????????????????????????Javascript????????????webview??????????????????Javascript
        webSettings.setJavaScriptEnabled(true);
// ???????????? html ??????JS ???????????????????????????????????????????????????CPU????????????
// ??? onStop ??? onResume ???????????? setJavaScriptEnabled() ???????????? false ??? true ??????


//????????????????????????????????????
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //??????webview?????????
        webSettings.setUseWideViewPort(true); //????????????????????????webview?????????
        webSettings.setLoadWithOverviewMode(true); // ????????????????????????

//????????????
        webSettings.setSupportZoom(true); //????????????????????????true??????????????????????????????
        webSettings.setBuiltInZoomControls(true); //????????????????????????????????????false?????????WebView????????????
        webSettings.setUseWideViewPort(true);

        webSettings.setDisplayZoomControls(false); //???????????????????????????

//??????????????????

        webSettings.setAllowFileAccess(true); //????????????????????????
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //????????????JS???????????????
        webSettings.setLoadsImagesAutomatically(true); //????????????????????????
        webSettings.setDefaultTextEncodingName("utf-8");//??????????????????

        //??????????????????:
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //?????????????????????
        //LOAD_CACHE_ONLY: ?????????????????????????????????????????????
        //LOAD_DEFAULT: ??????????????????cache-control????????????????????????????????????
        //LOAD_NO_CACHE: ??????????????????????????????????????????.
        //LOAD_CACHE_ELSE_NETWORK????????????????????????????????????????????????no-cache?????????????????????????????????



////??????????????????
//        webView.canGoBack();
//        //??????????????????
//        webView.canGoForward();

        //??????1. ?????????????????????
        webView.loadUrl(url);
//        webView.loadUrl("https://www.apple.com.cn/?afid=p238%7CN2nnhxYV_mtid_18707vxu38484&cid=aos-cn-kwba-brand-bz");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isError == false){
                    postResAddReadNum(Api.URL+"/v1/encyclopedia/addReadNum?titleId="+titleId);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                webView.setVisibility(View.GONE);
                webError.setVisibility(View.VISIBLE);
                isError = true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("page1213", "onPageStarted: ");

            }


        });



//
//        webView.loadUrl("https://mp.weixin.qq.com/s/MptKgEPG1hK9ulpDzqKfDg");
    }



    protected void postResAddReadNum(String url) {
        //1.??????okhttp??????
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.??????request
        //2.1??????requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:", String.valueOf(params));

        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid",GetSharePerfenceSP.getUid(this))
                .post(requestBodyJson)
                .build();
        //3.???request?????????call
        Call call =  Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(ResourceDetailWebActivity.this, "??????????????????");
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

                        AddReadNumResponse addReadNumResponse = gson.fromJson(res, AddReadNumResponse.class);

                        if (addReadNumResponse.getCode() == 0) {

                            saveStringToSp("refresh","refreshResourceList");


                        } else if (addReadNumResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(ResourceDetailWebActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(ResourceDetailWebActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            String msg = addReadNumResponse.getMsg();
                            ToastUtils.showTextToast2(ResourceDetailWebActivity.this, msg);
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
}