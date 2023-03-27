package com.example.qrcity;

import android.graphics.Bitmap;
import android.location.Location;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class ScannableCode {

    private String name;
    private int score;
    private String comment;
    private double[] location={0,0};
    private Bitmap photo;
    private String id;
    public ScannableCode(){

    }
    public ScannableCode(String hash,int score,String cmt,double[] location,Bitmap photo,String name){
        this.score=score;
        this.comment=cmt;
        this.location=location;
        this.photo=photo;
        this.name=name;
        try
        {
            this.id = toHexString(getSHA(hash));
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
    }
    /**
     * This method generates a SHA-256 hash code from a String input
     * https://www.geeksforgeeks.org/sha-256-hash-in-java/
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * This method converts a raw byte value to a String
     * https://www.geeksforgeeks.org/sha-256-hash-in-java/
     */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
    public void setId(String newid){
        this.id=newid;
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
    public String getId(){
        return this.id;
    }
    public String getComment(){
        return this.comment;
    }
    public int getScore(){
        return this.score;
    }
    public List<Double> getLocation() {
        List<Double> list = new LinkedList<>();
        list.add(location[0]);
        list.add(location[1]);
        return list;
    }


    public void setLocation(List<Double> list){
        this.location[0] = list.get(0);
        this.location[1] = list.get(1);
    }

    public double getLatitude(){
        return this.location[0];
    }
    public double getLongitude(){
      return this.location[1];
    };
    public Bitmap getPhoto(){return this.photo;};
}
