package com.example.ltmobile.Model;

import java.io.Serializable;
import java.util.Date;

public class Messages implements Serializable {
    private int messageId;
    private String message;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private int senderId;
    private int userId;
    private String userAvatar;
    private String username;
    //    private int answeredId;
    private int questionId;

    public Messages(String message, String image,Date createdAt,Date updatedAt ,int senderId, int questionId) {
        this.message = message;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderId = senderId;
        this.questionId = questionId;
    }

    public Messages(String message, int userId, int questionId) {
        this.message = message;
        this.userId = userId;
        this.questionId = questionId;
    }

    public Messages(String message, Date createdAt, Date updatedAt, int userId, String username, String userAvatar) {
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.username = username;
        this.userAvatar = userAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
