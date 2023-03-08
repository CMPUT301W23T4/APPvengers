package com.example.qrcity;

import android.location.Location;

public class ScannableCode {

    private int score;
    private String comment;
    private Location location;

    public void setComment(String new_Comment){
        this.comment=new_Comment;
    }
    public void setScore(int New_score){
        this.score=New_score;
    }
    public void setLocation(Location new_Location){
        this.location=new_Location;
    }

    public String getComment(){
        return this.comment;
    }
    public int getScore(){
        return this.score;
    }
    public Location getLocation(){return this.location;};
}
