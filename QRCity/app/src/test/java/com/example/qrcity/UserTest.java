package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import org.junit.Test;

import java.util.ArrayList;


//TODO: Add more tests as User class is implemented
public class UserTest {

    /** Mock ScannableCode values to be used to fill users list of scanned codes **/
    private int[] scores = {43, 18, 96, 27, 51};
    private String[] comments = {"Comment 0", "Comment 1", "Comment 2", "Comment 3", "Comment 4"};
    private double[][] locations = {{34.285, 110.189}, {156.023, 137.766}, {151.191, 44.902}, {92.487, 79.376}, {27.147, 67.384}};
    private String[] names = {"Novice Earth Thief", "Divine Fire Archer", "Master Wind Mage", "Amateur Earth Warrior", "Novice Water Thief"};

    private ArrayList<ScannableCode> mockScannableCodes(){
        ArrayList<ScannableCode> qrCodes= new ArrayList<ScannableCode>();
        for (int i = 0; i < scores.length; i++) {
            qrCodes.add(new ScannableCode(scores[i], comments[i], locations[i], null, names[i]));
        }

        return qrCodes;
    }

    private User mockUser() {
        String userId = "12345";
        String name = "Test User";
        String contactInfo = "780-1234-567";
        User user = new User(userId, name, contactInfo);
        return user;
    }

    /** Test Getters **/

    @Test
    public void testGetName() {
        User user = mockUser();
        String name = user.getName();
        assertEquals(0, name.compareTo("Test User"));
    }

    @Test
    public void testGetUserID() {
        User user = mockUser();
        String userId = user.getUserId();
        assertEquals(0, userId.compareTo("12345"));
    }

    @Test
    public void testGetContactInfo() {
        User user = mockUser();
        String contactInfo = user.getContactInfo();
        assertEquals(0, contactInfo.compareTo("780-1234-567"));
    }

    @Test
    public void testGetTotalScore() {
        User user = mockUser();
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 0);
    }

    @Test
    public void testGetNumCodes() {
        User user = mockUser();
        long numCodes = user.getNumCodes();
        assertTrue(numCodes == 0);
    }

    /** Test Setters **/

    @Test
    public void testSetName() {
        User user = mockUser();
        user.setName("New Name");
        String name = user.getName();
        assertNotEquals(0, name.compareTo("Test User"));
        assertEquals(0, name.compareTo("New Name"));
    }

    @Test
    public void testSetUserID() {
        User user = mockUser();
        user.setId("54321");
        String userId = user.getUserId();
        assertNotEquals(0, userId.compareTo("12345"));
        assertEquals(0, userId.compareTo("54321"));
    }

    @Test
    public void testSetContactInfo() {
        User user = mockUser();
        user.setContactInfo("780-9876-543");
        String contactInfo = user.getContactInfo();
        assertNotEquals(0, contactInfo.compareTo("780-1234-567"));
        assertEquals(0, contactInfo.compareTo("780-9876-543"));
    }

    @Test
    public void testSetTotalScore() {
        User user = mockUser();
        user.set_Total_Score(128);
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 128);
    }

    @Test
    public void testSetNumCodes() {
        User user = mockUser();
        user.set_Num_Codes(16);
        long numCodes = user.getNumCodes();
        assertTrue(numCodes == 16);
    }

    /** Test Increment methods **/

    @Test
    public void testAddToScore() {
        User user = mockUser();
        user.add_To_Score(128);
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 128);

        user.add_To_Score(128);
        totalScore = user.getTotalScore();
        assertTrue(totalScore == 256);
    }

    @Test
    public void testAddToNumCodes() {
        User user = mockUser();
        user.add_To_Num_Codes();
        long numCodes = user.getNumCodes();
        assertTrue(numCodes == 1);

        user.add_To_Num_Codes();
        numCodes = user.getNumCodes();
        assertTrue(numCodes == 2);

        user.add_To_Num_Codes(5);
        numCodes = user.getNumCodes();
        assertTrue(numCodes == 7);
    }

}
