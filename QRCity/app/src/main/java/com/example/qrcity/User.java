package com.example.qrcity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String userId;
    private String name;
    private String contactInfo = "None";
    private int totalScore;
    private int numCodes;

    private ArrayList<Map> userCodeList = new ArrayList<>();


    public User(String androidId, String name) {
        this.userId = androidId;
        this.name = name;
    }

    public User(String androidId, String name, String contactInfo) {
        this.userId = androidId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public User() {

    }


    public List<Map> getUserCodeList() {
        return userCodeList;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof User) {
            User s = (User) obj;
            result = this.userId.equals(s.userId);
        }
        return result;
    }

    @Override
    public int hashCode() {

        return this.userId.hashCode();
    }

    public String getName() {

        return name;
    }

    public int getTotalScore() {

        return totalScore;
    }

    public int getNumCodes() {

        return numCodes;
    }

    public String getUserId() {

        return userId;
    }

    public String getContactInfo() {

        return contactInfo;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setContactInfo(String contactInfo) {

        this.contactInfo = contactInfo;
    }

    public void setCodeList(List<Map> userCodeList) {
        this.userCodeList = new ArrayList<Map>(userCodeList);
    }

    public void setId(String value) {

        this.userId = value;
    }

    public void add_To_Score(int codeScore) {

        totalScore += codeScore;
    }

    public void set_Total_Score(int initialScore) {

        this.totalScore = initialScore;
    }

    public void set_Num_Codes(int numCodes) {

        this.numCodes = numCodes;
    }

    public void add_To_Num_Codes() {

        numCodes += 1;
    }

    public void add_To_Num_Codes(int i) {

        numCodes += i;
    }


}