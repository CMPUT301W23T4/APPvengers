package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import org.junit.Test;

public class ScannableCodeTest {
    private ScannableCode mockScannableCode() {
        int score = 17;
        String comment = "This is a comment";
        double[] location = {41.40338, 2.17403};
        Bitmap photo = null;                        //TODO: Test bitmap (how?)
        String name = "Amateur Wind Mage";
        ScannableCode qrCode= new ScannableCode(score, comment, location, photo, name);
        return qrCode;
    }

    /** Test Getters **/

    @Test
    public void testGetName() {
        ScannableCode qrCode = mockScannableCode();
        String name = qrCode.getName();
        assertEquals(0, name.compareTo("Amateur Wind Mage"));
    }

    @Test
    public void testGetComment() {
        ScannableCode qrCode = mockScannableCode();
        String comment = qrCode.getComment();
        assertEquals(0, comment.compareTo("This is a comment"));
    }

    public void testGetScore() {
        ScannableCode qrCode = mockScannableCode();
        int score = qrCode.getScore();
        assertTrue(score == 17);
    }

    @Test
    public void testGetLocation() {
        ScannableCode qrCode = mockScannableCode();
        double[] location = qrCode.getLocation();
        assertTrue(location[0] == 41.40338);
        assertTrue(location[1] == 2.17403);
    }

    @Test
    public void testGetLatitude() {
        ScannableCode qrCode = mockScannableCode();
        double Latitude = qrCode.getLatitude();
        assertTrue(Latitude == 41.40338);
    }

    @Test
    public void testGetLongitude() {
        ScannableCode qrCode = mockScannableCode();
        double Longitude = qrCode.getLongitude();
        assertTrue(Longitude == 2.17403);
    }

    //TODO: Test bitmap (how?)
    @Test
    public void testGetPhoto() {
        ScannableCode qrCode = mockScannableCode();
        Bitmap Photo = qrCode.getPhoto();
        assertTrue(Photo == null);
    }

    /** Test Setters **/

    @Test
    public void testSetName() {
        ScannableCode qrCode = mockScannableCode();
        qrCode.setName("New name");
        String name = qrCode.getName();
        assertNotEquals(0, name.compareTo("Amateur Wind Mage"));
        assertEquals(0, name.compareTo("New name"));
    }

    @Test
    public void testSetComment() {
        ScannableCode qrCode = mockScannableCode();
        qrCode.setComment("New comment");
        String comment = qrCode.getComment();
        assertNotEquals(0, comment.compareTo("This is a comment"));
        assertEquals(0, comment.compareTo("New comment"));
    }

    public void testSetScore() {
        ScannableCode qrCode = mockScannableCode();
        qrCode.setScore(200);
        int score = qrCode.getScore();
        assertFalse(score == 17);
        assertTrue(score == 200);
    }

    @Test
    public void testSetLocation() {
        ScannableCode qrCode = mockScannableCode();
        double[] newLocation = {37.7749, 122.4194};
        qrCode.setLocation(newLocation);

        assertFalse(qrCode.getLatitude() == 41.40338);
        assertFalse(qrCode.getLongitude() == 2.17403);

        assertTrue(qrCode.getLatitude() == 37.7749);
        assertTrue(qrCode.getLongitude() == 122.4194);
    }


    //TODO: Test bitmap (how?)
    @Test
    public void testSetPhoto() {
        ScannableCode qrCode = mockScannableCode();
        qrCode.setPhoto(null);
        Bitmap Photo = qrCode.getPhoto();
        assertTrue(Photo == null);
    }

}
