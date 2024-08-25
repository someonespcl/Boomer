package com.boomer.model;

public class User {

    private String username;
    private String email;
    private String password;
    private long timestamp;
    
    public User() {
        
    }

    public User(String username, String email, String password, long timestamp) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
