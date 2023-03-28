/** Author(s): Derek
 *  Purpose: Test methods associated with the ScannableCode class
 *
 *  Note: getPhoto and setPhoto can not be tested
 *      -> Stack Overflow link: https://stackoverflow.com/questions/58853136/how-to-do-unit-test-for-bitmap-in-android
 *          -> Stack Overflow Question: "How to do unit test for Bitmap in android" by 0xAliHn
 *          -> Stack Overflow Answer by Peter Staranchuk
 */
package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.qrcity.qr.ScannableCode;
import com.example.qrcity.user.TestObjects;

import org.junit.Test;

import java.util.List;

public class ScannableCodeTest {
    private TestObjects objects = new TestObjects();

    /** Test Getters **/

    @Test
    public void testGetName() {
        ScannableCode qrCode = objects.mockScannableCode();
        String name = qrCode.getName();
        assertEquals(0, name.compareTo("Amateur Wind Mage"));
    }

    @Test
    public void testGetComment() {
        ScannableCode qrCode = objects.mockScannableCode();
        String comment = qrCode.getComment();
        assertEquals(0, comment.compareTo("This is a comment"));
    }

    @Test
    public void testGetScore() {
        ScannableCode qrCode = objects.mockScannableCode();
        int score = qrCode.getScore();
        assertTrue(score == 17);
    }

    @Test
    public void testGetLocation() {
        ScannableCode qrCode = objects.mockScannableCode();
        List<Double> location = qrCode.getLocation();
        assertTrue(location.get(0) == 41.40338);
        assertTrue(location.get(1) == 2.17403);
    }

    @Test
    public void testGetLatitude() {
        ScannableCode qrCode = objects.mockScannableCode();
        double Latitude = qrCode.getLatitude();
        assertTrue(Latitude == 41.40338);
    }

    @Test
    public void testGetLongitude() {
        ScannableCode qrCode = objects.mockScannableCode();
        double Longitude = qrCode.getLongitude();
        assertTrue(Longitude == 2.17403);
    }

    /** Test Setters **/

    @Test
    public void testSetName() {
        ScannableCode qrCode = objects.mockScannableCode();
        qrCode.setName("New name");
        String name = qrCode.getName();
        assertNotEquals(0, name.compareTo("Amateur Wind Mage"));
        assertEquals(0, name.compareTo("New name"));
    }

    @Test
    public void testSetComment() {
        ScannableCode qrCode = objects.mockScannableCode();
        qrCode.setComment("New comment");
        String comment = qrCode.getComment();
        assertNotEquals(0, comment.compareTo("This is a comment"));
        assertEquals(0, comment.compareTo("New comment"));
    }

    @Test
    public void testSetScore() {
        ScannableCode qrCode = objects.mockScannableCode();
        qrCode.setScore(200);
        int score = qrCode.getScore();
        assertFalse(score == 17);
        assertTrue(score == 200);
    }

    @Test
    public void testSetLocation() {
        ScannableCode qrCode = objects.mockScannableCode();
        double[] newLocation = {37.7749, 122.4194};
        qrCode.setLocation(newLocation);

        assertFalse(qrCode.getLatitude() == 41.40338);
        assertFalse(qrCode.getLongitude() == 2.17403);

        assertTrue(qrCode.getLatitude() == 37.7749);
        assertTrue(qrCode.getLongitude() == 122.4194);
    }
}
