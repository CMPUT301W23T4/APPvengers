package com.example.qrcity;

import android.graphics.Bitmap;
import android.location.Location;

public class ScannableCode {

    private String name;
    private int score;
    private String comment;
    private double[] location={0,0};
    private Bitmap photo;
    public ScannableCode(int score,String cmt,double[] location,Bitmap photo,String name){
        this.score=score;
        this.comment=cmt;
        this.location=location;
        this.photo=photo;
        this.name=name;
    }
    public void setName(String new_Name){
        this.name=new_Name;
    }
    public String getName() {
        return this.name;
    }
    public void setComment(String new_Comment){
        this.comment=new_Comment;
    }
    public void setScore(int New_score){
        this.score=New_score;
    }
    public void setLocation(double[] new_Location){
        this.location[0]=new_Location[0];
        this.location[1]=new_Location[1];
    }
    public void setPhoto(Bitmap new_Photo){
        this.photo=new_Photo;
    }

    public String getComment(){
        return this.comment;
    }
    public int getScore(){
        return this.score;
    }
    public double[] getLocation(){return this.location;};
    public double getLatitude(){
        return this.location[0];
    }
    public double getLongitude(){
      return this.location[1];
    };
    public Bitmap getPhoto(){return this.photo;};
}
