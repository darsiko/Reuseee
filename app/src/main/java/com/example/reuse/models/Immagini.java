package com.example.reuse.models;

public class Immagini {
    private String pid;
    private String url;

    //costruttori
    public Immagini(){}

    //costruttore generale
    public Immagini(String pid, String url){
        this.pid=pid;
        this.url=url;
    }
    //
    public Immagini(String pid) {}






    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUid() {
        return pid;
    }
    public void setUid(String pid) {
        this.pid = pid;
    }
}
