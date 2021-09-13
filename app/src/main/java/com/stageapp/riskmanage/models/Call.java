package com.stageapp.riskmanage.models;

import android.graphics.drawable.Drawable;

public class Call {
    public Drawable callIcon;
    public  String callName;
    public  String callNumber;
    public  int usagePercentage;
    public  long usageDuration;


    public Call(Drawable callIcon, String callName,String callNumber, int usagePercentage, long usageDuration) {
        this.callIcon = callIcon;
        this.callName = callName;
        this.callNumber = callNumber;
        this.usagePercentage = usagePercentage;
        this.usageDuration = usageDuration;
    }
}