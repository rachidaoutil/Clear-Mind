package com.stageapp.riskmanage.models;

public class DayUsageStats {
    public String date;
    public long duration;
    public long avreg;
    public DayUsageStats(String date, long duration,long avreg) {
        this.date = date;
        this.duration = duration;
        this.avreg = avreg;
    }
}
