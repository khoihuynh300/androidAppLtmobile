package com.example.ltmobile.Model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String email, fullname,avatar, role, gender, password;

    public User(int userId, String email, String fullname, String gender, String avatar, String role) {
        this.userId = userId;
        this.email = email;
        this.fullname = fullname;
        this.avatar = avatar;
        this.role = role;
        this.gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
