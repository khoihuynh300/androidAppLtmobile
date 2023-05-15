package com.example.ltmobile.Model;

public class Notify {
    private int id;
    private String content, link;
    private boolean viewed;

    public Notify(int id, String content, String link, boolean viewed) {
        this.id = id;
        this.content = content;
        this.link = link;
        this.viewed = viewed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
