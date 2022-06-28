package com.example.daemonapp.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.daemonapp.APP;

/**
 * 检测APP通讯 - 副APP是否正常运行
 */
public class CheckAppManagement extends Application {

    private static volatile CheckAppManagement instance;

    private Handler handler = new Handler();

    private long lastCheckTime = 0; // 最后一次通信时间

    private long CHECK_TIME = 60 * 1000;

    private String homePackName = "org.calibur.stars";


    private boolean isAppCheck = true;

    /**
     * 单例
     *
     * @return
     */
    public static synchronized CheckAppManagement getInstance() {
        if (instance == null) {
            synchronized (CheckAppManagement.class) {
                if (instance == null) {
                    instance = new CheckAppManagement();
//                    instance.init();
                }
            }
        }
        return instance;
    }


    public void startCSActivity(Context context, String packName) {
//        Context context = getApplicationContext();
//        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packName);
//        context.startActivity(LaunchIntent);
//        Context context = getApplicationContext();
        ComponentName componetName = new ComponentName("org.calibur.stars", "org.calibur.stars.ui.main.MainActivity_");
        Intent intent = new Intent();
        intent.setComponent(componetName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 更新最后通信时间
     *
     * @param time
     */
    public void upLastCheckTime(long time) {
        lastCheckTime = time;
    }

    /**
     * 检测副app是否运行
     */
    public void init(Context context) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAppCheck) {
                    try {
                        long t = System.currentTimeMillis();
//                        Log.i("csTest", "boot checktime: " +  String.valueOf(lastCheckTime));
                        if (t - lastCheckTime > CHECK_TIME * 10) {
                            Log.i("csTest", "&&&&&&&&&&&& 检测到主APP进程被退出，尝试重启APP &&&&&&&&&&&&");
                            startCSActivity(context, homePackName);
                        }

                        // 发送保活广播
                        Log.i("csTest", "boot发送保活广播");
                        Intent intent = new Intent("com.example.daemonapp.DAEMON_Broadcast");
                        intent.setComponent(new ComponentName("org.calibur.stars", "org.calibur.stars.external.receiver.CSBroadcastReceiver"));
//                        intent.putExtra("daemon_msg", "DAEMON_Broadcast");
                        context.sendBroadcast(intent);
                    } catch (Exception e) {
                        Log.i("csTest", "restart app failed: " + e.toString());
                    }
                }
                handler.postDelayed(this, CHECK_TIME);
            }
        }, CHECK_TIME);
    }
}
