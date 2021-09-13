package com.stageapp.riskmanage.models;



public class PostedNotification {
    String packg;
    String ticker;
    String title;
    String text;

    public PostedNotification(String packg, String ticker, String title, String text) {
        this.packg = packg;
        this.ticker = ticker;
        this.title = title;
        this.text = text;
    }

    public String getPackg() {
        return packg;
    }

    public void setPackg(String packg) {
        this.packg = packg;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
