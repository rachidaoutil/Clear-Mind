package com.stageapp.riskmanage.models;

import android.graphics.drawable.Drawable;

import io.paperdb.Paper;

public class App {
    public Drawable appIcon;
    public  String appName;
    public  int usagePercentage;
    public  long usageDuration;
    public String categoryTitle;
    public int categoryID;
    public String pkg;



    public App(Drawable appIcon,String pkg, String appName, int categoryID, String categoryTitle, int usagePercentage, long usageDuration) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.pkg = pkg;
        this.usagePercentage = usagePercentage;
        this.usageDuration = usageDuration;
        this.categoryID= categoryID;
        this.categoryTitle = categoryTitle;

    }


    public App(Drawable appIcon, String appName, int usagePercentage, long usageDuration) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.usagePercentage = usagePercentage;
        this.usageDuration = usageDuration;
      }

    public int getUsagePercentage() {
        return usagePercentage;
    }

    public long getUsageDuration() {
        return usageDuration;
    }
}