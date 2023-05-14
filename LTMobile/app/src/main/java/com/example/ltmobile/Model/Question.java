package com.example.ltmobile.Model;


import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable {

    private int questionId;
    private Date createdAt;
    private Date updatedAt;
    private String title;
    private String avatar;
    private int askedId;
    private int answererId;
    private String askedFullname;
    private String answeredFullname;
    private String askedRole;
    private String answeredRole;

    public Question(int questionId, Date createdAt, Date updatedAt, String title, String avatar, int askedId, int answererId, String askedFullname, String answeredFullname, String askedRole, String answeredRole) {
        this.questionId = questionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.avatar = avatar;
        this.askedId = askedId;
        this.answererId = answererId;
        this.askedFullname = askedFullname;
        this.answeredFullname = answeredFullname;
        this.askedRole = askedRole;
        this.answeredRole = answeredRole;
    }

    public String getAskedRole() {
        return askedRole;
    }

    public void setAskedRole(String askedRole) {
        this.askedRole = askedRole;
    }

    public String getAnsweredRole() {
        return answeredRole;
    }

    public void setAnsweredRole(String answeredRole) {
        this.answeredRole = answeredRole;
    }

    public String getAnsweredFullname() {
        return answeredFullname;
    }

    public void setAnsweredFullname(String answeredFullname) {
        this.answeredFullname = answeredFullname;
    }

    public String getAskedFullname() {
        return askedFullname;
    }

    public void setAskedFullname(String askedFullname) {
        this.askedFullname = askedFullname;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAskedId() {
        return askedId;
    }

    public void setAskedId(int askedId) {
        this.askedId = askedId;
    }

    public int getAnswererId() {
        return answererId;
    }

    public void setAnswererId(int answererId) {
        this.answererId = answererId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
