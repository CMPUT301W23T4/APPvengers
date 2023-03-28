/** Author(s): Derek
 *  Purpose: Provide various mockObjects for testing
 *
 *  References:
 *      1)
 *          ->
 */

package com.example.qrcity.user;

import android.graphics.Bitmap;

import com.example.qrcity.qr.ScannableCode;
import com.example.qrcity.user.User;

import java.util.ArrayList;

/**This is a class dedicated to producing various mockObjects
 * This class should only to be used for testing
 */

public class TestObjects {

    public TestObjects(){}

    /** Scannable codes **/
    public User mockBasicUser() {
        User user = new User("0", "mockUser");
        return user;
    }
    public User mockAdvancedUser() {
        User user = new User("0", "mockUser", "780-12340-567");
        ArrayList<ScannableCode> codes = mockScannableCodes();
        for (ScannableCode code: codes) {
            user.addCode(code);
        }
        return user;
    }

    /** Scannable codes **/
    public ScannableCode mockScannableCode() {
        String hash = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
        String secondHash = "da0dec1c790d6b6197d906cce98981d60a4dd8c8da430fc71961bafaf2a8891a";

        int score = 17;

        String comment = "This is a comment";

        double[] location = {41.40338, 2.17403};

        Bitmap bitmap = null;

        String name = "Amateur Wind Mage";

        ScannableCode qrCode= new ScannableCode(secondHash, score, comment, location, bitmap, name);
        return qrCode;
    }

    public ArrayList<ScannableCode> mockScannableCodes(){
        ArrayList<ScannableCode> qrCodes= new ArrayList<ScannableCode>();

        String[] hashs = mockScannableCodesHashs();
        int[] scores = mockScannableCodesScores();
        String[] comments = mockScannableCodesComments();
        double[][] locations = mockScannableCodesLocations();
        String[] names = mockScannableCodesNames();

        for (int i = 0; i < scores.length; i++) {
            qrCodes.add(new ScannableCode(hashs[i], scores[i], comments[i], locations[i], null, names[i]));
        }

        return qrCodes;
    }

    // these are not real codes, they are just randomly generated attributes
    public String[] mockScannableCodesHashs(){
        String[] hashs = {  "5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9",
                "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b",
                "d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35",
                "4e07408562bedb8b60ce05c1decfe3ad16b72230967de01f640b7e4729b49fce",
                "4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a"};
        return hashs;
    }
    public String[] mockScannableCodesIDs(){
        ArrayList<ScannableCode> codes = mockScannableCodes();
        String[] hashs = {null, null, null, null, null};
        for (int i = 0; i < codes.size(); i++) {
            hashs[i] = codes.get(i).getId();
        }
        return hashs;
    }
    public int[] mockScannableCodesScores(){
        int[] scores = {43, 18, 96, 27, 51};
        return scores;
    }
    public String[] mockScannableCodesComments(){
        String[] comments = {"Comment 0", "Comment 1", "Comment 2", "Comment 3", "Comment 4"};
        return comments;
    }
    public double[][] mockScannableCodesLocations(){
        double[][] locations = {{34.285, 110.189}, {156.023, 137.766}, {151.191, 44.902}, {92.487, 79.376}, {27.147, 67.384}};
        return locations;
    }
    public String[] mockScannableCodesNames(){
        String[] names = {"Novice Earth Thief", "Divine Fire Archer", "Master Wind Mage", "Amateur Earth Warrior", "Novice Water Thief"};
        return names;
    }
}
