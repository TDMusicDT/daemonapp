package com.example.daemonapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.daemonapp.R;
import com.example.daemonapp.MainActivity;

public class ForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANEL_ID = ForegroundService.class.getName() + ".foreground";

    public static void start(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ForegroundService.class));
        } else {
            context.startService(new Intent(context, ForegroundService.class));
        }
    }

    public static void stop(Context context){
        context.stopService(new Intent(context, ForegroundService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground() {
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    private Notification buildNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        return new NotificationCompat.Builder(this, CHANEL_ID)
                .setContentTitle("daemon前台服务")
                .setContentText("daemon前台服务")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setChannelId(CHANEL_ID)
                .setVibrate(new long[0])
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        CharSequence name = "daemon前台服务";
        String description = "daemon前台服务";
        NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(description);
        channel.enableLights(false);
        manager.createNotificationChannel(channel);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}