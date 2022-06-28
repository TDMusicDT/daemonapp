package com.example.daemonapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import java.util.List;

public class LauncherUtils {

    public static final String homePackName = "org.calibur.stars";

    /**
     * 启动C app
     *
     * @param context
     */
    public static void startCSActivity(Context context) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(homePackName);
        context.startActivity(LaunchIntent);
    }

    /**
     * 启动C app
     *
     * @param context
     * @param extras
     */
    public static void startCSActivity(Context context, Bundle extras) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(homePackName);
        LaunchIntent.putExtras(extras);
        context.startActivity(LaunchIntent);
    }


    /**
     * 判断C应用是否是在前台
     *
     * @param context
     * @return
     */
    public static boolean isFrontDesk(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(homePackName)) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    return false;
                else
                    return true;
            }
        }
        return false;
    }


    /**
     * 发送消息
     *
     * @param context
     * @param msg
     */
    public static void sendMsg(Context context, String msg) {
        Intent intent = new Intent("CS_Broadcast_01");
        intent.putExtra("content", msg);
        context.sendBroadcast(intent);
    }


    /**
     * 发送消息
     *
     * @param context
     * @param event
     * @param msg
     */
    public static void sendServiceMsg(Context context, String event, String msg) {
        Intent intent = new Intent("CS_Broadcast_02");
        intent.putExtra("event", event);
        intent.putExtra("content", msg);
        context.sendBroadcast(intent);
    }

}
