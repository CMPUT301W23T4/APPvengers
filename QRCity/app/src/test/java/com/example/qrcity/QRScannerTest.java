/** Author(s): Derek
 *  Purpose: Test methods associated with the QRScanner class
 */
package com.example.qrcity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QRScannerTest {

    private String[] inputs =  {"Hello World",
                                "Goodbye",
                                "BFG5DGW54",
                                ""};

    private String[] hashValues =  {"a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
                                    "c015ad6ddaf8bb50689d2d7cbf1539dff6dd84473582a08ed1d15d841f4254f4",
                                    "8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32",
                                    "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"};

    @Test
    public void testCalculateHash() {

        QRScanner scanner = new QRScanner();

        for (int i = 0; i < inputs.length; i++) {
            assertEquals(0,  hashValues[i].compareTo(scanner.calculateHash(inputs[i])));
        }
    }


}
