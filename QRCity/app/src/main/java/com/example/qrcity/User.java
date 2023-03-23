package com.example.qrcity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String userId;
    private String name;
    private String contactInfo;
    private long totalScore;
    private long numCodes;
    private ArrayList<Map> userCodeList = new ArrayList<>();



    public User(String androidId, String name) {
        this.userId = androidId;
        this.name = name;
        this.contactInfo = "None";
        this.totalScore = 0;
        this.numCodes = 0;
    }
    public List<Map> getUserCodeList() {
        return userCodeList;
    }

    public User(String androidId, String name, String contactInfo) {
        this.userId = androidId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.totalScore = 0;
        this.numCodes = 0;
    }

    public User() {

    }
    public void addCode(ScannableCode codeData) {
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("codeName", codeData.getName());
        //codeMap.put("id", codeData.getId());
        userCodeList.add(codeMap);
    }



    @Override
    public int hashCode() {

        return this.userId.hashCode();
    }

    public String getName() {

        return name;
    }

    public long getTotalScore() {

        return totalScore;
    }

    public long getNumCodes() {

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



    public void setId(String value) {

        this.userId = value;
    }

    public void add_To_Score(int codeScore) {

        totalScore += codeScore;
    }

    public void set_Total_Score(long initialScore) {

        this.totalScore = initialScore;
    }

    public void set_Num_Codes(long numCodes) {

        this.numCodes = numCodes;
    }

    public void add_To_Num_Codes() {

        numCodes += 1;
    }

    public void add_To_Num_Codes(int i) {

        numCodes += i;
    }
    public void setCodeList(List<Map> userCodeList) {
        this.userCodeList = new ArrayList<Map>(userCodeList);
    }
    public void setTotalScore(int initialScore) {
        this.totalScore = initialScore;
    }
    public void setNumCodes(int numCodes) {
        this.numCodes = numCodes;
    }

}