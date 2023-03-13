package com.example.qrcity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String userId;
    private String name;
    private String contactInfo;
    private long totalscore;
    private long numcodes;




    public User(String androidId, String name) {
        this.userId = androidId;
        this.name = name;
        this.contactInfo = "None";
        this.totalscore = 0;
        this.numcodes = 0;
    }

    public User(String androidId, String name, String contactInfo) {
        this.userId = androidId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.totalscore = 0;
        this.numcodes = 0;
    }

    public User() {

    }




    @Override
    public int hashCode() {

        return this.userId.hashCode();
    }

    public String getName() {

        return name;
    }

    public long getTotalScore() {

        return totalscore;
    }

    public long getNumCodes() {

        return numcodes;
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

        totalscore += codeScore;
    }

    public void set_Total_Score(long initialScore) {

        this.totalscore = initialScore;
    }

    public void set_Num_Codes(long numCodes) {

        this.numcodes = numCodes;
    }

    public void add_To_Num_Codes() {

        numcodes += 1;
    }

    public void add_To_Num_Codes(int i) {

        numcodes += i;
    }


}