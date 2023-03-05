package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScoringSystemTest {

    private String[] hashValues =  {"a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
            "c015ad6ddaf8bb50689d2d7cbf1539dff6dd84473582a08ed1d15d841f4254f4",
            "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6",
            "1234567812345678123456781234567812345678123456781234567812345678",
            "0112223333444445555556666666777777888889999aaabbcddeeeffff000000"};

    private int[] scores = {17, 59, 115, 0, 3275397};

    private String[] names = {"Amateur Wind Mage",
                              "Novice Earth Warrior",
                              "Master Wind Archer",
                              "Divine Water Warrior",
                              "Divine Earth Warrior"};
    @Test
    public void testGetScore() {

        ScoringSystem system = new ScoringSystem();

        for (int i = 0; i < hashValues.length; i++) {
            assertTrue(scores[i] == system.getScore(hashValues[i]));

        }
    }

    @Test
    public void testGetName() {

        ScoringSystem system = new ScoringSystem();

        for (int i = 0; i < hashValues.length; i++) {
            assertEquals(0, names[i].compareTo(system.getName(hashValues[i])));
        }
    }
}
