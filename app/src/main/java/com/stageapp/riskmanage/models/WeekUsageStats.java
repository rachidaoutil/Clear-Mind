package com.stageapp.riskmanage.models;

public class WeekUsageStats {
    public String date;
    public long duration;
    public long avreg;
    public WeekUsageStats(String date, long duration, long avreg) {
        this.date = date;
        this.duration = duration;
        this.avreg = avreg;
    }
}
