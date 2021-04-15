package com.example.daoqimanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class GetSharePerfenceSP {

    public static String  getToken(Context context){

            SharedPreferences sp = context.getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
            String token = sp.getString("token", "");
            return token;

    }

    public static String  getUid(Context context){

        SharedPreferences sp = context.getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        return uid;

    }

    public static String  getUserType(Context context){

        SharedPreferences sp = context.getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype", "");
        return userType;

    }

    public static String  getType(Context context){

        SharedPreferences sp = context.getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("type", "");
        return userType;

    }

}
