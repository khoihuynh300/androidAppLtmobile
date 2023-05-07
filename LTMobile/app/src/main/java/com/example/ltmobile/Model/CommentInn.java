package com.example.ltmobile.Model;

import java.util.Date;

public class CommentInn {
    private int commentInnId;
    private String content;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private String username;
    private String avatar;
    private int userId;
    private int innId;

    public CommentInn(int commentInnId, String content, String image, Date createdAt, Date updatedAt, String username, String avatar) {
        this.commentInnId = commentInnId;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.avatar = avatar;
    }

    public CommentInn(String content, int userId, int innId) {
        this.content = content;
        this.userId = userId;
        this.innId = innId;
    }

    public int getCommentInnId() {
        return commentInnId;
    }

    public void setCommentInnId(int commentInnId) {
        this.commentInnId = commentInnId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInnId() {
        return innId;
    }

    public void setInnId(int innId) {
        this.innId = innId;
    }
}
