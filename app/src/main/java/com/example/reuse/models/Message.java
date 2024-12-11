package com.example.reuse.models;

public class Message {
    private long timestamp;
    private String text;
    private int userAvatarResId;
    boolean isSent;



    public Message(long timestamp, String text, int userAvatarResId, boolean isSent) {
        this.timestamp = timestamp;
        this.text = text;
        this.userAvatarResId = userAvatarResId;
        this.isSent = isSent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserAvatarResId() {
        return userAvatarResId;
    }

    public void setUserAvatarResId(int userAvatarResId) {
        this.userAvatarResId = userAvatarResId;
    }
}
