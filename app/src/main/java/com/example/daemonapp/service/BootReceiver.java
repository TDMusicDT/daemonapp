package com.example.daemonapp.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.daemonapp.APP;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent toIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(toIntent);
        }
    }

}
