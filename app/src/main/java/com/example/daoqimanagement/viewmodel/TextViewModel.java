package com.example.daoqimanagement.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.daoqimanagement.PrivateLinkActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ActivityCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TextViewModel extends ViewModel {

    private MutableLiveData<String> textUser;
    private MutableLiveData<String> textStatement;
    public Application application;
    public Activity activity;

    public MutableLiveData<String> getTextUser() {
        String s = TxtReader.getString(application.getResources().openRawResource(R.raw.user));
        if (textUser == null){
            textUser = new MutableLiveData<>();
            textUser.setValue(s);
        }
        return textUser;
    }

    public MutableLiveData<String> getTextStatement() {
        String s = TxtReader.getString(application.getResources().openRawResource(R.raw.statement));
        if (textStatement == null){
            textStatement = new MutableLiveData<>();
            textStatement.setValue(s);
        }
        return textStatement;
    }



    public static class TxtReader {


        /**
         * 通过一个InputStream获取内容
         *
         * @param inputStream
         * @return
         */
        public static String getString(InputStream inputStream) {
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }


        /**
         * 通过txt文件的路径获取其内容
         *
         * @param filepath
         * @return
         */
        public static String getString(String filepath) {
            File file = new File(filepath);
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return getString(fileInputStream);
        }
    }

    public void onFinish(){
        ActivityCollector.removeActivity(activity);
        activity.finish();
    }


}
