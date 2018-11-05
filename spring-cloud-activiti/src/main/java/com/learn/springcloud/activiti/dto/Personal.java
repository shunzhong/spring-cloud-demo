package com.learn.springcloud.activiti.dto;

import java.io.Serializable;

/**
 * 用户信息
 * @author shunzhong.deng
 */
public class Personal implements Serializable {

    private static final long serialVersionUID = -1111111L;
    private String userName;
    private int age;
    private String employeeId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
