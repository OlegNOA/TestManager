package com.example.testmanager.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String username;
    private String email;
    private String password;
    private String userId;
    private Timestamp createdTimestamp;




    public UserModel(String phone, String username, String email, String password, String userId, Timestamp createdTimestamp) {
        this.phone = phone;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.createdTimestamp = createdTimestamp;
        // Assuming you have an empty constructor to initialize fcmToken

    }





    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }


}
