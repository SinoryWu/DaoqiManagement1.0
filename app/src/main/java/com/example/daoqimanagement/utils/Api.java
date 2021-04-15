package com.example.daoqimanagement.utils;

import okhttp3.OkHttpClient;

public class Api {
    public static String URL = "http://47.98.205.142:9510";//测试服
    //    public static String URL = "https://transmit.daoqihz.com";//正式服


    static OkHttpClient okHttpClient = new OkHttpClient();

    public static OkHttpClient ok() {

        return okHttpClient;
    }

}
