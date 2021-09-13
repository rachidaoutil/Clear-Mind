package com.stageapp.riskmanage.models;

import java.util.List;

public class Categorie {
    int id;
    public String name;

    public long getTimeSpentDay() {
        return timeSpentDay;
    }

    List<App> Apps;
    public long timeSpentDay;
    public long timeSpentWeek;
    public int percnt;

    public Categorie(int id, String name, List<App> apps,Long timeSpentWeek, long timeSpentDay, int perent) {
        this.id = id;
        this.name = name;
        Apps = apps;
        this.timeSpentDay = timeSpentDay;
        this.timeSpentWeek = timeSpentWeek;
        this.percnt = perent;
    }
    public Categorie(int id,String name ,int percnt, long timeSpentDay,long timeSpentWeek) {
        this.id = id;
        this.name = name;
        this.timeSpentDay = timeSpentDay;
        this.timeSpentDay = timeSpentWeek;
        this.percnt = percnt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<App> getApps() {
        return Apps;
    }

    public void setApps(List<App> apps) {
        Apps = apps;
    }

    public void setTimeSpentDay(long timeSpentDay) {
        this.timeSpentDay = timeSpentDay;
    }

    public long getTimeSpentWeek() {
        return timeSpentWeek;
    }

    public void setTimeSpentWeek(long timeSpentWeek) {
        this.timeSpentWeek = timeSpentWeek;
    }

    public int getPercnt() {
        return percnt;
    }

    public void setPercnt(int percnt) {
        this.percnt = percnt;
    }
}
