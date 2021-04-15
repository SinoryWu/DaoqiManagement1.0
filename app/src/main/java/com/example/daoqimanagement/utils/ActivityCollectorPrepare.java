package com.example.daoqimanagement.utils;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ActivityCollectorPrepare {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();

    }
}
