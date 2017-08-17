package com.example.alpay.learnwithdrawing;


import java.util.ArrayList;

public class User {

    public String full_name;
    public int point;
    public String user_id;
    public static User user = new User("trash","trash", 0);


    public User(String user_id, String full_name, int point) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.point = point;
    }

    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<User> friends = new ArrayList<User>();


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "User email is: "+user_id+", full name is: "+full_name+", and has points: "+ Integer.toString(point);
    }
}
