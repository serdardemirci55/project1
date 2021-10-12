package com.cmpe281.project1;

import java.util.List;

public class User {

    private String username;
    private String password;
    private String name;
    private String given_name;

    public void setName(String name) {
        this.name = name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getName() {
        return name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}