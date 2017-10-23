package com.blogspot.athletio;

import Utility.JsonObjectParser;

/**
 * Created by tanvir on 8/24/17.
 */


public class User {

    UserInfo userInfo;
    UserData userData;

    public User() {
    }

    public User(String DisplayName, int BirthDate, int BirthMonth, int BirthYear, String Gender, String Email, int height, int weight) {
        userInfo=new UserInfo(DisplayName,BirthDate,BirthMonth,BirthYear,Gender,Email);
        userData=new UserData(height,weight);
    }

    public User(String str) {
        JsonObjectParser jsonObjectParser =new JsonObjectParser(str);
        userInfo=new UserInfo(jsonObjectParser.getString("userInfo"));
        userData=new UserData(jsonObjectParser.getString("userData"));
    }

    @Override
    public String toString() {
        return "{" +
                "userInfo=" + userInfo +
                ", userData=" + userData +
                '}';
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
