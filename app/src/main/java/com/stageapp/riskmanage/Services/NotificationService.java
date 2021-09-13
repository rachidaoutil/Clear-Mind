package com.stageapp.riskmanage.Services;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.stageapp.riskmanage.models.PostedNotification;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by mukesh on 19/5/15.
 */
public class NotificationService extends NotificationListenerService {

    Context context;


    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();
        int Icon = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap largelargeIcon= sbn.getNotification().largeIcon;


        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);
        List<PostedNotification> postedNotifications = new ArrayList<>();

        try {
           postedNotifications =  Paper.book().read("PostedNotification",new ArrayList<>());
        }catch (Exception e){
            Log.e("PostedNotification",e.getMessage());
        }

        postedNotifications.add(new PostedNotification(pack,ticker,title,text));
        Paper.book().write("PostedNotification",postedNotifications);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}