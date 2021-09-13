package com.stageapp.riskmanage.models;

public class DayStat {

    long pointTime;
    long pointData;

    public DayStat(long pointTime, long pointData) {
        this.pointTime = pointTime;
        this.pointData = pointData;
    }

    public long getPointTime() {
        return pointTime;
    }

    public void setPointTime(long pointTime) {
        this.pointTime = pointTime;
    }

    public long getPointData() {
        return pointData;
    }

    public void setPointData(long pointData) {
        this.pointData = pointData;
    }
}
