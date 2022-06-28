package com.example.daemonapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.daemonapp.service.CheckAppManagement;


public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("csTest", "boot接收到广播: " + intent.getAction());
//        String daemonMsg = intent.getExtras().getString("daemon_msg");
//        Log.i("csTest", daemonMsg);
        if (intent.getAction().equals("org.calibur.stars.DAEMON_Broadcast")) {
            CheckAppManagement.getInstance().upLastCheckTime(System.currentTimeMillis());
        }
    }

}
