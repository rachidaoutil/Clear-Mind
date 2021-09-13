package com.stageapp.riskmanage.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.stageapp.riskmanage.Activity.MainActivity;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.DayStat;
import com.stageapp.riskmanage.models.mNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class usageService extends Service {


    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    public static int USAGE_LIMIT =   60 * 60 * 1000  ; // 1/2 hours
    public static boolean flag_1 = true;
    public static boolean isDestroyed = false;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    static long StartTime = 0;
    static long Counter = 0;

    NotificationManager notificationManager;
    SharedPreferences settings;
    SharedPreferences.Editor editor;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;

//        editor.putBoolean("sreentimeAlert",false);
//        editor.commit();
    }

    @Override
    public void onCreate() {
        Log.e("usageService","Its has started!!!!!!!!");

        this.settings = getSharedPreferences("Prefs", 0);
        this.editor   = settings.edit();
        createNotificationChannel();
        if (USAGE_LIMIT == 0) {
            this.stopSelf();
        }

       StartTime =  new Date().getTime();
       Counter = loadStatistics();

       editor.putBoolean("sreentimeAlert",true);
       editor.commit();
       isDestroyed = false;




        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            if(!settings.getBoolean("sreentimeAlert",false)){
                mTimer.cancel();
            }
            USAGE_LIMIT = settings.getInt("usageTime", 0);

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    long currentTime = new Date().getTime();
                    long duration = loadStatistics();

                    Toast.makeText(getApplicationContext(), "Hourly using phone "+getDurationBreakdown(duration),
                            Toast.LENGTH_SHORT).show();



                    if (duration > USAGE_LIMIT) {

                        Log.e("Notif","send send "+getDurationBreakdown(USAGE_LIMIT));

                        if (flag_1){

                            String nofifText = "You've reached a "+getDurationBreakdown(duration)+" of ScreenTime!";
                            List<mNotification> oldNotifications = new ArrayList<>();
                            try {

                                oldNotifications = Paper.book().read("Notifications",new ArrayList<>());
                                if (oldNotifications.size() > 10) {
                                    oldNotifications  = new ArrayList<>();
                                }


                            }catch (Exception e){
                                Log.e("Notif","send send "+e.getMessage());

                            }
                            oldNotifications.add(new mNotification("ScreenTime Alert",nofifText,"ScreenTime",getDateTime(),"Normal"));
                            Paper.book().write("Notifications",oldNotifications);
                            usageService.this.notificationManager.notify(0, getNotification(nofifText));

                            Toast.makeText(getApplicationContext(), "Stop using phone "+getDateTime(),
                                    Toast.LENGTH_SHORT).show();
                            flag_1 = false;
                        }
                    }else {
                        flag_1 = true;
                    }

                    if(currentTime == (60 * 60 * 1000 + StartTime)){
                        List<DayStat> dayStats = new ArrayList<>();
                        try {

                            dayStats = Paper.book().read("DayStats");
                            dayStats.add(new DayStat(currentTime,duration-Counter));
                            Paper.book().write("DayStats",dayStats);

                        }catch (Exception e){
                            Log.e("DayStats",""+e.getMessage());

                        }
                        StartTime = currentTime;
                    }
                }
            });
        }

        private String getDateTime() {
            // get date time in custom format
            DateFormat df = new SimpleDateFormat("HH:mm - MMM d, ''yy");

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss]");
            return df.format(new Date());
        }

    }

    /**
     * load the usage stats for last 24h
     */
    public long loadStatistics() {
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  System.currentTimeMillis() - 1000*3600*24,  System.currentTimeMillis());
        long time = appList.stream().filter(app -> app.getTotalTimeInForeground() > 0).map(UsageStats::getTotalTimeInForeground).mapToLong(Long::longValue).sum();
        return time;
    }




    /**
     * helper method to get string in format hh:mm:ss from miliseconds
     *
     * @param millis (application time in foreground)
     * @return string in format hh:mm:ss from miliseconds
     */
    private String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours + " h " +  minutes + " m " + seconds + " s");
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notif";
            String description = "push notif";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("10001_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification getNotification(String content) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001_ID")
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("My notification")
                .setContentText(content)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder.build();
    }





}
