package com.stageapp.riskmanage.models;

public class mNotification {
    public String nTitle;
    public String nDescrp;
    public String nType;
    public String nDate;
    public String nPriorty;

    public mNotification(String nTitle, String nDescrp, String nType, String nDate, String nPriorty) {
        this.nTitle = nTitle;
        this.nDescrp = nDescrp;
        this.nType = nType;
        this.nDate = nDate;
        this.nPriorty = nPriorty;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }

    public String getnDescrp() {
        return nDescrp;
    }

    public void setnDescrp(String nDescrp) {
        this.nDescrp = nDescrp;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public String getnPriorty() {
        return nPriorty;
    }

    public void setnPriorty(String nPriorty) {
        this.nPriorty = nPriorty;
    }
}
