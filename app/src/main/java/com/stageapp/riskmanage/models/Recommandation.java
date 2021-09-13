package com.stageapp.riskmanage.models;

public class Recommandation {
    String rTitle;
    String rBref;
    String date;
    boolean rSave;

    public Recommandation(String rTitle, String rBref, String date, boolean rSave) {
        this.rTitle = rTitle;
        this.rBref = rBref;
        this.date = date;
        this.rSave = rSave;
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getrBref() {
        return rBref;
    }

    public void setrBref(String rBref) {
        this.rBref = rBref;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isrSave() {
        return rSave;
    }

    public void setrSave(boolean rSave) {
        this.rSave = rSave;
    }
}
