package com.learn.springcloud.auth.controller;

import com.alibaba.fastjson.JSONObject;

public class User {
    private String userName;
    private String age;
    private String gender;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        if (null == this) {
            return "";
        }
        return JSONObject.toJSONString(this);
    }

}
