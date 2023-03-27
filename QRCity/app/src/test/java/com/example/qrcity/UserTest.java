/** Author(s): Derek
 *  Purpose: Test methods associated with the User class
 */
package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//TODO: Add more tests as User class is implemented
public class UserTest {
    private TestObjects objects = new TestObjects();

    /** Test Getters **/

    @Test
    public void testGetName() {
        User user = objects.mockBasicUser();
        String name = user.getName();
        assertEquals(0, name.compareTo("mockUser"));
    }

    @Test
    public void testGetUserID() {
        User user = objects.mockBasicUser();
        String userId = user.getUserId();
        assertEquals(0, userId.compareTo("0"));
    }

    @Test
    public void testGetContactInfo() {
        User user = objects.mockAdvancedUser();
        String contactInfo = user.getContactInfo();
        assertEquals(0, contactInfo.compareTo("780-12340-567"));
    }

    @Test
    public void testGetTotalScore() {
        User user = objects.mockBasicUser();
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 0);
    }

    @Test
    public void testGetNumCodes() {
        User user = objects.mockBasicUser();
        long numCodes = user.getNumCodes();
        assertTrue(numCodes == 0);
    }

    @Test
    public void testGetUserCodeList() {
        User user = objects.mockAdvancedUser();
        List<Map> codes = user.getUserCodeList();
        assertEquals(5, codes.size());
        String[] expectedHash = objects.mockScannableCodesIDs();
        for (int i = 0; i < expectedHash.length; i++) {
            Map code = codes.get(i);
            assertEquals(0, expectedHash[i].compareTo((String) code.get("id")));
        }
    }

    /** Test Setters **/

    @Test
    public void testSetName() {
        User user = objects.mockBasicUser();
        user.setName("New Name");
        String name = user.getName();
        assertEquals(0, name.compareTo("New Name"));
    }

    @Test
    public void testSetUserID() {
        User user = objects.mockBasicUser();
        user.setId("1");
        String userId = user.getUserId();
        assertEquals(0, userId.compareTo("1"));
    }

    @Test
    public void testSetContactInfo() {
        User user = objects.mockBasicUser();
        user.setContactInfo("780-9876-543");
        String contactInfo = user.getContactInfo();
        assertEquals(0, contactInfo.compareTo("780-9876-543"));
    }

    @Test
    public void testSetTotalScore() {
        User user = objects.mockBasicUser();
        user.set_Total_Score(128);
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 128);
    }

    @Test
    public void testSetNumCodes() {
        User user = objects.mockBasicUser();
        user.set_Num_Codes(16);
        long numCodes = user.getNumCodes();
        assertTrue(numCodes == 16);
    }

    @Test
    public void testSetUserCodeList() {
        User user = objects.mockBasicUser();

        List<Map> codes = user.getUserCodeList();
        assertEquals(0, codes.size());

        user.setCodeList(objects.mockAdvancedUser().getUserCodeList());

        codes = user.getUserCodeList();
        assertEquals(5, codes.size());
    }

    /** Test Increment methods **/

    @Test
    public void testaddCode() {
        User user = objects.mockBasicUser();
        user.addCode(objects.mockScannableCode());

    }

    @Test
    public void testAddToScore() {
        User user = objects.mockBasicUser();
        user.add_To_Score(128);
        long totalScore = user.getTotalScore();
        assertTrue(totalScore == 128);

        user.add_To_Score(128);
        totalScore = user.getTotalScore();
        assertTrue(totalScore == 256);
    }

    @Test
    public void testAddToNumCodes() {
        User user = objects.mockBasicUser();
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
