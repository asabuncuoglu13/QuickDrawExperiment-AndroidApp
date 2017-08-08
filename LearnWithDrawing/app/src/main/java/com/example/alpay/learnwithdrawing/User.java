package com.example.alpay.learnwithdrawing;

public class User {

    public String full_name;
    public int point;

    public User(String full_name, int point) {
        this.full_name = full_name;
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
