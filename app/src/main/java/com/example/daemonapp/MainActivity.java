package com.example.daemonapp;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.daemonapp.databinding.ActivityMainBinding;
import com.example.daemonapp.service.CheckAppManagement;
import com.example.daemonapp.service.ForegroundService;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Handler handler = new Handler();
    private long CHECK_TIME = 60 * 60 * 1000;
    private static String starsPackName = "org.calibur.stars";
    private static String runPackName = "com.spotify.music";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (!APP.is_init_daemon) {
            startForeground();
            CheckAppManagement.getInstance().init(getApplicationContext());
            APP.is_init_daemon = true;
        }

//        new Thread() {
//            @Override
//            public void run() {
//                startForeground();
//                listenApp();
//            }
//        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void startForeground() {
        try {
            ForegroundService.start(getApplicationContext());
            Log.i("csTest", "&&&&&&& ???????????????????????? &&&&&&");
//            if (!ForegroundService.serviceIsLive) {
//                // Android 8.0??????startForegroundService????????????????????????
//                Intent mForegroundService = new Intent(this, ForegroundService.class);
//                mForegroundService.putExtra("Foreground", "This is a foreground service.");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(mForegroundService);
//                } else {
//                    startService(mForegroundService);
//                }
//                Log.i("csTest", "&&&&&&& ???????????????????????? &&&&&&");
//            } else {
//                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            Log.e("csTest", "&&&&&&& ???????????????????????? &&&&&&");
            Log.e("csTest", e.toString());
        }
    }


    public void startCSActivity(Context context, String packName) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packName);
        context.startActivity(LaunchIntent);
    }


    public boolean isFrontDesk(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            Log.i("csTest", appProcess.processName);
            if (appProcess.processName.equals(starsPackName)) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    return false;
                else
                    return true;
            }
        }
        return false;
    }

    class RecentUseComparator implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }

    }

    public boolean isRun(Context context) {
        long ts = System.currentTimeMillis();

        RecentUseComparator mRecentComp = new RecentUseComparator();

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        //??????ts-10000 ???ts??????????????????UsageStats????????????????????????????????????????????????????????????
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 10000, ts);

        if (usageStats == null) return false;
        if (usageStats.size() == 0) return false;
        Collections.sort(usageStats, mRecentComp);
        Log.d("csTest", "====usageStats.get(0).getPackageName()" + usageStats.get(0).getPackageName());
        return usageStats.get(0).getPackageName().equals(starsPackName);
    }


    public void listenApp() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("csTest", "&&&&&&&&& ????????????????????? &&&&&&&&&&&");
                    Context mContext = getApplicationContext();
                    startCSActivity(mContext, starsPackName);
                    Thread.sleep(5000);
                    startCSActivity(mContext, runPackName);
//                    if (!isRun(mContext)) {
//                        Log.i("csTest","&&&&&&&&&&&& ?????????APP??????????????????????????????APP &&&&&&&&&&&&");
////                        startLauncherActivity(mContext);
//                        startCSActivity(mContext, starsPackName);
//                    } else {
//                        Log.i("csTest","&&&&&&&&&&&& ?????????APP?????????????????? &&&&&&&&&&&&");
//                    }
                    Log.i("csTest", "&&&&&&&&& ???????????????????????? &&&&&&&&&&&");
                } catch (Exception e) {
                    Log.e("csTest", "&&&&&&& ???????????? &&&&&&");
                    Log.e("csTest", e.toString());
                }
                handler.postDelayed(this, CHECK_TIME);
            }
        }, CHECK_TIME);
    }

    public String shellExec(String cmd) {
        Runtime mRuntime = Runtime.getRuntime();
        try {
            // Process???????????????????????????????????????????????????
            Process mProcess = mRuntime.exec(new String[]{"su", "-c", cmd});
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                mRespBuff.append(buff, 0, ch);
            }
            mReader.close();
            //  Log.i("shell", "shellExec: " + mRespBuff???toString());
            return mRespBuff.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}