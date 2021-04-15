package com.example.daoqimanagement.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtilsPhoto {
    private Context ctx;
    private static OkHttpClient ok = null;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Map<String, String> map = new HashMap<String, String>();//存放
    private List<String> list = new ArrayList<String>();
    private static File file;
    private static String imgpath;
    private static String imageName;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private OkHttpUtilsPhoto() {

    }


    /*
     *单例获取
     * */
    public static OkHttpClient getInstance() {
        if (ok == null) {
            synchronized (OkHttpUtilsPhoto.class) {
                if (ok == null)
                    ok = new OkHttpClient();
            }
        }
        return ok;
    }

    public static void getString(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /*
     * 键值对上传数据
     * */
    public static void postString(String url, Map<String, String> map, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        //遍历map中所有的参数到builder
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
            Log.e("", "key: " + key + "   map.get:  " + map.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /*
     * 上传一张图片带参数
     * */
    public static void UploadFileCS(String url, String key1, String path, Map<String, Object> map, final HttpCallBack  callback) {
//      path.lastIndexOf是找到uri里面最后一个"你想找的东西"的位置,所以就是截取最后一个"/"和最后一个"."之间的东西,
        String imagpath = path.substring(0, path.lastIndexOf("/"));

        String imgName[] = path.split("/");
        for (int i = 0; i < imgName.length; i++) {
            if (i == imgName.length - 1) {
                String name = imgName[i];
                file = new File(imagpath, name);
            }
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        //遍历map中所有的参数到builder
        for (String key : map.keySet()) {
            builder.addFormDataPart(key, (String) map.get(key));
        }
        //讲文件添加到builder中
        builder.addFormDataPart(key1, file.getName(), fileBody);
        //创建请求体
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                OnError(callback, "网络请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String string = response.body().string();
                onSuccess(callback,string);
            }
        });
    }

    /*
     *上传多个图片文件
     * */
    @SuppressWarnings("unused")
    public static void UploadFileMore(String url, List<String> paths, Callback callback) {
        if (paths != null) {
            //创建文件集合
            List<File> list = new ArrayList<File>();
            //遍历整个图片地址
            for (String str : paths) {
                //截取图片地址：/storage/emulated/0
                imgpath = str.substring(0, str.lastIndexOf("/"));
                //将图片路径分解成String数组
                String[] imgName = str.split("/");
                for (int i = 0; i < imgName.length; i++) {
                    if (i == imgName.length - 1) {
                        imageName = imgName[i];//获取图片名称
                        File file = new File(imgpath, imageName);
                        list.add(file);
                    }
                }
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);//设置表单类型
            //遍历图片文件
            for (File file : list) {
                if (file != null) {
                    builder.addFormDataPart("acrd", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                }
            }
            //构建请求体
            MultipartBody requestBody = builder.build();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Call call = getInstance().newCall(request);
            call.enqueue(callback);
        }

    }

    /*
     * 上传多张图片带参数
     * */
    @SuppressWarnings("unused")
    public static void UploadFileSCMore(String url, String value, List<String> paths, Map<String, String> map, Callback callback) {
        if (paths != null && map != null) {
            //创建文件集合
            List<File> list = new ArrayList<File>();
            //遍历整个图片地址
            for (String str : paths) {
                //截取图片地址：/storage/emulated/0
                imgpath = str.substring(0, str.lastIndexOf("/"));
                //将图片路径分解成String数组
                String[] imgName = str.split("/");
                for (int i = 0; i < imgName.length; i++) {
                    if (i == imgName.length - 1) {
                        imageName = imgName[i];//获取图片名称
                        File file = new File(imgpath, imageName);
                        list.add(file);
                    }
                }
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);//设置表单类型
            //遍历图片文件
            for (File file : list) {
                if (file != null) {
                    builder.addFormDataPart(value, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                }
            }
            //遍历map中所有的参数到builder
            for (String key : map.keySet()) {
                builder.addFormDataPart(key, map.get(key));
            }
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder().url(url).post(requestBody).build();
            Call call = getInstance().newCall(request);
            call.enqueue(callback);
        }
    }

    public static void onSuccess(final HttpCallBack callBack, final String data) {
        if (callBack != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {//在主线程操作
                    callBack.onSuccess(data);
                }
            });
        }
    }


    public static void OnError(final HttpCallBack callBack, final String msg) {
        if (callBack != null) {
            handler.post(new Runnable() {
                @Override


                public void run() {
                    callBack.onError(msg);
                }

            });
        }
    }
    public static abstract class HttpCallBack {
        //成功回调
        public abstract void onSuccess(String data);
        //失败
        public abstract void onError(String meg);

    }



}
